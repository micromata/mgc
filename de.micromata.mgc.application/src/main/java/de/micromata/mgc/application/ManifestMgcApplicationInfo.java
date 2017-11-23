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

package de.micromata.mgc.application;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
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
  private static final String MgcHelpUrl = "MgcHelpUrl";

  private boolean inited = false;
  private String logoLargePath;
  private String name;
  private String version;
  private String copyright;
  private String detailInfo;
  private String license;
  private String homeUrl;
  private String helpUrl;
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
    helpUrl = manifest.getMainAttributes().getValue(MgcHelpUrl);
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

  @Override
  public String getHelpUrl()
  {
    init();
    return helpUrl;
  }

  public boolean hasHelpUrl()
  {
    return StringUtils.isNotBlank(getHelpUrl());
  }
}
