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

package de.micromata.genome.logging.spi.log4j;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.types.Pair;

/**
 * Logging implemented via.
 *
 * @author roger@micromata.de
 */
public class Log4JLogging extends BaseLogging
{
  public static final String LOG4J_DEFAULT_PREFIX = "de.micromata.genome.logging.";
  public static final String LOG4J_FALLBACK_PREFIX = "de.micromata.genome.fallback.";

  /**
   * Writing to log4j prefix to category.
   */
  private String log4jCategoryPrefix = LOG4J_DEFAULT_PREFIX;

  /**
   * The min log level.
   */
  private int minLogLevel = LogLevel.Debug.getLevel();

  /**
   * Instantiates a new log4 j logging.
   */
  public Log4JLogging()
  {

  }

  /**
   * Instantiates a new log4 j logging.
   *
   * @param logPrefix the log prefix
   */
  public Log4JLogging(String logPrefix)
  {
    this.log4jCategoryPrefix = logPrefix;
  }

  /**
   * Maps a Genome LogLevel to Log4J loglevel.
   *
   * @param ll the ll
   * @return the priority
   */
  public static Priority mapLoglevel(LogLevel ll)
  {
    switch (ll) {
      case Debug:
        return Level.DEBUG;
      case Trace:
        return Level.DEBUG;
      case Info:
        return Level.INFO;
      case Note:
        return Level.INFO;
      case Warn:
        return Level.WARN;
      case Error:
        return Level.ERROR;
      case Fatal:
        return Level.FATAL;
      default:
        return Level.INFO;
    }
  }

  /**
   * Map log level to level.
   *
   * @param ll the ll
   * @return the level
   */
  public static Level mapLogLevelToLevel(LogLevel ll)
  {
    switch (ll) {
      case Debug:
        return Level.TRACE;
      case Trace:
        return Level.DEBUG;
      case Info:
        return Level.INFO;
      case Note:
        return Level.INFO;
      case Warn:
        return Level.WARN;
      case Error:
        return Level.ERROR;
      case Fatal:
        return Level.FATAL;
      default:
        return Level.INFO;
    }
  }

  /**
   * Map level to log level.
   *
   * @param ll the ll
   * @return the log level
   */
  public static LogLevel mapLevelToLogLevel(Level ll)
  {
    switch (ll.toInt()) {
      case Level.TRACE_INT:
        return LogLevel.Debug;
      case Priority.DEBUG_INT:
        return LogLevel.Trace;
      case Priority.INFO_INT:
        return LogLevel.Info;

      case Priority.WARN_INT:
        return LogLevel.Warn;
      case Priority.ERROR_INT:
        return LogLevel.Error;
      case Priority.FATAL_INT:
        return LogLevel.Fatal;
      default:
        return LogLevel.Info;
    }
  }

  @Override
  public void doLogImpl(final LogWriteEntry lwe)
  {
    if (lwe.getLevel().getLevel() < minLogLevel) {
      return;
    }
    Logger log = Logger.getLogger(log4jCategoryPrefix + lwe.getCategory());
    Priority p = mapLoglevel(lwe.getLevel());
    if (log.isEnabledFor(p) == false) {
      return;
    }

    StringBuilder sb = new StringBuilder();
    for (LogAttribute a : lwe.getAttributes()) {
      if (a == null) {
        continue;
      }

      if (sb.length() > 0) {
        sb.append("|");
      }

      sb.append(a.getType().toString()).append("; value=").append(a.getValueToWrite(lwe));
    }
    String message = lwe.getMessage();
    if (sb.length() > 0) {
      message += "| " + sb.toString();
    }
    try {
      log.log(p, message);
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      try {
        log.log(Level.FATAL, "Log4J throws Exception: " + ex.getMessage());
      } catch (Throwable ex2) { // NOSONAR "Illegal Catch" framework
        // nix
      }
    }
  }

  @Override
  protected void selectLogsImpl(List<Object> logId, boolean masterOnly, LogEntryCallback callback)
  {
    LogEntry le = new LogEntry();
    le.setMessage("Select Logs not supported by Log4JLogging");
    le.setCategory("Category1");
    le.setLogLevel(LogLevel.Note);
    le.setLogEntryIndex(Long.valueOf(55));
    le.setTimestamp(System.currentTimeMillis());
    callback.onRow(le);
  }

  @Override
  protected void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback)
  {
    // TODO lado mit roger absprechen um die Datei vlt so zu parsen
    Enumeration appenders = Logger.getLogger(log4jCategoryPrefix).getRootLogger().getAllAppenders();
    while (appenders.hasMoreElements()) {
      Object o = appenders.nextElement();
      if (o instanceof FileAppender == false) {
        continue;
      }
      FileAppender fa = (FileAppender) o;
      System.err.println("Die Datei sollte geparsed werden " + fa.getFile());
    }

    // TEST MS 2 dummy LogEntry
    LogEntry le = new LogEntry();
    le.setMessage("Select Logs not supported by Log4JLogging");
    le.setCategory("Category1");
    le.setLogLevel(LogLevel.Note);
    le.setLogEntryIndex(Long.valueOf(55));
    le.setTimestamp(System.currentTimeMillis());
    if ((category == null || category.equals(le.getCategory()))
        && (loglevel == null || loglevel.equals(le.getLogLevel().getLevel()))) {
      callback.onRow(le);
    }

    le = new LogEntry();
    le.setMessage("Select ist nicht von Log4JLogging unterstützt");
    le.setCategory("Category2");
    le.setLogLevel(LogLevel.Trace);
    le.setLogEntryIndex(Long.valueOf(44));
    le.setTimestamp(System.currentTimeMillis());
    if ((category == null || category.equals(le.getCategory()))
        && (loglevel == null || loglevel.equals(le.getLogLevel().getLevel()))) {
      callback.onRow(le);
    }
  }

  @Override
  public LogLevel getConfigMinLogLevel()
  {
    return LogLevel.Debug;
  }

  @Override
  public boolean supportsSearch()
  {
    return false;
  }

  @Override
  public boolean supportsFulltextSearch()
  {
    return false;
  }

  public String getLog4jCategoryPrefix()
  {
    return log4jCategoryPrefix;
  }

  public void setLog4jCategoryPrefix(String log4jCategoryPrefix)
  {
    this.log4jCategoryPrefix = log4jCategoryPrefix;
  }

  /**
   * Checks if is log enabled.
   *
   * @param level the level
   * @param cat the cat
   * @return true, if is log enabled
   */
  public boolean isLogEnabled(LogLevel level, LogCategory cat)
  {
    // TODO rrk implement
    return false;
  }

  @Override
  public String formatLogId(Object logId)
  {
    return "";
  }

  @Override
  public Object parseLogId(String logId)
  {
    return "";
  }

  public int getMinLogLevel()
  {
    return minLogLevel;
  }

  public void setMinLogLevel(int minLogLevel)
  {
    this.minLogLevel = minLogLevel;
  }
}
