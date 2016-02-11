package de.micromata.genome.db.jdbc.trace;

/**
 * The Interface JdbcTraced.
 *
 * @author roger
 */
public interface JdbcTraced
{

  /**
   * Gets the trace config.
   *
   * @return the trace config
   */
  public TraceConfig getTraceConfig();

  /**
   * Checks if is in recursion.
   *
   * @return true, if is in recursion
   */
  public boolean isInRecursion();

  /**
   * Lock recursion.
   */
  public void lockRecursion();

  /**
   * Release recursion.
   */
  public void releaseRecursion();
}
