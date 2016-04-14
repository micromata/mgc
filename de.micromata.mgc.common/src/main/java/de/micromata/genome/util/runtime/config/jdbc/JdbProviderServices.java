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

package de.micromata.genome.util.runtime.config.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Provides Jdbc Driver support.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JdbProviderServices
{
  public static JdbProviderService findProviderByName(String name)
  {
    ServiceLoader<JdbProviderService> loader = ServiceLoader.load(JdbProviderService.class);
    List<JdbProviderService> ret = new ArrayList<>();
    for (JdbProviderService ps : loader) {
      if (ps.getName().equals(name) == true) {
        return ps;
      }
    }
    return null;
  }

  public static JdbProviderService findProviderId(String id)
  {
    ServiceLoader<JdbProviderService> loader = ServiceLoader.load(JdbProviderService.class);
    List<JdbProviderService> ret = new ArrayList<>();
    for (JdbProviderService ps : loader) {
      if (ps.getId().equals(id) == true) {
        return ps;
      }
    }
    return null;
  }

  /**
   * Give availabe (with existant jdbc driver in classpath) jdbc services.
   * 
   * @return
   */
  public static List<JdbProviderService> getAvailableJdbcServices()
  {
    ServiceLoader<JdbProviderService> loader = ServiceLoader.load(JdbProviderService.class);
    List<JdbProviderService> ret = new ArrayList<>();
    for (JdbProviderService ps : loader) {
      if (driverIsEnabled(ps) == true) {
        ret.add(ps);
      }
    }
    return ret;
  }

  /**
   * Give all jdbc services.
   * 
   * @return
   */
  public static List<JdbProviderService> getAllJdbcServices()
  {
    ServiceLoader<JdbProviderService> loader = ServiceLoader.load(JdbProviderService.class);
    List<JdbProviderService> ret = new ArrayList<>();
    for (JdbProviderService ps : loader) {
      ret.add(ps);
    }
    return ret;
  }

  public static JdbProviderService findJdbcServiceById(String id)
  {
    List<JdbProviderService> services = getAllJdbcServices();
    for (JdbProviderService s : services) {
      if (s.getId().equals(id) == true) {
        return s;
      }
    }
    return null;
  }

  public static JdbProviderService findJdbcServiceByJdbDriver(String id)
  {
    List<JdbProviderService> services = getAllJdbcServices();
    for (JdbProviderService s : services) {
      if (s.getJdbcDriver().equals(id) == true) {
        return s;
      }
    }
    return null;
  }

  /**
   * Try to load jdbc driver.
   *
   * @param ps the ps
   * @return true, if successful
   */
  public static boolean driverIsEnabled(JdbProviderService ps)
  {
    try {
      Class.forName(ps.getJdbcDriver(), false, ps.getClass().getClassLoader());
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

}
