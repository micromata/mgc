/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   05.02.2008
// Copyright Micromata 05.02.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi;

/**
 * A Thread runned into the Scheduler/Dispatcher.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SchedulerThread extends Thread
{

  /**
   * The job id.
   */
  private long jobId;

  /**
   * Instantiates a new scheduler thread.
   */
  public SchedulerThread()
  {

  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param target the target
   * @param name the name
   */
  public SchedulerThread(Runnable target, String name)
  {
    super(target, name);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param target the target
   */
  public SchedulerThread(Runnable target)
  {
    super(target);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param name the name
   */
  public SchedulerThread(String name)
  {
    super(name);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param target the target
   * @param name the name
   * @param stackSize the stack size
   */
  public SchedulerThread(ThreadGroup group, Runnable target, String name, long stackSize)
  {
    super(group, target, name, stackSize);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param target the target
   * @param name the name
   */
  public SchedulerThread(ThreadGroup group, Runnable target, String name)
  {
    super(group, target, name);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param target the target
   */
  public SchedulerThread(ThreadGroup group, Runnable target)
  {
    super(group, target);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param name the name
   */
  public SchedulerThread(ThreadGroup group, String name)
  {
    super(group, name);
  }

  @Override
  public String toString()
  {
    return super.toString() + "; jobId: " + jobId;
  }

  public long getJobId()
  {
    return jobId;
  }

  public void setJobId(long jobId)
  {
    this.jobId = jobId;
  }

}
