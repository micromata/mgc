package de.micromata.mgc.javafx;

import java.util.ArrayList;
import java.util.List;

/**
 * Some system services.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public class SystemService
{
  protected static SystemService INSTANCE = new SystemService();

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

  public List<JdbcDriverDescription> getJdbcDrivers()
  {
    List<JdbcDriverDescription> ret = new ArrayList<>();
    for (JdbcDriverDescription r : StandardJdbcDriverDescriptions.values()) {
      String classname = r.getDriverClassName();
      if (classExists(classname) == true) {
        ret.add(r);
      }

    }
    return ret;
  }
}