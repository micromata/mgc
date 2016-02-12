package de.micromata.genome.db.jdbc.wrapper;

import java.sql.SQLException;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

/**
 * The Class XAConnectionWrapper.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class XAConnectionWrapper extends PooledConnectionWrapper implements XAConnection
{

  /**
   * The nested xa connection.
   */
  protected XAConnection nestedXAConnection;

  /**
   * Instantiates a new XA connection wrapper.
   */
  public XAConnectionWrapper()
  {

  }

  /**
   * Instantiates a new XA connection wrapper.
   *
   * @param nestedXaConnection the nested xa connection
   * @param wrapperFactory the wrapper factory
   * @param dataSourceWrapper the data source wrapper
   */
  public XAConnectionWrapper(XAConnection nestedXaConnection, SqlWrapperFactory wrapperFactory,
      DataSourceWrapper dataSourceWrapper)
  {
    super(nestedXaConnection, wrapperFactory, dataSourceWrapper);
    this.nestedXAConnection = nestedXaConnection;
  }

  @Override
  public XAResource getXAResource() throws SQLException
  {
    return nestedXAConnection.getXAResource();
  }

  public XAConnection getNestedXAConnection()
  {
    return nestedXAConnection;
  }

  public void setNestedXAConnection(XAConnection nestedXAConnection)
  {
    this.nestedXAConnection = nestedXAConnection;
  }

}
