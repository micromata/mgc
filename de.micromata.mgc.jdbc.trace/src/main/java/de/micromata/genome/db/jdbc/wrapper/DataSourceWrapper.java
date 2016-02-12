package de.micromata.genome.db.jdbc.wrapper;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Wrapper to Datasource.
 *
 * @author roger
 */
public class DataSourceWrapper implements DataSource
{

  /**
   * The nested data source.
   */
  protected DataSource nestedDataSource;

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    return nestedDataSource.isWrapperFor(iface);
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return nestedDataSource.unwrap(iface);
  }

  /**
   * The wrapper factory.
   */
  protected SqlWrapperFactory wrapperFactory;

  /**
   * Instantiates a new data source wrapper.
   */
  public DataSourceWrapper()
  {

  }

  /**
   * Instantiates a new data source wrapper.
   *
   * @param nestedDataSource the nested data source
   */
  public DataSourceWrapper(DataSource nestedDataSource)
  {
    this.nestedDataSource = nestedDataSource;
  }

  /**
   * Instantiates a new data source wrapper.
   *
   * @param nestedDataSource the nested data source
   * @param wrapperFactory the wrapper factory
   */
  public DataSourceWrapper(DataSource nestedDataSource, SqlWrapperFactory wrapperFactory)
  {
    this.nestedDataSource = nestedDataSource;
    this.wrapperFactory = wrapperFactory;
  }

  @Override
  public Connection getConnection() throws SQLException
  {
    return wrapperFactory.createConnection(this, null, nestedDataSource.getConnection());
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException
  {
    return wrapperFactory.createConnection(this, username, nestedDataSource.getConnection());
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException
  {
    return nestedDataSource.getLogWriter();
  }

  @Override
  public int getLoginTimeout() throws SQLException
  {
    return nestedDataSource.getLoginTimeout();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException
  {
    nestedDataSource.setLogWriter(out);

  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException
  {
    nestedDataSource.setLoginTimeout(seconds);
  }

  public SqlWrapperFactory getWrapperFactory()
  {
    return wrapperFactory;
  }

  public void setWrapperFactory(SqlWrapperFactory wrapperFactory)
  {
    this.wrapperFactory = wrapperFactory;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException
  {
    return Jdbc17Utils.invoke(nestedDataSource, SQLFeatureNotSupportedException.class, "getParentLogger");
  }
}
