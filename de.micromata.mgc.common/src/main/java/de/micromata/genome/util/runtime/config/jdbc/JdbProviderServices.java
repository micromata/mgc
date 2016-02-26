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
