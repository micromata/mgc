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

package de.micromata.mgc.javafx;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderService;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderServices;

/**
 * Some system services.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public class SystemService
{
  protected static SystemService INSTANCE = new SystemService();

  public static enum OsType
  {
    Windows, Mac,

    Other
  }

  public static SystemService get()
  {
    return INSTANCE;
  }

  public boolean classExists(String name)
  {
    try {
      Class.forName(name, false, getClass().getClassLoader());
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  public List<JdbProviderService> getJdbcDrivers()
  {
    return JdbProviderServices.getAvailableJdbcServices();
  }

  public JdbProviderService findJdbDriverByName(String name)
  {
    for (JdbProviderService js : getJdbcDrivers()) {
      if (js.getName().equals(name) == true) {
        return js;
      }
    }
    return null;
  }

  public JdbProviderService findJdbDriverByJdbcDriver(String name)
  {
    for (JdbProviderService js : getJdbcDrivers()) {
      if (js.getJdbcDriver().equals(name) == true) {
        return js;
      }
    }
    return null;
  }

  public JdbProviderService findJdbDriverById(String name)
  {
    return JdbProviderServices.findJdbcServiceById(name);
  }

  public void openUrlInBrowser(String url)
  {
    Desktop desktop = null;
    if (Desktop.isDesktopSupported()) {
      desktop = Desktop.getDesktop();
    } else {
      GLog.warn(GenomeLogCategory.System, "Launching Browser not supported");
      return;
    }

    if (desktop != null) {
      try {
        desktop.browse(new URI(url));
      } catch (final IOException ex) {
        GLog.error(GenomeLogCategory.System, "Can't launch browser: " + ex.getMessage(), new LogExceptionAttribute(ex));
      } catch (final URISyntaxException ex) {
        GLog.error(GenomeLogCategory.System, "Can't launch browser: " + ex.getMessage(), new LogExceptionAttribute(ex));
      }
    }
  }

  public OsType getOsType()
  {
    String osName = System.getProperty("os.name");
    //    if (true) {
    //      return OsType.Mac;
    //    }
    OsType osType = OsType.Other;
    if (StringUtils.startsWith(osName, "Windows") == true) {
      osType = OsType.Windows;
    } else if (StringUtils.startsWith(osName, "Mac") == true) {
      osType = OsType.Mac;
    }
    return osType;

  }
}