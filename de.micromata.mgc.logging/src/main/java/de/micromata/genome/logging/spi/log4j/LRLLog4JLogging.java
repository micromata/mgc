package de.micromata.genome.logging.spi.log4j;

import java.sql.Timestamp;
import java.util.List;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.types.Pair;

/**
 * Loggs into secondaryLogging (by default Log4J) and keeps nth records in memory for viewing.
 * 
 * This implemention does not work for cluster
 * 
 * TODO synchronize
 * 
 * @author roger
 * 
 */
public class LRLLog4JLogging extends BaseLogging
{

  /**
   * The max queue length.
   */
  private int maxQueueLength = 2000;

  /**
   * The current syntetic pk.
   */
  private long currentSynteticPk = 0;

  /**
   * The queue.
   */
  private RoundList<LogEntry> queue = new RoundList<LogEntry>(maxQueueLength);

  /**
   * The secondary logging.
   */
  private Logging secondaryLogging = new Log4JLogging();

  /**
   * Convert.
   *
   * @param lwe the lwe
   * @return the log entry
   */
  protected LogEntry convert(LogWriteEntry lwe)
  {
    LogEntry le = new LogEntry();
    if (lwe.getTimestamp() != 0) {
      le.setTimestamp(lwe.getTimestamp());
    } else {
      le.setTimestamp(System.currentTimeMillis());
    }
    le.setAttributes(lwe.getAttributes());
    le.setCategory(lwe.getCategory());
    // le.setDocLogEntry(lwe.get)
    le.setLogLevel(lwe.getLevel());
    le.setMessage(lwe.getMessage());
    return le;
  }

  @Override
  public void doLogImpl(LogWriteEntry lwe)
  {

    LogEntry le = convert(lwe);
    le.setLogEntryIndex(++currentSynteticPk);
    queue.add(le);
    secondaryLogging.doLogImpl(lwe);
  }

  @Override
  protected void selectLogsImpl(List<Object> logId, boolean masterOnly, LogEntryCallback callback)
  {

    for (int i = queue.size() - 1; i >= 0; --i) {
      LogEntry re = queue.get(i);
      if (logId.contains(re.getLogEntryIndex()) == false) {
        continue;
      }
      LogEntry cp = new LogEntry(re);
      callback.onRow(cp);
    }

  }

  @Override
  protected void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback)
  {

    // TODO no sort, no filter currently
    int foundElems = 0;
    for (int i = queue.size() - 1; i >= 0; --i) {
      LogEntry re = queue.get(i);

      if (loglevel != null && loglevel > re.getLogLevel().getLevel()) {
        continue;
      }
      if (category != null && re.getCategory().startsWith(category) == false) {
        continue;
      }
      if (msg != null && re.getMessage().startsWith(msg) == false) {
        continue;
      }

      if (logAttributes != null) {
        boolean notFound = false;
        for (Pair<String, String> p : logAttributes) {
          LogAttribute la = re.getAttributeByType(p.getFirst());
          if (la == null) {
            notFound = true;
            break;
          }
          if (la.getValue().startsWith(p.getSecond()) == false) {
            notFound = true;
            break;
          }
        }
        if (notFound == true) {
          continue;
        }
      }
      ++foundElems;
      if (foundElems < startRow) {
        continue;
      }

      if (foundElems > maxRow + startRow) {
        break;
      }

      LogEntry le = new LogEntry(re);

      callback.onRow(le);

    }
  }

  @Override
  public boolean supportsSearch()
  {
    return true;
  }

  @Override
  public boolean supportsFulltextSearch()
  {
    return false;
  }

  @Override
  public String formatLogId(Object logId)
  {
    return ((Long) logId).toString();
  }

  @Override
  public Object parseLogId(String logId)
  {
    return Long.valueOf(logId);
  }

}
