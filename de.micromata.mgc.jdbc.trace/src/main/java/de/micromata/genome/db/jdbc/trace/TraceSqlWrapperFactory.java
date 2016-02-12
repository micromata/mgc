package de.micromata.genome.db.jdbc.trace;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

import de.micromata.genome.db.jdbc.wrapper.ConnectionWrapper;
import de.micromata.genome.db.jdbc.wrapper.DataSourceWrapper;
import de.micromata.genome.db.jdbc.wrapper.SqlWrapperFactory;

/**
 * A factory for creating TraceSqlWrapper objects.
 *
 * @author roger
 */
public class TraceSqlWrapperFactory implements SqlWrapperFactory
{

  @Override
  public CallableStatement createCallableStatement(ConnectionWrapper connection, CallableStatement preparedStatement)
  {
    return new TraceCallableStatement((TraceConnection) connection, preparedStatement);
  }

  @Override
  public Connection createConnection(DataSourceWrapper dataSource, String userName, Connection connection)
  {
    return new TraceConnection((TraceDataSource) dataSource, userName, connection);
  }

  @Override
  public DataSource createDataSource(DataSource dataSource)
  {
    return new TraceDataSource(dataSource);
  }

  @Override
  public PreparedStatement createPreparedStatement(ConnectionWrapper connection, String sql,
      PreparedStatement preparedStatement)
  {
    return new TracePreparedStatement((TraceConnection) connection, sql, preparedStatement);
  }

  @Override
  public Statement createStatement(ConnectionWrapper connection, Statement statement)
  {
    return new TraceStatement((TraceConnection) connection, statement);
  }

}
