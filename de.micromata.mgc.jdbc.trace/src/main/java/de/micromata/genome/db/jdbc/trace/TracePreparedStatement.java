package de.micromata.genome.db.jdbc.trace;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import de.micromata.genome.db.jdbc.wrapper.PreparedStatementWrapper;
import de.micromata.genome.util.runtime.CallableX;

/**
 * The Class TracePreparedStatement.
 *
 * @author roger
 */
public class TracePreparedStatement extends PreparedStatementWrapper implements SqlTraced
{

  /**
   * The trace connection.
   */
  private TraceConnection traceConnection;

  /**
   * The sql arguments.
   */
  private TraceSqlArguments sqlArguments = new TraceSqlArguments();

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
   * Instantiates a new trace prepared statement.
   */
  public TracePreparedStatement()
  {

  }

  /**
   * Instantiates a new trace prepared statement.
   *
   * @param traceConnection the trace connection
   * @param sql the sql
   * @param nestedPreparedStatement the nested prepared statement
   */
  public TracePreparedStatement(TraceConnection traceConnection, String sql, PreparedStatement nestedPreparedStatement)
  {
    super(nestedPreparedStatement);
    this.traceConnection = traceConnection;
    this.sqlArguments.setSql(sql);
  }

  @Override
  public TraceSqlArguments getSqlArguments()
  {
    return sqlArguments;
  }

  @Override
  public TraceConnection getTraceConnection()
  {
    return traceConnection;
  }

  public void setTraceConnection(TraceConnection traceConnection)
  {
    this.traceConnection = traceConnection;
  }

  /**
   * Execute wrapped.
   *
   * @param <T> the generic type
   * @param sql the sql
   * @param callable the callable
   * @return the t
   * @throws SQLException the SQL exception
   */
  protected <T> T executeWrapped(String sql, CallableX<T, SQLException> callable) throws SQLException
  {
    SqlTracer tracer = traceConnection.getDataSourceWrapper().getTraceConfig().getSqlTracer();
    return tracer.executeWrapped(this, sql, callable);
  }

  @Override
  public boolean execute(final String sql) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Boolean, SQLException>()
    {
      @Override
      public Boolean call() throws SQLException
      {
        return TracePreparedStatement.super.execute(sql);
      }
    });
  }

  @Override
  public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Boolean, SQLException>()
    {
      @Override
      public Boolean call() throws SQLException
      {
        return TracePreparedStatement.super.execute(sql, autoGeneratedKeys);
      }
    });
  }

  @Override
  public boolean execute(final String sql, final int[] columnIndexes) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Boolean, SQLException>()
    {
      @Override
      public Boolean call() throws SQLException
      {
        return TracePreparedStatement.super.execute(sql, columnIndexes);
      }
    });
  }

  @Override
  public boolean execute(final String sql, final String[] columnNames) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Boolean, SQLException>()
    {
      @Override
      public Boolean call() throws SQLException
      {
        return TracePreparedStatement.super.execute(sql, columnNames);
      }
    });
  }

  @Override
  public int[] executeBatch() throws SQLException
  {
    return executeWrapped(null, new CallableX<int[], SQLException>()
    {
      @Override
      public int[] call() throws SQLException
      {
        return TracePreparedStatement.super.executeBatch();
      }
    });
  }

  @Override
  public ResultSet executeQuery(final String sql) throws SQLException
  {
    return executeWrapped(sql, new CallableX<ResultSet, SQLException>()
    {
      @Override
      public ResultSet call() throws SQLException
      {
        return TracePreparedStatement.super.executeQuery(sql);
      }
    });
  }

  @Override
  public int executeUpdate(final String sql) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Integer, SQLException>()
    {
      @Override
      public Integer call() throws SQLException
      {
        return TracePreparedStatement.super.executeUpdate(sql);
      }
    });

  }

  @Override
  public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Integer, SQLException>()
    {
      @Override
      public Integer call() throws SQLException
      {
        return TracePreparedStatement.super.executeUpdate(sql, autoGeneratedKeys);
      }
    });
  }

  @Override
  public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Integer, SQLException>()
    {
      @Override
      public Integer call() throws SQLException
      {
        return TracePreparedStatement.super.executeUpdate(sql, columnIndexes);
      }
    });

  }

  @Override
  public int executeUpdate(final String sql, final String[] columnNames) throws SQLException
  {
    return executeWrapped(sql, new CallableX<Integer, SQLException>()
    {
      @Override
      public Integer call() throws SQLException
      {
        return TracePreparedStatement.super.executeUpdate(sql, columnNames);
      }
    });
  }

  @Override
  public boolean execute() throws SQLException
  {
    return executeWrapped(null, new CallableX<Boolean, SQLException>()
    {
      @Override
      public Boolean call() throws SQLException
      {
        return TracePreparedStatement.super.execute();
      }
    });
  }

  @Override
  public ResultSet executeQuery() throws SQLException
  {
    return executeWrapped(null, new CallableX<ResultSet, SQLException>()
    {
      @Override
      public ResultSet call() throws SQLException
      {
        return TracePreparedStatement.super.executeQuery();
      }
    });
  }

  @Override
  public int executeUpdate() throws SQLException
  {
    return executeWrapped(null, new CallableX<Integer, SQLException>()
    {
      @Override
      public Integer call() throws SQLException
      {
        return TracePreparedStatement.super.executeUpdate();
      }
    });
  }

  @Override
  public void addBatch() throws SQLException
  {
    super.addBatch();
  }

  @Override
  public void clearParameters() throws SQLException
  {
    sqlArguments.clearArgs();
    super.clearParameters();
  }

  @Override
  public void setArray(int i, Array x) throws SQLException
  {
    sqlArguments.put(i, x);
    super.setArray(i, x);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeAscii(x, length));
    super.setAsciiStream(parameterIndex, x, length);
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setBigDecimal(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeBinary(x, length));
    super.setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void setBlob(int i, Blob x) throws SQLException
  {
    sqlArguments.put(i, x);
    super.setBlob(i, x);
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setBoolean(parameterIndex, x);
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setByte(parameterIndex, x);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setBytes(parameterIndex, x);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeCharacterStream(reader, length));
    super.setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setClob(int i, Clob x) throws SQLException
  {
    sqlArguments.put(i, x);
    super.setClob(i, x);
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setDate(parameterIndex, x, cal);
  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setDate(parameterIndex, x);
  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setDouble(parameterIndex, x);
  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setFloat(parameterIndex, x);
  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setInt(parameterIndex, x);
  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setLong(parameterIndex, x);
  }

  @Override
  public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException
  {
    sqlArguments.put(paramIndex, SqlTypeWrapper.makeNull(sqlType, typeName));
    super.setNull(paramIndex, sqlType, typeName);
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeNull(sqlType, null));
    super.setNull(parameterIndex, sqlType);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeObject(x, targetSqlType, scale));
    super.setObject(parameterIndex, x, targetSqlType, scale);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeObject(x, targetSqlType, 0));
    super.setObject(parameterIndex, x, targetSqlType);
  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException
  {
    sqlArguments.put(parameterIndex, SqlTypeWrapper.makeObject(x, 0, 0));
    super.setObject(parameterIndex, x);
  }

  @Override
  public void setRef(int i, Ref x) throws SQLException
  {
    sqlArguments.put(i, x);
    super.setRef(i, x);
  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setShort(parameterIndex, x);
  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setString(parameterIndex, x);
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setTime(parameterIndex, x, cal);
  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setTime(parameterIndex, x);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setTimestamp(parameterIndex, x, cal);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setTimestamp(parameterIndex, x);
  }

  @Override
  @Deprecated
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
  {
    super.setUnicodeStream(parameterIndex, x, length);
  }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException
  {
    sqlArguments.put(parameterIndex, x);
    super.setURL(parameterIndex, x);
  }

  @Override
  public void addBatch(String sql) throws SQLException
  {
    super.addBatch(sql);
  }

  @Override
  public void cancel() throws SQLException
  {
    // TODO Auto-generated method stub
    super.cancel();
  }

  @Override
  public void clearBatch() throws SQLException
  {
    // TODO Auto-generated method stub
    super.clearBatch();
  }

}
