package de.micromata.genome.db.jdbc.wrapper;

import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Little utility to invoke JDBC Library functions by reflection.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class Jdbc17Utils
{
  /**
   * Invoking an exception
   * 
   * @param target target bean to call
   * @param exclass expected exception class
   * @param name of the function
   * @param args of the function
   * @return value of the function
   * @throws EX thrown exception type
   */
  @SuppressWarnings("unchecked")
  public static <T, EX extends Exception> T invoke(Object target, Class<EX> exclass, String name, Object... args)
      throws EX
  {
    try {
      return (T) PrivateBeanUtils.invokeMethod(target, name, args);
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      if (exclass.isAssignableFrom(ex.getCause().getClass()) == true) {
        throw (EX) ex.getCause();
      }
      throw ex;
    }
  }
}
