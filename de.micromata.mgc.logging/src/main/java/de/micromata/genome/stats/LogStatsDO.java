/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   21.03.2008
// Copyright Micromata 21.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.stats;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.micromata.genome.logging.LogLevel;

/**
 * Pojo for Logging Stats entry.
 * 
 * @author roger@micromata.de
 * 
 */
public class LogStatsDO extends TypeStatsDO implements Cloneable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8074947033254064946L;

  /**
   * The Class LogStatEntry.
   */
  static public class LogStatEntry implements Serializable, Cloneable
  {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8471880233507225170L;

    /**
     * The debug count.
     */
    private long debugCount;

    /**
     * The info count.
     */
    private long infoCount;

    /**
     * The note count.
     */
    private long noteCount;

    /**
     * The warn count.
     */
    private long warnCount;

    /**
     * The error count.
     */
    private long errorCount;

    /**
     * The fatal count.
     */
    private long fatalCount;

    /**
     * Copy to.
     *
     * @param c the c
     */
    protected void copyTo(LogStatEntry c)
    {
      c.debugCount = debugCount;
      c.infoCount = infoCount;
      c.noteCount = noteCount;
      c.warnCount = warnCount;
      c.errorCount = errorCount;
      c.fatalCount = fatalCount;
    }

    @Override
    public synchronized Object clone()
    {
      LogStatEntry c = new LogStatEntry();
      copyTo(c);
      return c;
    }

    /**
     * Inc error count.
     */
    final public void incErrorCount()
    {
      ++errorCount;
    }

    /**
     * Inc warn count.
     */
    final public void incWarnCount()
    {
      ++warnCount;
    }

    /**
     * Inc fatal count.
     */
    final public void incFatalCount()
    {
      ++fatalCount;
    }

    /**
     * Inc note count.
     */
    final public void incNoteCount()
    {
      ++noteCount;
    }

    /**
     * Inc info count.
     */
    final public void incInfoCount()
    {
      ++infoCount;
    }

    /**
     * Inc debug count.
     */
    final public void incDebugCount()
    {
      ++debugCount;
    }

    public long getErrorCount()
    {
      return errorCount;
    }

    public void setErrorCount(long errorCount)
    {
      this.errorCount = errorCount;
    }

    public long getFatalCount()
    {
      return fatalCount;
    }

    public void setFatalCount(long fatalCount)
    {
      this.fatalCount = fatalCount;
    }

    public long getNoteCount()
    {
      return noteCount;
    }

    public void setNoteCount(long noteCount)
    {
      this.noteCount = noteCount;
    }

    public long getWarnCount()
    {
      return warnCount;
    }

    public void setWarnCount(long warnCount)
    {
      this.warnCount = warnCount;
    }

    public long getDebugCount()
    {
      return debugCount;
    }

    public void setDebugCount(long debugCount)
    {
      this.debugCount = debugCount;
    }

    public long getInfoCount()
    {
      return infoCount;
    }

    public void setInfoCount(long infoCount)
    {
      this.infoCount = infoCount;
    }
  }

  /**
   * The stats.
   */
  private Map<String, LogStatEntry> stats = new TreeMap<String, LogStatEntry>();

  /**
   * The all stats.
   */
  private LogStatEntry allStats = new LogStatEntry();

  @Override
  public Object clone()
  {
    LogStatsDO l = new LogStatsDO();
    for (Map.Entry<String, LogStatEntry> m : stats.entrySet()) {
      l.stats.put(m.getKey(), (LogStatEntry) m.getValue().clone());
    }
    l.allStats = (LogStatEntry) allStats.clone();
    return l;
  }

  public Map<String, LogStatEntry> getStats()
  {
    return stats;
  }

  public Set<Map.Entry<String, LogStatEntry>> getStatsEntries()
  {
    return stats.entrySet();
  }

  public void setStats(Map<String, LogStatEntry> stats)
  {
    this.stats = stats;
  }

  /**
   * Sets the stat.
   *
   * @param category the category
   * @param level the level
   */
  public synchronized void setStat(String category, LogLevel level)
  {
    LogStatEntry le = stats.get(category);
    if (le == null) {
      le = new LogStatEntry();
      stats.put(category, le);
    }
    setStat(le, level);
    setStat(allStats, level);
  }

  /**
   * Sets the stat.
   *
   * @param le the le
   * @param level the level
   */
  private void setStat(LogStatEntry le, LogLevel level)
  {
    setLastDate(new Date());
    switch (level) {
      case Note:
        le.incNoteCount();
        break;
      case Warn:
        le.incWarnCount();
        break;
      case Error:
        le.incErrorCount();
        break;
      case Fatal:
        le.incFatalCount();
        break;
      case Info:
        le.incInfoCount();
        break;
      case Debug:
      case Trace:
        le.incDebugCount();
        break;
    }
  }

  public LogStatEntry getAllStats()
  {
    return allStats;
  }

  public void setAllStats(LogStatEntry allStats)
  {
    this.allStats = allStats;
  }
}
