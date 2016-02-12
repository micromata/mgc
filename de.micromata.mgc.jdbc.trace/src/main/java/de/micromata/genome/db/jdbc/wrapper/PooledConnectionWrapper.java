package de.micromata.genome.db.jdbc.wrapper;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

/**
 * The Class PooledConnectionWrapper.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class PooledConnectionWrapper implements PooledConnection
{

  /**
   * The nested pooled connection.
   */
  protected PooledConnection nestedPooledConnection;

  @Override
  public void addStatementEventListener(StatementEventListener listener)
  {
    nestedPooledConnection.addStatementEventListener(listener);
  }

  @Override
  public void removeStatementEventListener(StatementEventListener listener)
  {
    nestedPooledConnection.removeStatementEventListener(listener);
  }

  /**
   * The data source wrapper.
   */
  protected DataSourceWrapper dataSourceWrapper;

  /**
   * The wrapper factory.
   */
  protected SqlWrapperFactory wrapperFactory;

  /**
   * Instantiates a new pooled connection wrapper.
   */
  public PooledConnectionWrapper()
  {

  }

  /**
   * Instantiates a new pooled connection wrapper.
   *
   * @param nestedPooledConnection the nested pooled connection
   * @param wrapperFactory the wrapper factory
   * @param dataSourceWrapper the data source wrapper
   */
  public PooledConnectionWrapper(PooledConnection nestedPooledConnection, SqlWrapperFactory wrapperFactory,
      DataSourceWrapper dataSourceWrapper)
  {
    this.nestedPooledConnection = nestedPooledConnection;
    this.wrapperFactory = wrapperFactory;
    this.dataSourceWrapper = dataSourceWrapper;
  }

  @Override
  public void addConnectionEventListener(ConnectionEventListener listener)
  {
    nestedPooledConnection.addConnectionEventListener(listener);
  }

  @Override
  public void close() throws SQLException
  {
    nestedPooledConnection.close();
  }

  @Override
  public Connection getConnection() throws SQLException
  {
    return wrapperFactory.createConnection(dataSourceWrapper, null, nestedPooledConnection.getConnection());
  }

  @Override
  public void removeConnectionEventListener(ConnectionEventListener listener)
  {
    nestedPooledConnection.removeConnectionEventListener(listener);
  }

  public PooledConnection getNestedPooledConnection()
  {
    return nestedPooledConnection;
  }

  public void setNestedPooledConnection(PooledConnection nestedPooledConnection)
  {
    this.nestedPooledConnection = nestedPooledConnection;
  }

  public DataSourceWrapper getDataSourceWrapper()
  {
    return dataSourceWrapper;
  }

  public void setDataSourceWrapper(DataSourceWrapper dataSourceWrapper)
  {
    this.dataSourceWrapper = dataSourceWrapper;
  }

  public SqlWrapperFactory getWrapperFactory()
  {
    return wrapperFactory;
  }

  public void setWrapperFactory(SqlWrapperFactory wrapperFactory)
  {
    this.wrapperFactory = wrapperFactory;
  }

}
