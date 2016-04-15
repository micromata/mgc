/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   19.02.2007
// Copyright Micromata 19.02.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi.jdbc;

/**
 * Wrapps a query for jobs.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AdminJobQueryDO
{

  /**
   * The host name.
   */
  private String hostName = "";

  /**
   * The state.
   */
  private String state = "";

  /**
   * The job name.
   */
  private String jobName = "";

  /**
   * The scheduler name.
   */
  private String schedulerName = "";

  /**
   * The result count.
   */
  private int resultCount = 10;

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName(String jobName)
  {
    this.jobName = jobName;
  }

  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  public String getSchedulerName()
  {
    return schedulerName;
  }

  public void setSchedulerName(String schedulerName)
  {
    this.schedulerName = schedulerName;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public int getResultCount()
  {
    return resultCount;
  }

  public void setResultCount(int resultCount)
  {
    this.resultCount = resultCount;
  }
}
