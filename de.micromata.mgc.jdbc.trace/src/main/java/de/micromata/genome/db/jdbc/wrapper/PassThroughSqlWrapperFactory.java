package de.micromata.genome.db.jdbc.wrapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * A Wrapper factory, which simply pass through orinial interface.
 *
 * @author roger
 */
public class PassThroughSqlWrapperFactory implements SqlWrapperFactory
{

  @Override
  public Connection createConnection(DataSourceWrapper dataSource, String user, Connection connection)
  {
    return connection;
  }

  @Override
  public DataSource createDataSource(DataSource dataSource)
  {
    return dataSource;
  }

  @Override
  public PreparedStatement createPreparedStatement(ConnectionWrapper connection, String sql,
      PreparedStatement preparedStatement)
  {
    return preparedStatement;
  }

  @Override
  public Statement createStatement(ConnectionWrapper connection, Statement statement)
  {
    return statement;
  }

  @Override
  public CallableStatement createCallableStatement(ConnectionWrapper connection, CallableStatement preparedStatement)
  {
    return preparedStatement;
  }

}
