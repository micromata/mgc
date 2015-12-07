package de.micromata.genome.util.runtime;

/**
 * Like callable, but also allow to customize Exception to throw
 * 
 * @author roger
 * 
 * @param <V> Return type
 * @param <EX> Exception type.
 */
public interface CallableX<V, EX extends Throwable>
{

  /**
   * Call method.
   *
   * @return the v
   * @throws EX the ex
   */
  public V call() throws EX;
}
