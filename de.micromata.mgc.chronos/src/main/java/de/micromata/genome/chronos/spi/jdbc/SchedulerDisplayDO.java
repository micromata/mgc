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
 * The Class SchedulerDisplayDO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SchedulerDisplayDO extends SchedulerDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5924549593570479121L;

  /**
   * The pool size.
   */
  private int poolSize;

  /**
   * The pool active.
   */
  private int poolActive;

  /**
   * The pool completed.
   */
  private long poolCompleted;

  /**
   * The pool waiting.
   */
  private int poolWaiting;

  /**
   * The pool task count.
   */
  private long poolTaskCount;

  /**
   * Instantiates a new scheduler display do.
   */
  public SchedulerDisplayDO()
  {//TODO rrk dieser Konstruktro hat gefehlt. Ist das korrekt ?
    super();
  }

  /**
   * Instantiates a new scheduler display do.
   *
   * @param sched the sched
   */
  public SchedulerDisplayDO(SchedulerDO sched)
  {
    super(sched);
  }

  public int getPoolActive()
  {
    return poolActive;
  }

  public void setPoolActive(int poolActive)
  {
    this.poolActive = poolActive;
  }

  public long getPoolCompleted()
  {
    return poolCompleted;
  }

  public void setPoolCompleted(long poolCompleted)
  {
    this.poolCompleted = poolCompleted;
  }

  public int getPoolSize()
  {
    return poolSize;
  }

  public void setPoolSize(int poolSize)
  {
    this.poolSize = poolSize;
  }

  public long getPoolTaskCount()
  {
    return poolTaskCount;
  }

  public void setPoolTaskCount(long poolTaskCount)
  {
    this.poolTaskCount = poolTaskCount;
  }

  public int getPoolWaiting()
  {
    return poolWaiting;
  }

  public void setPoolWaiting(int poolWaiting)
  {
    this.poolWaiting = poolWaiting;
  }
}
