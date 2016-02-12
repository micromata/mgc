package de.micromata.genome.db.jdbc.trace;

import java.sql.CallableStatement;

import de.micromata.genome.db.jdbc.wrapper.CallableStatementWrapper;

/**
 * The Class TraceCallableStatement.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TraceCallableStatement extends CallableStatementWrapper
{

  /**
   * The trace connection.
   */
  private TraceConnection traceConnection;

  /**
   * Instantiates a new trace callable statement.
   */
  public TraceCallableStatement()
  {
  }

  /**
   * Instantiates a new trace callable statement.
   *
   * @param traceConnection the trace connection
   * @param nestedStatement the nested statement
   */
  public TraceCallableStatement(TraceConnection traceConnection, CallableStatement nestedStatement)
  {
    super(nestedStatement);
    this.traceConnection = traceConnection;
  }

  // TODO overwrite implement methods

  public TraceConnection getTraceConnection()
  {
    return traceConnection;
  }

  public void setTraceConnection(TraceConnection traceConnection)
  {
    this.traceConnection = traceConnection;
  }
}
