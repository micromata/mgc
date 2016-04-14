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

package de.micromata.genome.db.jdbc.trace;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import de.micromata.genome.db.jdbc.wrapper.ConnectionWrapper;
import de.micromata.genome.util.runtime.CallableX;

/**
 * The Class TraceConnection.
 *
 * @author roger
 */
public class TraceConnection extends ConnectionWrapper implements JdbcTraced, SqlTraced
{

  /**
   * The data source wrapper.
   */
  protected TraceDataSource dataSourceWrapper;

  /**
   * The user name.
   */
  private String userName;

  /**
   * The trace autocommit.
   */
  private boolean traceAutocommit = true;

  /**
   * The sql commands.
   */
  private SqlCommands sqlCommands = new SqlCommands();

  /**
   * The sql arguments.
   */
  private TraceSqlArguments sqlArguments = new TraceSqlArguments();

  /**
   * Instantiates a new trace connection.
   */
  public TraceConnection()
  {
    super();
  }

  /**
   * The recursion guard.
   */
  protected static ThreadLocal<Boolean> recursionGuard = new ThreadLocal<Boolean>()
  {

    @Override
    protected Boolean initialValue()
    {
      return Boolean.FALSE;
    }
  };

  /**
   * Instantiates a new trace connection.
   *
   * @param dataSourceWrapper the data source wrapper
   * @param userName the user name
   * @param nestedConnection the nested connection
   */
  public TraceConnection(TraceDataSource dataSourceWrapper, String userName, Connection nestedConnection)
  {
    super(nestedConnection, dataSourceWrapper.getWrapperFactory());
    this.dataSourceWrapper = dataSourceWrapper;
    this.userName = userName;
    try {
      this.traceAutocommit = nestedConnection.getAutoCommit();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public TraceConfig getTraceConfig()
  {
    return dataSourceWrapper.getTraceConfig();
  }

  @Override
  public boolean isInRecursion()
  {
    return recursionGuard.get() == Boolean.TRUE;
  }

  @Override
  public void lockRecursion()
  {
    recursionGuard.set(Boolean.TRUE);
  }

  @Override
  public void releaseRecursion()
  {
    recursionGuard.set(Boolean.FALSE);
  }

  @Override
  public TraceSqlArguments getSqlArguments()
  {
    return sqlArguments;
  }

  @Override
  public TraceConnection getTraceConnection()
  {
    return this;
  }

  /**
   * Execute prepared wrapped.
   *
   * @param <T> the generic type
   * @param sql the sql
   * @param callable the callable
   * @return the t
   * @throws SQLException the SQL exception
   */
  protected <T> T executePreparedWrapped(String sql, CallableX<T, SQLException> callable) throws SQLException
  {
    SqlTracer tracer = getDataSourceWrapper().getTraceConfig().getSqlTracer();
    return tracer.executePreparedWrapped(this, sql, callable);
  }

  @Override
  public PreparedStatement prepareStatement(final String sql) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<PreparedStatement, SQLException>()
    {

      @Override
      public PreparedStatement call() throws SQLException
      {
        return TraceConnection.super.prepareStatement(sql);
      }
    });
  }

  @Override
  public CallableStatement prepareCall(final String sql) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<CallableStatement, SQLException>()
    {

      @Override
      public CallableStatement call() throws SQLException
      {
        return TraceConnection.super.prepareCall(sql);
      }
    });

  }

  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency)
      throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<CallableStatement, SQLException>()
    {

      @Override
      public CallableStatement call() throws SQLException
      {
        return TraceConnection.super.prepareCall(sql, resultSetType, resultSetConcurrency);
      }
    });
  }

  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
      final int resultSetHoldability) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<CallableStatement, SQLException>()
    {

      @Override
      public CallableStatement call() throws SQLException
      {
        return TraceConnection.super.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      }
    });

  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<PreparedStatement, SQLException>()
    {

      @Override
      public PreparedStatement call() throws SQLException
      {
        return TraceConnection.super.prepareStatement(sql, autoGeneratedKeys);
      }
    });
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<PreparedStatement, SQLException>()
    {

      @Override
      public PreparedStatement call() throws SQLException
      {
        return TraceConnection.super.prepareStatement(sql, columnIndexes);
      }
    });

  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<PreparedStatement, SQLException>()
    {

      @Override
      public PreparedStatement call() throws SQLException
      {
        return TraceConnection.super.prepareStatement(sql, columnNames);
      }
    });
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency)
      throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<PreparedStatement, SQLException>()
    {

      @Override
      public PreparedStatement call() throws SQLException
      {
        return TraceConnection.super.prepareStatement(sql, resultSetType, resultSetConcurrency);
      }
    });

  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
      final int resultSetHoldability) throws SQLException
  {
    return executePreparedWrapped(sql, new CallableX<PreparedStatement, SQLException>()
    {

      @Override
      public PreparedStatement call() throws SQLException
      {
        return TraceConnection.super.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      }
    });
  }

  @Override
  public void commit() throws SQLException
  {
    getTraceConfig().getSqlTracer().commit(this);
  }

  @Override
  public boolean getAutoCommit() throws SQLException
  {
    return traceAutocommit = super.getAutoCommit();
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException
  {
    traceAutocommit = autoCommit;
    super.setAutoCommit(autoCommit);
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException
  {
    super.releaseSavepoint(savepoint);
  }

  @Override
  public void rollback() throws SQLException
  {
    super.rollback();
    getTraceConfig().getSqlTracer().rollback(this, null);
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException
  {
    getTraceConfig().getSqlTracer().rollback(this, savepoint);
    super.rollback(savepoint);

  }

  @Override
  public Savepoint setSavepoint() throws SQLException
  {
    Savepoint savePoint = super.setSavepoint();
    sqlCommands.pushSavePoint(savePoint);
    return savePoint;
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException
  {
    Savepoint savePoint = super.setSavepoint(name);
    sqlCommands.pushSavePoint(savePoint);
    return savePoint;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public TraceDataSource getDataSourceWrapper()
  {
    return dataSourceWrapper;
  }

  public void setDataSourceWrapper(TraceDataSource dataSourceWrapper)
  {
    this.dataSourceWrapper = dataSourceWrapper;
  }

  public boolean isTraceAutocommit()
  {
    return traceAutocommit;
  }

  public void setTraceAutocommit(boolean autocommit)
  {
    this.traceAutocommit = autocommit;
  }

  public SqlCommands getSqlCommands()
  {
    return sqlCommands;
  }

  public void setSqlCommands(SqlCommands sqlCommands)
  {
    this.sqlCommands = sqlCommands;
  }

}
