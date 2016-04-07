package de.micromata.genome.logging;

import java.lang.ref.SoftReference;
import java.util.Date;

import de.micromata.genome.logging.spi.log4j.Log4JLogging;
import de.micromata.genome.logging.spi.log4j.RoundList;
import de.micromata.genome.util.runtime.HostUtils;
import de.micromata.genome.util.types.Converter;
import de.micromata.genome.util.types.TimeInMillis;

/**
 * Implements Fallbacklogging.
 * 
 * This wrapps doLogImpl(), and if primary throws an exception, passed to secondary.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class FallbackLogging extends BaseLogging implements LoggingWithFallback
{

  /**
   * When fast failure occours.
   */
  protected volatile Date lastLogFailure = null;

  /**
   * When last Log entry date in failure.
   */
  protected volatile Date lastLogFailed = null;

  /**
   * How many logs are written into fallback.
   */
  protected volatile long lastFailureCount = 0;

  /**
   * How long log in fallback in error case.
   */
  private long stayInFallbackSeconds = TimeInMillis.SECOND * 60;

  /**
   * Used to make fallback Logging.
   */
  private Logging secondary = new Log4JLogging(Log4JLogging.LOG4J_FALLBACK_PREFIX);

  /**
   * The max hard failed log queue.
   */
  private int maxHardFailedLogQueue = 2000;

  /**
   * The max soft failed log queue.
   */
  private int maxSoftFailedLogQueue = 20000;

  /**
   * The hard queue.
   */
  protected RoundList<LogWriteEntry> hardQueue = new RoundList<>(maxHardFailedLogQueue);

  /**
   * The soft queue.
   */
  protected SoftReference<RoundList<LogWriteEntry>> softQueue = new SoftReference<>(
      new RoundList<LogWriteEntry>(maxSoftFailedLogQueue));

  /**
   * This implements persistence of Logging, which may fail.
   * 
   * @param lwe log entry to write
   */
  protected abstract void doLogImplWithFallback(LogWriteEntry lwe);

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void doLogImpl(LogWriteEntry lwe)
  {
    if (lastLogFailure != null) {
      synchronized (this) {
        ++lastFailureCount;
        Date cdate = new Date();
        if ((cdate.getTime() - lastLogFailure.getTime()) < stayInFallbackSeconds) {
          writeToSecondary(lwe);
          return;
        }
        lastLogFailure = null;
        drainFallbackLogs();
        /**
         * @logging
         * @reason Das Logging in die Datenbank ist zu dem angegebenen Zeitpunkt fehlgeschlagen
         * @action Dieser LogEintrag wird im Retry-Fall geschrieben. Sollte Datum des ersten fehlgeschlagenen
         *         DB-Logeintrags enthalten
         */
        fatal(GenomeLogCategory.Database, "Some DBLogging failed: "
            + Converter.dateToDebugString(lastLogFailed)
            + "; c: "
            + lastFailureCount
            + "; n: "
            + HostUtils.getThisHostName()
            + "; time: "
            + Converter.dateToDebugString(lastLogFailed));
      }
    }

    try {
      doLogImplWithFallback(lwe);
      lastLogFailed = null;
      lastFailureCount = 0;
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      writeToSecondary(lwe);
      getSecondary().doLog(LogLevel.Fatal, GenomeLogCategory.Database, "Cannot write to Logging Table",
          new LogExceptionAttribute(ex));
      lastLogFailure = new Date();
      if (lastLogFailed == null) {
        lastLogFailed = lastLogFailure;
      }
    }
  }

  /**
   * Write to secondary.
   *
   * @param lwe the lwe
   */
  protected void writeToSecondary(LogWriteEntry lwe)
  {
    getSecondary().doLogImpl(lwe);
    putToFallbackQueue(lwe);
  }

  /**
   * Drain fallback logs.
   */
  protected void drainFallbackLogs()
  {
    RoundList<LogWriteEntry> sq = softQueue.get();
    if (sq != null) {
      writeFallbackQueue(sq);
    }
    writeFallbackQueue(hardQueue);
  }

  /**
   * Write fallback queue.
   *
   * @param sq the sq
   */
  protected void writeFallbackQueue(RoundList<LogWriteEntry> sq)
  {
    for (LogWriteEntry lwe : sq) {
      doLogImplWithFallback(lwe);
    }
    sq.clear();
  }

  /**
   * Put to fallback queue.
   *
   * @param lwe the lwe
   */
  protected void putToFallbackQueue(LogWriteEntry lwe)
  {
    synchronized (this) {
      if (hardQueue.size() == maxHardFailedLogQueue) {
        LogWriteEntry lastentry = hardQueue.get(hardQueue.size() - 1);
        RoundList<LogWriteEntry> sq = softQueue.get();
        if (sq == null) {
          softQueue = new SoftReference<>(new RoundList<LogWriteEntry>(maxSoftFailedLogQueue));
        }

        sq = softQueue.get();
        sq.add(lastentry);
      }
      hardQueue.add(lwe);
    }
  }

  /**
   * Gets the stay in fallback seconds.
   *
   * @return the stay in fallback seconds
   */
  public long getStayInFallbackSeconds()
  {
    return stayInFallbackSeconds;
  }

  /**
   * Sets the stay in fallback seconds.
   *
   * @param stayInFallbackSeconds the new stay in fallback seconds
   */
  public void setStayInFallbackSeconds(long stayInFallbackSeconds)
  {
    this.stayInFallbackSeconds = stayInFallbackSeconds;
  }

  /**
   * Gets the secondary.
   *
   * @return the secondary
   */
  @Override
  public Logging getSecondary()
  {
    return secondary;
  }

  /**
   * Sets the secondary.
   *
   * @param secondary the new secondary
   */
  @Override
  public void setSecondary(Logging secondary)
  {
    this.secondary = secondary;
  }

  /**
   * Gets the max hard failed log queue.
   *
   * @return the max hard failed log queue
   */
  public int getMaxHardFailedLogQueue()
  {
    return maxHardFailedLogQueue;
  }

  /**
   * Sets the max hard failed log queue.
   *
   * @param maxHardFailedLogQueue the new max hard failed log queue
   */
  public void setMaxHardFailedLogQueue(int maxHardFailedLogQueue)
  {
    this.maxHardFailedLogQueue = maxHardFailedLogQueue;
    hardQueue = new RoundList<LogWriteEntry>(maxHardFailedLogQueue); // NOSONAR reassign locked design
  }

  /**
   * Gets the max soft failed log queue.
   *
   * @return the max soft failed log queue
   */
  public int getMaxSoftFailedLogQueue()
  {
    return maxSoftFailedLogQueue;
  }

  /**
   * Sets the max soft failed log queue.
   *
   * @param maxSoftFailedLogQueue the new max soft failed log queue
   */
  public void setMaxSoftFailedLogQueue(int maxSoftFailedLogQueue)
  {
    this.maxSoftFailedLogQueue = maxSoftFailedLogQueue;
    softQueue = new SoftReference<>(new RoundList<LogWriteEntry>(maxSoftFailedLogQueue)); // NOSONAR reassign locked design
  }
}
