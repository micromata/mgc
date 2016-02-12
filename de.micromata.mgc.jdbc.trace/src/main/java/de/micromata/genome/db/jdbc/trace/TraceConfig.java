package de.micromata.genome.db.jdbc.trace;

import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.matcher.BooleanListRulesFactory;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;

/**
 * Configuration.
 * 
 * If you instantiate not via Spring call manually afterPropertiesSet() after setting all properties
 * 
 * @author roger
 * 
 */
public class TraceConfig
{
  /**
   * If false do not log. all log-Parameters are irrelevant
   */
  private boolean enableLogging = true;

  /**
   * If false do not log. all stats-Parameters are irrelevant
   */
  private boolean enableStats = true;

  /**
   * if true also log stacktrace elements
   */
  private boolean logStacktraces = false;

  /**
   * Logg the prepared statement with Type GenomeAttributeType.SqlStatement
   */
  private boolean logPreparedStatement = true;

  /**
   * before logging or stats tracing trim the sql
   */
  private boolean trimPreparedStatement = false;

  /**
   * Logg the prepared statement in Category GenomeAttributeType.SqlArgs
   */
  private boolean logArguments = true;

  /**
   * Logg the resolved statement as literal into GenomeAttributeType.SqlResolvedStatement
   * 
   * This will replace all ? with corresponding sql argument literal
   */
  private boolean logSqlLiteralStatement = false;

  /**
   * if true do log operation only if commit are successfull.
   * 
   * If the Connection is autocommit this will be ignored
   */
  private boolean logAtCommit = false;

  /**
   * if the connection rollbacked log the statements, which are rolled back.
   * 
   * If the Connection is autocommit this will be ignored
   */
  private boolean logRolledBack = false;

  private SqlArgRenderer sqlArgRenderer = new SqlLiteralArgRenderer();

  private SqlTracer sqlTracer = new StdSqlTracer();

  /**
   * Logging and Stats
   */
  public LogCategory logCategory = GenomeLogCategory.Database;

  public LogLevel logLevel = LogLevel.Debug;

  /**
   * Where to log. If null, logs to standard Logging
   */
  private Logging logging = null;

  private String logFilterRuleString = "+*";

  private Matcher<String> logFilterMatcher;

  private MatcherFactory<String> logFilterMatcherFactory = new BooleanListRulesFactory<String>();

  public TraceConfig()
  {

  }

  public void afterPropertiesSet() throws Exception
  {
    this.logFilterMatcher = logFilterMatcherFactory.createMatcher(logFilterRuleString);
  }

  public boolean isEnableLogging()
  {
    return enableLogging;
  }

  public void setEnableLogging(boolean enableLogging)
  {
    this.enableLogging = enableLogging;
  }

  public boolean isEnableStats()
  {
    return enableStats;
  }

  public void setEnableStats(boolean enableStats)
  {
    this.enableStats = enableStats;
  }

  public boolean isLogStacktraces()
  {
    return logStacktraces;
  }

  public void setLogStacktraces(boolean logStacktraces)
  {
    this.logStacktraces = logStacktraces;
  }

  public boolean isLogPreparedStatement()
  {
    return logPreparedStatement;
  }

  public void setLogPreparedStatement(boolean logPreparedStatement)
  {
    this.logPreparedStatement = logPreparedStatement;
  }

  public boolean isTrimPreparedStatement()
  {
    return trimPreparedStatement;
  }

  public void setTrimPreparedStatement(boolean trimPreparedStatement)
  {
    this.trimPreparedStatement = trimPreparedStatement;
  }

  public boolean isLogArguments()
  {
    return logArguments;
  }

  public void setLogArguments(boolean logArguments)
  {
    this.logArguments = logArguments;
  }

  public boolean isLogSqlLiteralStatement()
  {
    return logSqlLiteralStatement;
  }

  public void setLogSqlLiteralStatement(boolean logSqlLiteralStatement)
  {
    this.logSqlLiteralStatement = logSqlLiteralStatement;
  }

  public boolean isLogAtCommit()
  {
    return logAtCommit;
  }

  public void setLogAtCommit(boolean logAtCommit)
  {
    this.logAtCommit = logAtCommit;
  }

  public boolean isLogRolledBack()
  {
    return logRolledBack;
  }

  public void setLogRolledBack(boolean logRolledBack)
  {
    this.logRolledBack = logRolledBack;
  }

  public Logging getLogging()
  {
    return logging;
  }

  public void setLogging(Logging logging)
  {
    this.logging = logging;
  }

  public SqlArgRenderer getSqlArgRenderer()
  {
    return sqlArgRenderer;
  }

  public void setSqlArgRenderer(SqlArgRenderer sqlArgRenderer)
  {
    this.sqlArgRenderer = sqlArgRenderer;
  }

  public LogCategory getLogCategory()
  {
    return logCategory;
  }

  public void setLogCategory(LogCategory logCategory)
  {
    this.logCategory = logCategory;
  }

  public SqlTracer getSqlTracer()
  {
    return sqlTracer;
  }

  public void setSqlTracer(SqlTracer sqlTracer)
  {
    this.sqlTracer = sqlTracer;
  }

  public LogLevel getLogLevel()
  {
    return logLevel;
  }

  public void setLogLevel(LogLevel logLevel)
  {
    this.logLevel = logLevel;
  }

  public String getLogFilterRuleString()
  {
    return logFilterRuleString;
  }

  public void setLogFilterRuleString(String logFilterRuleString)
  {
    this.logFilterRuleString = logFilterRuleString;
  }

  public Matcher<String> getLogFilterMatcher()
  {
    if (logFilterMatcher == null) {
      logFilterMatcher = logFilterMatcherFactory.createMatcher(logFilterRuleString);
    }
    return logFilterMatcher;
  }

  public void setLogFilterMatcher(Matcher<String> logFilterMatcher)
  {
    this.logFilterMatcher = logFilterMatcher;
  }

  public MatcherFactory<String> getLogFilterMatcherFactory()
  {
    return logFilterMatcherFactory;
  }

  public void setLogFilterMatcherFactory(MatcherFactory<String> logFilterMatcherFactory)
  {
    this.logFilterMatcherFactory = logFilterMatcherFactory;
  }

}
