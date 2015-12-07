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

/**
 * POJO for holding Stats.
 * 
 * @author roger@micromata.de
 * 
 */
public class StatsDO implements Serializable, Cloneable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5464154130335924966L;

  /**
   * The log stats.
   */
  private LogStatsDO logStats = new LogStatsDO();

  /**
   * The perf stats.
   */
  private PerfStatsDO perfStats = new PerfStatsDO();

  public LogStatsDO getLogStats()
  {
    return logStats;
  }

  public void setLogStats(LogStatsDO logStats)
  {
    this.logStats = logStats;
  }

  public PerfStatsDO getPerfStats()
  {
    return perfStats;
  }

  public void setPerfStats(PerfStatsDO perfStats)
  {
    this.perfStats = perfStats;
  }

  @Override
  public Object clone()
  {
    StatsDO s = new StatsDO();
    s.logStats = (LogStatsDO) logStats.clone();
    s.perfStats = (PerfStatsDO) perfStats.clone();
    return s;
  }
}
