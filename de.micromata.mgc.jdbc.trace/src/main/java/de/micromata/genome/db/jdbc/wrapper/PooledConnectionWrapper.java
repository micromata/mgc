//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
