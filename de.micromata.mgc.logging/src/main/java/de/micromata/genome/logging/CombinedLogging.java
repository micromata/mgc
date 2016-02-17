package de.micromata.genome.logging;

import java.sql.Timestamp;
import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * Overwrites de.micromata.genome.logging.CombinedLogging.doLogImpl() to primary and secondary.
 * 
 * Search methods delegates to primary.
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class CombinedLogging extends BaseLogging
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

  @Override
  public boolean supportsSearch()
  {
    return primary.supportsSearch();
  }

  @Override
  public boolean supportsFulltextSearch()
  {
    return primary.supportsFulltextSearch();
  }

  @Override
  public String formatLogId(Object logId)
  {
    return primary.formatLogId(logId);
  }

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
  public void selectLogs(List<Object> logId, boolean masterOnly, LogEntryCallback callback)
  {
    primary.selectLogs(logId, masterOnly, callback);
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

  @Override
  protected void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback) throws EndOfSearch
  {
    // TODO Auto-generated method stub

  }

  @Override
  protected void selectLogsImpl(List<Object> logId, boolean masterOnly, LogEntryCallback callback) throws EndOfSearch
  {
    // TODO Auto-generated method stub

  }
}
