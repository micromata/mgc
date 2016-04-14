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
