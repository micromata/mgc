package de.micromata.genome.logging;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * Overwrites de.micromata.genome.logging.CombinedLogging.doLogImpl() to primary and secondary.
 * 
 * All other methods delegates to primary.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class CombinedLogging implements Logging
{

  /**
   * The primary.
   */
  protected Logging primary;

  /**
   * The secondary.
   */
  protected Logging secondary;

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void doLogImpl(LogWriteEntry lwe)
  {
    primary.doLogImpl(lwe);
    secondary.doLogImpl(lwe);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void selectLogs(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback)
  {
    primary.selectLogs(start, end, loglevel, category, msg, logAttributes, startRow, maxRow, orderBy, masterOnly,
        callback);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void selectLogs(List<Object> logId, boolean masterOnly, LogEntryCallback callback)
  {
    primary.selectLogs(logId, masterOnly, callback);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void debug(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.debug(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void trace(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.trace(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void info(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.info(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void note(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.note(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void warn(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.warn(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void error(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.error(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void fatal(LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.fatal(cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void doLog(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.doLog(ll, cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void logPreStart(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes)
  {
    primary.logPreStart(ll, cat, msg, attributes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public LogLevel getConfigMinLogLevel()
  {
    return primary.getConfigMinLogLevel();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Collection<LogCategory> getRegisteredCategories()
  {
    return primary.getRegisteredCategories();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Collection<LogAttributeType> getRegisteredAttributes()
  {
    return primary.getRegisteredAttributes();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean supportsSearch()
  {
    return primary.supportsSearch();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean supportsFulltextSearch()
  {
    return primary.supportsFulltextSearch();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Collection<LogAttributeType> getSearchAttributes()
  {
    return primary.getSearchAttributes();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String formatLogId(Object logId)
  {
    return primary.formatLogId(logId);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Object parseLogId(String logId)
  {
    return primary.parseLogId(logId);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void logLwe(LogWriteEntry lwe)
  {
    primary.logLwe(lwe);
  }

  public Logging getPrimary()
  {
    return primary;
  }

  public void setPrimary(Logging primary)
  {
    this.primary = primary;
  }

  public Logging getSecondary()
  {
    return secondary;
  }

  public void setSecondary(Logging secondary)
  {
    this.secondary = secondary;
  }
}
