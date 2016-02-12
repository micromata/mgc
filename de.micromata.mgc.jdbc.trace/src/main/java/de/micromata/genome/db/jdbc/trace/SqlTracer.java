package de.micromata.genome.db.jdbc.trace;

import java.sql.SQLException;
import java.sql.Savepoint;

import de.micromata.genome.util.runtime.CallableX;

/**
 * Traces SQL statements.
 * 
 * @author roger
 * 
 */
public interface SqlTracer
{
  /**
   * Execute a statement.
   * 
   * @param sqlTraced
   * @param sql
   * @param callable
   * @return
   * @throws SQLException
   */
  public <T> T executeWrapped(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable) throws SQLException;

  /**
   * Execute a prepareStatement/Call
   * 
   * @param sqlTraced
   * @param sql
   * @param callable
   * @return
   * @throws SQLException
   */
  public <T> T executePreparedWrapped(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable) throws SQLException;

  /**
   * Doing a rollback.
   * 
   * @param connection
   * @param savepoint
   */
  public void rollback(TraceConnection connection, Savepoint savepoint);

  /**
   * Doing a commit.
   * 
   * @param connection
   */
  public void commit(TraceConnection connection) throws SQLException;

}
