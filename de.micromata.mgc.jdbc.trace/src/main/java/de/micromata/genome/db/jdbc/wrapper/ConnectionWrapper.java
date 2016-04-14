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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Wrapper to connection.
 *
 * @author roger
 */
public class ConnectionWrapper implements Connection
{

  /**
   * The nested connection.
   */
  protected Connection nestedConnection;

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException
  {
    return nestedConnection.createArrayOf(typeName, elements);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Blob createBlob() throws SQLException
  {
    return nestedConnection.createBlob();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Clob createClob() throws SQLException
  {
    return nestedConnection.createClob();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public NClob createNClob() throws SQLException
  {
    return nestedConnection.createNClob();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public SQLXML createSQLXML() throws SQLException
  {
    return nestedConnection.createSQLXML();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException
  {
    return nestedConnection.createStruct(typeName, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Properties getClientInfo() throws SQLException
  {
    return nestedConnection.getClientInfo();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getClientInfo(String name) throws SQLException
  {
    return nestedConnection.getClientInfo(name);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isValid(int timeout) throws SQLException
  {
    return nestedConnection.isValid(timeout);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    return nestedConnection.isWrapperFor(iface);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException
  {
    nestedConnection.setClientInfo(properties);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException
  {
    nestedConnection.setClientInfo(name, value);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return nestedConnection.unwrap(iface);
  }

  /**
   * The wrapper factory.
   */
  protected SqlWrapperFactory wrapperFactory;

  /**
   * Instantiates a new connection wrapper.
   */
  public ConnectionWrapper()
  {

  }

  /**
   * Instantiates a new connection wrapper.
   *
   * @param nestedConnection the nested connection
   * @param wrapperFactory the wrapper factory
   */
  public ConnectionWrapper(Connection nestedConnection, SqlWrapperFactory wrapperFactory)
  {
    this.nestedConnection = nestedConnection;
    this.wrapperFactory = wrapperFactory;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void clearWarnings() throws SQLException
  {
    nestedConnection.clearWarnings();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void close() throws SQLException
  {
    nestedConnection.close();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void commit() throws SQLException
  {
    nestedConnection.commit();

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Statement createStatement() throws SQLException
  {
    return wrapperFactory.createStatement(this, nestedConnection.createStatement());
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
  {
    return wrapperFactory.createStatement(this, nestedConnection.createStatement(resultSetType, resultSetConcurrency));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException
  {
    return wrapperFactory
        .createStatement(this,
            nestedConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean getAutoCommit() throws SQLException
  {
    return nestedConnection.getAutoCommit();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getCatalog() throws SQLException
  {
    return nestedConnection.getCatalog();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getHoldability() throws SQLException
  {
    return nestedConnection.getHoldability();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public DatabaseMetaData getMetaData() throws SQLException
  {
    return nestedConnection.getMetaData();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getTransactionIsolation() throws SQLException
  {
    return nestedConnection.getTransactionIsolation();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException
  {
    return nestedConnection.getTypeMap();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public SQLWarning getWarnings() throws SQLException
  {
    return nestedConnection.getWarnings();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isClosed() throws SQLException
  {
    return nestedConnection.isClosed();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isReadOnly() throws SQLException
  {
    return nestedConnection.isReadOnly();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String nativeSQL(String sql) throws SQLException
  {
    return nestedConnection.nativeSQL(sql);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException
  {
    return wrapperFactory.createCallableStatement(this, nestedConnection.prepareCall(sql));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
  {
    return wrapperFactory.createCallableStatement(this,
        nestedConnection.prepareCall(sql, resultSetType, resultSetConcurrency));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability)
          throws SQLException
  {
    return wrapperFactory.createCallableStatement(this,
        nestedConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException
  {
    return wrapperFactory.createPreparedStatement(this, sql, nestedConnection.prepareStatement(sql));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
  {
    return wrapperFactory.createPreparedStatement(this, sql, nestedConnection.prepareStatement(sql, autoGeneratedKeys));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
  {
    return wrapperFactory.createPreparedStatement(this, sql, nestedConnection.prepareStatement(sql, columnIndexes));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
  {
    return wrapperFactory.createPreparedStatement(this, sql, nestedConnection.prepareStatement(sql, columnNames));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
  {
    return wrapperFactory.createPreparedStatement(this, sql,
        nestedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability)
          throws SQLException
  {
    return wrapperFactory.createPreparedStatement(this, sql,
        nestedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException
  {
    nestedConnection.releaseSavepoint(savepoint);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void rollback() throws SQLException
  {
    nestedConnection.rollback();

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void rollback(Savepoint savepoint) throws SQLException
  {
    nestedConnection.releaseSavepoint(savepoint);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException
  {
    nestedConnection.setAutoCommit(autoCommit);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setCatalog(String catalog) throws SQLException
  {
    nestedConnection.setCatalog(catalog);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setHoldability(int holdability) throws SQLException
  {
    nestedConnection.setHoldability(holdability);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException
  {
    nestedConnection.setReadOnly(readOnly);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Savepoint setSavepoint() throws SQLException
  {
    return nestedConnection.setSavepoint();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Savepoint setSavepoint(String name) throws SQLException
  {
    return nestedConnection.setSavepoint(name);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setTransactionIsolation(int level) throws SQLException
  {
    nestedConnection.setTransactionIsolation(level);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException
  {
    nestedConnection.setTypeMap(map);
  }

  /**
   * Gets the nested connection.
   *
   * @return the nested connection
   */
  public Connection getNestedConnection()
  {
    return nestedConnection;
  }

  /**
   * Sets the nested connection.
   *
   * @param nestedConnection the new nested connection
   */
  public void setNestedConnection(Connection nestedConnection)
  {
    this.nestedConnection = nestedConnection;
  }

  /**
   * Gets the wrapper factory.
   *
   * @return the wrapper factory
   */
  public SqlWrapperFactory getWrapperFactory()
  {
    return wrapperFactory;
  }

  /**
   * Sets the wrapper factory.
   *
   * @param wrapperFactory the new wrapper factory
   */
  public void setWrapperFactory(SqlWrapperFactory wrapperFactory)
  {
    this.wrapperFactory = wrapperFactory;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setSchema(String schema) throws SQLException
  {
    Jdbc17Utils.invoke(nestedConnection, SQLException.class, "setSchema", schema);

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getSchema() throws SQLException
  {
    return (String) Jdbc17Utils.invoke(nestedConnection, SQLException.class, "getSchema");
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void abort(Executor executor) throws SQLException
  {
    Jdbc17Utils.invoke(nestedConnection, SQLException.class, "abort", executor);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
  {
    Jdbc17Utils.invoke(nestedConnection, SQLException.class, "setNetworkTimeout", executor, milliseconds);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getNetworkTimeout() throws SQLException
  {
    return (Integer) Jdbc17Utils.invoke(nestedConnection, SQLException.class, "getNetworkTimeout");
  }
}
