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

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogStacktraceAttribute;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingContext;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.CallableX;
import de.micromata.genome.util.types.Pair;

/**
 * The Class StdSqlTracer.
 *
 * @author roger
 */
public class StdSqlTracer implements SqlTracer
{

  /**
   * Do log.
   *
   * @param prefix the prefix
   * @param cat the cat
   * @param sqlArguments the sql arguments
   * @param connection the connection
   * @param traceConfig the trace config
   */
  public void doLog(String prefix, LogCategory cat, TraceSqlArguments sqlArguments, TraceConnection connection,
      TraceConfig traceConfig)
  {
    // String realSql = sqlArguments.getSql();

    LogLevel logLevel = traceConfig.getLogLevel();
    doLog(prefix, logLevel, cat, sqlArguments, connection, traceConfig);
  }

  /**
   * Do log.
   *
   * @param prefix the prefix
   * @param logLevel the log level
   * @param cat the cat
   * @param sqlArguments the sql arguments
   * @param connection the connection
   * @param traceConfig the trace config
   */
  public void doLog(String prefix, LogLevel logLevel, LogCategory cat, TraceSqlArguments sqlArguments,
      TraceConnection connection,
      TraceConfig traceConfig)
  {
    String sql = sqlArguments.getSql();
    int attrSize = 0;
    if (traceConfig.isLogArguments() == true) {
      ++attrSize;
    }
    if (traceConfig.isLogPreparedStatement() == true) {
      ++attrSize;
    }
    if (traceConfig.isLogSqlLiteralStatement() == true) {
      ++attrSize;
    }
    if (traceConfig.isLogStacktraces() == true) {
      ++attrSize;
    }

    LogAttribute[] attributes = new LogAttribute[attrSize];
    int i = 0;
    if (traceConfig.isLogArguments() == true) {
      attributes[i++] = new LogAttribute(GenomeAttributeType.SqlStatement, sqlArguments.getSql());
    }
    if (traceConfig.isLogPreparedStatement() == true) {
      attributes[i++] = new JdbcSqlArgsAttribute(sqlArguments.getArgs());
    }
    if (traceConfig.isLogSqlLiteralStatement() == true) {
      attributes[i++] = new LogSqlLiteralAttribute(sqlArguments.getSql(), sqlArguments.getArgs(),
          traceConfig.getSqlArgRenderer());
    }
    if (traceConfig.isLogStacktraces() == true) {
      attributes[i++] = new LogStacktraceAttribute("de.micromata.genome.db.jdbc.trace");
    }
    Logging logging = traceConfig.getLogging();
    if (logging == null) {
      logging = LoggingServiceManager.get().getLogging();
    }
    logging.doLog(logLevel, cat, prefix + sql, attributes);
  }

  /**
   * Adds the to log.
   *
   * @param cat the cat
   * @param sqlArguments the sql arguments
   * @param connection the connection
   * @param sqlTraced the sql traced
   * @param traceConfig the trace config
   */
  public void addToLog(LogCategory cat, TraceSqlArguments sqlArguments, TraceConnection connection, SqlTraced sqlTraced,
      TraceConfig traceConfig)
  {
    if (traceConfig.isLogAtCommit() == true && connection.isTraceAutocommit() == false) {
      connection.getSqlCommands().addTrace(sqlArguments);
      return;
    }
    doLog("", cat, sqlArguments, connection, traceConfig);
  }

  @Override
  public void rollback(TraceConnection connection, Savepoint savepoint)
  {
    TraceConfig traceConfig = connection.getTraceConfig();
    if (traceConfig.isLogRolledBack() == true) {
      logRolledBack(connection, savepoint);
    }
    connection.getSqlCommands().clearSavepoint(savepoint);
  }

  /**
   * Log commited.
   *
   * @param connection the connection
   */
  public void logCommited(TraceConnection connection)
  {
    TraceConfig traceConfig = connection.getTraceConfig();
    LogCategory logCat = traceConfig.getLogCategory();
    SqlCommands cmds = connection.getSqlCommands();

    for (Pair<Savepoint, List<TraceSqlArguments>> p : cmds.getUncommitedCommands()) {
      for (TraceSqlArguments tsa : p.getSecond()) {
        doLog("commit; ", logCat, tsa, connection, traceConfig);
      }
    }
  }

  /**
   * Log rolled back.
   *
   * @param connection the connection
   * @param sp the sp
   */
  public void logRolledBack(TraceConnection connection, Savepoint sp)
  {
    TraceConfig traceConfig = connection.getTraceConfig();
    LogCategory logCat = traceConfig.getLogCategory();
    SqlCommands cmds = connection.getSqlCommands();
    int until = 0;
    if (sp != null) {
      until = cmds.findSavepointIndex(sp);
    }
    for (int i = cmds.getUncommitedCommands().size() - 1; i >= until; --i) {
      for (TraceSqlArguments tsa : cmds.getUncommitedCommands().get(i).getSecond()) {
        doLog("rollback; ", logCat, tsa, connection, traceConfig);
      }
    }
  }

  @Override
  public void commit(TraceConnection connection) throws SQLException
  {
    TraceConfig traceConfig = connection.getTraceConfig();
    long start = System.currentTimeMillis();
    try {
      connection.getNestedConnection().commit();
    } finally {
      long end = System.currentTimeMillis();
      LoggingServiceManager.get().getStatsDAO().addPerformance(traceConfig.getLogCategory(), "commit", end - start, 0);
    }
    if (traceConfig.isLogAtCommit() == true) {
      logCommited(connection);
    }

    connection.getSqlCommands().clear();
  }

  /**
   * Checks if is filtered for logging.
   *
   * @param traceConfig the trace config
   * @param sql the sql
   * @return true, if is filtered for logging
   */
  protected boolean isFilteredForLogging(TraceConfig traceConfig, String sql)
  {
    if (sql == null) {
      return false;
    }
    return traceConfig.getLogFilterMatcher().match(sql);
  }

  @Override
  public <T> T executeWrapped(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable) throws SQLException
  {
    if (sql == null) {
      sql = sqlTraced.getSqlArguments().getSql();
    }
    LoggingContext.setCurrentSql(sql, sqlTraced.getSqlArguments().getArgs());
    T t = executeWrappedInt(sqlTraced, sql, callable);
    LoggingContext.setCurrentSql(null, null);
    return t;
  }

  /**
   * Execute wrapped int.
   *
   * @param <T> the generic type
   * @param sqlTraced the sql traced
   * @param sql the sql
   * @param callable the callable
   * @return the t
   * @throws SQLException the SQL exception
   */
  public <T> T executeWrappedInt(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable)
      throws SQLException
  {
    TraceConnection connection = sqlTraced.getTraceConnection();
    TraceConfig traceConfig = connection.getTraceConfig();
    boolean statsEnabled = traceConfig.isEnableStats();
    boolean logEnabled = traceConfig.isEnableLogging();

    if (logEnabled == false && statsEnabled == false) {
      return callable.call();
    }

    boolean inRec = connection.isInRecursion();
    if (statsEnabled == false && inRec == true) {
      return callable.call();
    }

    TraceSqlArguments sqlArguments = sqlTraced.getSqlArguments();
    String realSql = sqlArguments.getSql();
    LogCategory cat = traceConfig.getLogCategory();
    if (sql != null) {
      sqlArguments.setSql(sql);
    }
    if (traceConfig.isTrimPreparedStatement() == true) {
      sqlArguments.setSql(StringUtils.trim(sqlArguments.getSql()));
    }
    if (logEnabled == true) {
      logEnabled = isFilteredForLogging(traceConfig, sqlArguments.getSql());
    }
    // System.out.println("sqltrace: " + sqlArguments.toString());
    if (logEnabled == false && statsEnabled == false) {
      return callable.call();
    }

    // nur Stats
    if (inRec == true || logEnabled == false) {
      long start = System.currentTimeMillis();
      try {
        return callable.call();
      } finally {
        long end = System.currentTimeMillis();
        LoggingServiceManager.get().getStatsDAO().addPerformance(traceConfig.getLogCategory(), realSql, end - start, 0);
      }
    }
    // Logging + optional Stats
    try {
      connection.lockRecursion();

      if (logEnabled == true) {
        boolean loggingLogEnabled = GLog.isDebugEnabled(cat, realSql);
        if (loggingLogEnabled == true) {
          addToLog(cat, sqlArguments, connection, sqlTraced, traceConfig);
        }
      }
      long start = System.currentTimeMillis();
      try {
        return callable.call();
      } finally {
        if (statsEnabled == true) {
          long end = System.currentTimeMillis();
          LoggingServiceManager.get().getStatsDAO().addPerformance(cat, realSql, end - start, 0);
        }
      }
    } finally {
      connection.releaseRecursion();
    }
  }

  @Override
  public <T> T executePreparedWrapped(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable)
      throws SQLException
  {
    if (sql == null) {
      sql = sqlTraced.getSqlArguments().getSql();
    }
    LoggingContext.setCurrentSql(sql, sqlTraced.getSqlArguments().getArgs());

    T ret = callable.call();
    LoggingContext.setCurrentSql(null, null);
    return ret;
  }
}
