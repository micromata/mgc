package de.micromata.genome.db.jdbc.wrapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * A factory for creating SqlWrapper objects.
 *
 * @author roger
 */
public interface SqlWrapperFactory
{

  /**
   * Creates a new SqlWrapper object.
   *
   * @param dataSource the data source
   * @return the data source
   */
  public DataSource createDataSource(DataSource dataSource);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param dataSource the data source
   * @param userName the user name
   * @param connection the connection
   * @return the connection
   */
  public Connection createConnection(DataSourceWrapper dataSource, String userName, Connection connection);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param connection the connection
   * @param statement the statement
   * @return the statement
   */
  public Statement createStatement(ConnectionWrapper connection, Statement statement);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param connection the connection
   * @param sql the sql
   * @param preparedStatement the prepared statement
   * @return the prepared statement
   */
  public PreparedStatement createPreparedStatement(ConnectionWrapper connection, String sql,
      PreparedStatement preparedStatement);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param connection the connection
   * @param preparedStatement the prepared statement
   * @return the callable statement
   */
  public CallableStatement createCallableStatement(ConnectionWrapper connection, CallableStatement preparedStatement);
}
