package de.micromata.genome.util.runtime;

/**
 * Like Callable, but with no exception
 * 
 * @author roger
 * 
 */
public interface RuntimeCallable
{

  /**
   * Call.
   */
  public void call();
}
