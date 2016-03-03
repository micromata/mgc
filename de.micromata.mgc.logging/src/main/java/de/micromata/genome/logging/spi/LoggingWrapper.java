package de.micromata.genome.logging.spi;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.types.Pair;

/**
 * Wrapps another logging.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingWrapper implements Logging
{
  protected Logging target;

  public LoggingWrapper()
  {

  }

  public LoggingWrapper(Logging target)
  {
    this.target = target;
  }

  public Logging getTarget()
  {
    return target;
  }

  public void setTarget(Logging target)
  {
    this.target = target;
  }

  @Override
  public void debug(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.debug(cat, msg, attributes);
  }

  @Override
  public void trace(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.trace(cat, msg, attributes);
  }

  @Override
  public void info(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.info(cat, msg, attributes);
  }

  @Override
  public void note(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.note(cat, msg, attributes);
  }

  @Override
  public void warn(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.warn(cat, msg, attributes);
  }

  @Override
  public void error(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.error(cat, msg, attributes);
  }

  @Override
  public void fatal(LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.fatal(cat, msg, attributes);
  }

  @Override
  public void doLog(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.doLog(ll, cat, msg, attributes);
  }

  @Override
  public void doLogImpl(LogWriteEntry lwe)
  {
    target.doLogImpl(lwe);
  }

  @Override
  public void logPreStart(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes)
  {
    target.logPreStart(ll, cat, msg, attributes);
  }

  @Override
  public LogLevel getConfigMinLogLevel()
  {
    return target.getConfigMinLogLevel();
  }

  @Override
  public Collection<LogCategory> getRegisteredCategories()
  {
    return target.getRegisteredCategories();
  }

  @Override
  public Collection<LogAttributeType> getRegisteredAttributes()
  {
    return target.getRegisteredAttributes();
  }

  @Override
  public boolean supportsSearch()
  {
    return target.supportsSearch();
  }

  @Override
  public boolean supportsFulltextSearch()
  {
    return target.supportsFulltextSearch();
  }

  @Override
  public Collection<LogAttributeType> getSearchAttributes()
  {
    return target.getSearchAttributes();
  }

  @Override
  public void selectLogs(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback)
  {
    target.selectLogs(start, end, loglevel, category, msg, logAttributes, startRow, maxRow, orderBy, masterOnly,
        callback);
  }

  @Override
  public void selectLogs(List<Object> logId, boolean masterOnly, LogEntryCallback callback)
  {
    target.selectLogs(logId, masterOnly, callback);
  }

  @Override
  public String formatLogId(Object logId)
  {
    return target.formatLogId(logId);
  }

  @Override
  public Object parseLogId(String logId)
  {
    return target.parseLogId(logId);
  }

  @Override
  public void logLwe(LogWriteEntry lwe)
  {
    target.logLwe(lwe);
  }

}
