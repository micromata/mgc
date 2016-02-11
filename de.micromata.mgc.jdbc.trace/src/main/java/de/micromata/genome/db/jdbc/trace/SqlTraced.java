package de.micromata.genome.db.jdbc.trace;

/**
 * The Interface SqlTraced.
 *
 * @author roger
 */
public interface SqlTraced
{

  /**
   * Gets the sql arguments.
   *
   * @return the sql arguments
   */
  public TraceSqlArguments getSqlArguments();

  /**
   * Gets the trace connection.
   *
   * @return the trace connection
   */
  public TraceConnection getTraceConnection();
}
