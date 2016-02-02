package de.micromata.genome.util.runtime;

/**
 * Extension to common lang exception utils
 * 
 * @author Roger Rene Kommer (r.kommer@micromata.de)
 * 
 */
public class ExceptionUtils extends org.apache.commons.lang.exception.ExceptionUtils
{
  /**
   * Rethrow an exception into Error, RuntimeException or Exception declared. If non, a RuntimeException will be used.
   * 
   * @param ex
   * @param declared
   * @throws Exception
   */
  public static void wrappException(Throwable ex, Class<? extends Exception>... declared) throws Exception
  {
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    for (Class<? extends Exception> ce : declared) {
      if (ce.isAssignableFrom(ex.getClass()) == true) {
        throw (Exception) ex;
      }
    }
    throw new RuntimeException(ex);
  }

  /**
   * Unwrapp an rethrow Throwable as Exception or Error.
   *
   * @param ex the ex
   * @throws Exception the exception
   */
  public static void throwUnwrappedThrowableToException(Throwable ex) throws Exception
  {
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    if (ex instanceof Exception) {
      throw (Exception) ex;
    }
    throw new IllegalArgumentException("Exception is Neither error or Exception: " + ex.getClass(), ex);
  }
}
