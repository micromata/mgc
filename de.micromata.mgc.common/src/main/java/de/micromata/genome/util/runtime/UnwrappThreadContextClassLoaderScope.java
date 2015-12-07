package de.micromata.genome.util.runtime;

/**
 * works with ThreadContextClassLoaderScope. Unwrapp one scope. muss innerhalb try/finally aufgerufen werden, wobei im
 * finally-Block ThreadContextClassLoaderScope.restore() aufgerufen werden muss.
 * 
 * @author roger
 * 
 */
public class UnwrappThreadContextClassLoaderScope
{

  /**
   * The lscope.
   */
  ThreadContextClassLoaderScope lscope;

  /**
   * Instantiates a new unwrapp thread context class loader scope.
   */
  public UnwrappThreadContextClassLoaderScope()
  {
    lscope = ThreadContextClassLoaderScope.peek();
    if (lscope != null) {
      Thread.currentThread().setContextClassLoader(lscope.getPreviousClassLoader());
    }
  }

  /**
   * Reset.
   */
  public void reset()
  {
    if (lscope != null) {
      Thread.currentThread().setContextClassLoader(lscope.getNewClassLoader());
    }
  }
}
