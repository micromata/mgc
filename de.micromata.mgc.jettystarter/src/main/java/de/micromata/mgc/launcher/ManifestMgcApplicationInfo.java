package de.micromata.mgc.launcher;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ManifestMgcApplicationInfo implements MgcApplicationInfo
{
  private static final Logger LOG = Logger.getLogger(ManifestMgcApplicationInfo.class);
  private static final String MgcAppName = "MgcAppName";
  private static final String MgcCopyright = "MgcCopyright";
  private static final String MgcVersion = "MgcVersion";
  private static final String MgcDescription = "MgcDescription";
  private static final String MgcLargLogoPath = "MgcLargLogoPath";
  private static final String MgcLicense = "MgcLicense";
  private static final String MgcHomeUrl = "MgcHomeUrl";

  private boolean inited = false;
  private String logoLargePath;
  private String name;
  private String version;
  private String copyright;
  private String detailInfo;
  private String license;
  private String homeUrl;

  private MgcApplication<?> application;

  public ManifestMgcApplicationInfo(MgcApplication<?> application)
  {
    this.application = application;
  }

  protected void init()
  {
    if (inited == true) {
      return;
    }

    Manifest manifest = findManifest();
    if (manifest == null) {
      LOG.error("Canot find MANIFEST.MF with " + MgcAppName + " defined");
      return;
    }
    name = manifest.getMainAttributes().getValue(MgcAppName);
    copyright = manifest.getMainAttributes().getValue(MgcCopyright);
    version = manifest.getMainAttributes().getValue(MgcVersion);
    detailInfo = manifest.getMainAttributes().getValue(MgcDescription);
    logoLargePath = manifest.getMainAttributes().getValue(MgcLargLogoPath);
    license = manifest.getMainAttributes().getValue(MgcLicense);
    homeUrl = manifest.getMainAttributes().getValue(MgcHomeUrl);
    inited = true;

  }

  public Manifest findManifest()
  {
    try {
      Enumeration<URL> resources = application.getClass().getClassLoader()
          .getResources("META-INF/MANIFEST.MF");

      while (resources.hasMoreElements()) {
        try {
          URL url = resources.nextElement();
          Manifest manifest = new Manifest(url.openStream());
          // check that this is your manifest and do what you need or get the next one
          Attributes attrs = manifest.getMainAttributes();

          String val = attrs.getValue(MgcAppName);
          if (StringUtils.isNotBlank(val) == true) {
            return manifest;
          }
        } catch (IOException E) {
          // handle
        }
      }
      return null;
    } catch (IOException ex) {
      return null;
    }
    //    Class<? extends MgcApplication> cl = application.getClass();
    //    InputStream inputStream = null;
    //    try {
    //      URLClassLoader classLoader = (URLClassLoader) cl.getClassLoader();
    //      String classFilePath = cl.getName().replace('.', '/') + ".class";
    //      URL classUrl = classLoader.getResource(classFilePath);
    //      if (classUrl == null) {
    //        return null;
    //      }
    //      String classUri = classUrl.toString();
    //      if (classUri.startsWith("jar:") == false) {
    //        return null;
    //      }
    //      int separatorIndex = classUri.lastIndexOf('!');
    //      if (separatorIndex <= 0) {
    //        return null;
    //      }
    //      String manifestUri = classUri.substring(0, separatorIndex + 2) + "META-INF/MANIFEST.MF";
    //      URL url = new URL(manifestUri);
    //      inputStream = url.openStream();
    //      return new Manifest(inputStream);
    //    } catch (Throwable e) {
    //      return null;
    //    } finally {
    //      if (inputStream != null) {
    //        try {
    //          inputStream.close();
    //        } catch (Throwable e) {
    //          // ignore
    //        }
    //      }
    //    }
  }

  @Override
  public String getLogoLargePath()
  {
    init();
    return logoLargePath;
  }

  @Override
  public String getName()
  {
    init();
    return name;
  }

  @Override
  public String getVersion()
  {
    init();
    return version;
  }

  @Override
  public String getCopyright()
  {
    init();
    return copyright;
  }

  @Override
  public String getDetailInfo()
  {
    init();
    return detailInfo;
  }

  @Override
  public String getLicense()
  {
    init();
    return license;
  }

  @Override
  public String getHomeUrl()
  {
    init();
    return homeUrl;
  }

}
