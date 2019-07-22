//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.jpa.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.archive.internal.StandardArchiveDescriptorFactory;
import org.hibernate.boot.archive.scan.internal.ScanResultCollector;
import org.hibernate.boot.archive.scan.spi.ClassFileArchiveEntryHandler;
import org.hibernate.boot.archive.scan.spi.NonClassFileArchiveEntryHandler;
import org.hibernate.boot.archive.scan.spi.PackageInfoArchiveEntryHandler;
import org.hibernate.boot.archive.scan.spi.ScanEnvironment;
import org.hibernate.boot.archive.scan.spi.ScanOptions;
import org.hibernate.boot.archive.scan.spi.ScanParameters;
import org.hibernate.boot.archive.scan.spi.ScanResult;
import org.hibernate.boot.archive.scan.spi.Scanner;
import org.hibernate.boot.archive.spi.ArchiveContext;
import org.hibernate.boot.archive.spi.ArchiveDescriptor;
import org.hibernate.boot.archive.spi.ArchiveDescriptorFactory;
import org.hibernate.boot.archive.spi.ArchiveEntry;
import org.hibernate.boot.archive.spi.ArchiveEntryHandler;
import org.hibernate.jpa.boot.internal.StandardJpaScanEnvironmentImpl;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.BooleanListRulesFactory;
import de.micromata.genome.util.matcher.CommonMatchers;
import de.micromata.genome.util.matcher.Matcher;

/**
 * Scanner, which looks into class path, and optionally additionally libraries.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Florian Blumenstein
 *
 */
public class JpaWithExtLibrariesScanner implements Scanner
{
  static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JpaWithExtLibrariesScanner.class);

  /**
   * Class name which has to implement JpaExtScannerUrlProvider.
   */
  public static final String EXTLIBURLPROVIDER = "de.micromata.genome.jpa.extlibrary.urlprovider";
  /**
   * Url matcher expression
   */
  public static final String EXTLIBURLMATCHER = "de.micromata.genome.jpa.extlibrary.urlmatcher";

  private final ArchiveDescriptorFactory archiveDescriptorFactory;

  private final Map<String, ArchiveDescriptorInfo> archiveDescriptorCache = new HashMap<String, ArchiveDescriptorInfo>();

  public JpaWithExtLibrariesScanner()
  {
    this(StandardArchiveDescriptorFactory.INSTANCE);
  }

  public JpaWithExtLibrariesScanner(ArchiveDescriptorFactory value)
  {
    this.archiveDescriptorFactory = value;
  }

  @Override
  public ScanResult scan(ScanEnvironment environment, ScanOptions options, ScanParameters parameters)
  {
    ScanResultCollector collector = new ScanResultCollector(environment, options, parameters);

    if (environment.getNonRootUrls() != null) {
      ArchiveContext context = new ArchiveContextImpl(false, collector);
      for (URL url : environment.getNonRootUrls()) {
        ArchiveDescriptor descriptor = buildArchiveDescriptor(url, false);
        descriptor.visitArchive(context);
      }
    }
    Set<URL> loadedUrls = new HashSet<>();
    if (environment.getRootUrl() != null) {
      URL rootUrl = environment.getRootUrl();
      visitUrl(rootUrl, collector, CommonMatchers.always());
    }
    visitExternUrls(environment, collector, loadedUrls);
    return collector.toScanResult();
  }

  protected void visitUrl(URL url, ScanResultCollector collector, Matcher<String> urlMatcher)
  {
    if (urlMatcher.match(url.toString()) == false) {
      return;
    }
    ArchiveContext context = new ArchiveContextImpl(true, collector);
    String surl = url.toString();
    if (surl.contains("!")) {
      String customUrlStr = url.toString();
      if (surl.startsWith("jar:") == false) {
        customUrlStr = "jar:" + customUrlStr;
      }
      // Remove the trail after the second '!', otherwise a file not found exception will be thrown.
      // e. g.: jar:file:/...myjar.jar!/BOOT-INF/lib/org.projectforge.plugins.memo-7.0-SNAPSHOT.jar!/
      // This workaround is needed by ProjectForge. In former version it works, because another exception
      // was thrown by Spring while building ArchiveDescriptor ;-)
      if (customUrlStr.lastIndexOf('!') > customUrlStr.indexOf('!')) {
        customUrlStr = customUrlStr.substring(0, customUrlStr.lastIndexOf('!'));
      }
      log.info("Custom URL: " + customUrlStr);
      try {
        URL customUrl = new URL(customUrlStr);
        ArchiveDescriptor descriptor = buildArchiveDescriptor(customUrl, true);
        descriptor.visitArchive(context);
      } catch (MalformedURLException e) {
        log.error("Error while getting custom URL: " + customUrlStr);
      }
    } else {
      final ArchiveDescriptor descriptor = buildArchiveDescriptor(url, true);
      descriptor.visitArchive(context);
      handleClassManifestClassPath(url, collector, urlMatcher);
    }
  }

  URL fixUrlToOpen(URL url)
  {
    String surl = url.toString();
    String orgurl = surl;
    if (surl.endsWith("!/") == true) {
      surl = surl.substring(0, surl.length() - 2);
    }
    if (StringUtils.startsWith(surl, "jar:jar:file:") == true) {
      surl = surl.substring("jar:jar:".length());

    }
    if (StringUtils.startsWith(surl, "jar:file:") == true) {
      surl = surl.substring("jar:".length());
    }
    try {
      URL ret = new URL(surl);
      log.info("Patches url from " + orgurl + " to " + surl);
      return ret;
    } catch (MalformedURLException ex) {
      log.warn("Cannot parse patched url: " + surl + "; " + ex.getMessage());
      return url;
    }

  }

  /**
   * A jar may have also declared more deps in manifest (like surefire).
   *
   * @param url the url
   * @param collector the collector to use
   */
  @SuppressWarnings("deprecation")
  private void handleClassManifestClassPath(URL url, ScanResultCollector collector, Matcher<String> urlMatcher)
  {
    String urls = url.toString();
    URL urltoopen = fixUrlToOpen(url);
    urls = urltoopen.toString();
    if (urls.endsWith(".jar") == false) {
      return;
    }

    try (InputStream is = urltoopen.openStream()) {
      try (JarInputStream jarStream = new JarInputStream(is)) {
        Manifest manifest = jarStream.getManifest();
        if (manifest == null) {
          return;
        }
        Attributes attr = manifest.getMainAttributes();
        String val = attr.getValue("Class-Path");
        if (StringUtils.isBlank(val) == true) {
          return;
        }
        String[] entries = StringUtils.split(val, " \t\n");
        for (String entry : entries) {
          URL surl = new URL(entry);
          visitUrl(surl, collector, urlMatcher);
        }

      }

    } catch (IOException ex) {
      log.warn("JpaScan; Cannot open jar: " + url + ": " + ex.getMessage());
    }

  }

  protected void visitExternUrls(ScanEnvironment environment, ScanResultCollector collector, Set<URL> loadedUrls)
  {
    String matcherexppr = getPersistenceProperties(environment).getProperty(EXTLIBURLMATCHER);
    Matcher<String> urlmatcher = CommonMatchers.always();
    if (StringUtils.isNotBlank(matcherexppr) == true) {
      urlmatcher = new BooleanListRulesFactory<String>().createMatcher(matcherexppr);
    }
    JpaExtScannerUrlProvider prov = loadJpaExtScannerUrlProvider(environment, collector);
    Collection<URL> urls = prov.getScannUrls();
    for (URL url : urls) {
      if (loadedUrls.contains(url) == true) {
        continue;
      }

      try {
        visitUrl(url, collector, urlmatcher);
        loadedUrls.add(url);

      } catch (Exception ex) {
        log.warn("Cannot scan " + url + "; " + ex.getMessage());
      }
    }
  }

  private Properties getPersistenceProperties(ScanEnvironment environment)
  {
    if ((environment instanceof StandardJpaScanEnvironmentImpl) == false) {
      log.warn("environment is not StandardJpaScanEnvironmentImpl: " + environment.getClass());
      return new Properties();
    }
    PersistenceUnitDescriptor pud = (PersistenceUnitDescriptor) PrivateBeanUtils.readField(environment,
        "persistenceUnitDescriptor");
    return pud.getProperties();
  }

  protected JpaExtScannerUrlProvider loadJpaExtScannerUrlProvider(ScanEnvironment environment,
      ScanResultCollector collector)
  {
    Properties properties = getPersistenceProperties(environment);
    String provider = properties.getProperty(EXTLIBURLPROVIDER);
    if (StringUtils.isBlank(provider) == true) {
      return null;
    }
    try {
      Class<?> clazz = Class.forName(provider);
      JpaExtScannerUrlProvider urlpr = (JpaExtScannerUrlProvider) clazz.newInstance();
      return urlpr;
    } catch (Exception ex) {
      log.error("Cannot create JpaExtScannerUrlProvider: " + ex.getMessage(), ex);
      return null;
    }
  }

  private ArchiveDescriptor buildArchiveDescriptor(URL url, boolean isRootUrl)
  {
    final ArchiveDescriptor descriptor;
    final ArchiveDescriptorInfo descriptorInfo = archiveDescriptorCache.get(url.toString());
    if (descriptorInfo == null) {
      descriptor = archiveDescriptorFactory.buildArchiveDescriptor(url);
      archiveDescriptorCache.put(
          url.toString(),
          new ArchiveDescriptorInfo(descriptor, isRootUrl));
    } else {
      validateReuse(descriptorInfo, isRootUrl);
      descriptor = descriptorInfo.archiveDescriptor;
    }
    return descriptor;
  }

  // This needs to be protected and attributes/constructor visible in case
  // a custom scanner needs to override validateReuse.
  protected static class ArchiveDescriptorInfo
  {
    public final ArchiveDescriptor archiveDescriptor;
    public final boolean isRoot;

    public ArchiveDescriptorInfo(ArchiveDescriptor archiveDescriptor, boolean isRoot)
    {
      this.archiveDescriptor = archiveDescriptor;
      this.isRoot = isRoot;
    }
  }

  @SuppressWarnings("UnusedParameters")
  protected void validateReuse(ArchiveDescriptorInfo descriptor, boolean root)
  {
    // is it really reasonable that a single url be processed multiple times?
    // for now, throw an exception, mainly because I am interested in situations where this might happen
    throw new IllegalStateException("ArchiveDescriptor reused; can URLs be processed multiple times?");
  }

  public static class ArchiveContextImpl implements ArchiveContext
  {
    private final boolean isRootUrl;

    private final ClassFileArchiveEntryHandler classEntryHandler;
    private final PackageInfoArchiveEntryHandler packageEntryHandler;
    private final ArchiveEntryHandler fileEntryHandler;

    public ArchiveContextImpl(boolean isRootUrl, ScanResultCollector scanResultCollector)
    {
      this.isRootUrl = isRootUrl;

      this.classEntryHandler = new ClassFileArchiveEntryHandler(scanResultCollector);
      this.packageEntryHandler = new PackageInfoArchiveEntryHandler(scanResultCollector);
      this.fileEntryHandler = new NonClassFileArchiveEntryHandler(scanResultCollector);
    }

    @Override
    public boolean isRootUrl()
    {
      return isRootUrl;
    }

    @Override
    public ArchiveEntryHandler obtainArchiveEntryHandler(ArchiveEntry entry)
    {
      final String nameWithinArchive = entry.getNameWithinArchive();

      if (nameWithinArchive.endsWith("package-info.class")) {
        return packageEntryHandler;
      } else if (nameWithinArchive.endsWith(".class")) {
        return classEntryHandler;
      } else {
        return fileEntryHandler;
      }
    }
  }
}
