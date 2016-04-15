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

import java.util.concurrent.ThreadFactory;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;

/**
 * Factory for scheduler threads.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SchedulerThreadFactory implements ThreadFactory
{

  /**
   * The monitor.
   */
  private final Object monitor = new Object();

  /**
   * The thread group name.
   */
  private String threadGroupName;

  /**
   * The thread group.
   */
  private ThreadGroup threadGroup;

  /**
   * The thread name prefix.
   */
  private String threadNamePrefix = "";

  /**
   * The thread priority.
   */
  private int threadPriority = Thread.NORM_PRIORITY;

  /**
   * The daemon.
   */
  private boolean daemon = false;

  /**
   * The thread count.
   */
  private int threadCount = 0;

  @Override
  public Thread newThread(Runnable r)
  {
    return createThread(r);
  }

  /**
   * Creates a new SchedulerThread object.
   *
   * @param runnable the runnable
   * @return the thread
   */
  public Thread createThread(Runnable runnable)
  {
    SchedulerThread thread = new SchedulerThread(getThreadGroup(),
        runnable, nextThreadName());
    thread.setPriority(getThreadPriority());
    thread.setDaemon(isDaemon());
    /**
     * @logging
     * @reason Scheduler startet einen Job
     * @action Keine
     */
    GLog.note(GenomeLogCategory.Scheduler, "Chronos; Create Thread: "
        + thread);
    return thread;
  }

  protected String getDefaultThreadNamePrefix()
  {
    return getClass().getSimpleName() + "-";
  }

  /**
   * Return the thread name to use for a newly created thread.
   * <p>
   * Default implementation returns the specified thread name prefix with an increasing thread count appended: for
   * example, "SimpleAsyncTaskExecutor-0".
   *
   * @return the string
   * @see #getThreadNamePrefix()
   */
  protected String nextThreadName()
  {
    int threadNumber = 0;
    synchronized (this.monitor) {
      this.threadCount++;
      threadNumber = this.threadCount;
    }
    return getThreadNamePrefix() + threadNumber;
  }

  public String getThreadGroupName()
  {
    return threadGroupName;
  }

  public void setThreadGroupName(String threadGroupName)
  {
    this.threadGroupName = threadGroupName;
  }

  public String getThreadNamePrefix()
  {
    return threadNamePrefix;
  }

  public void setThreadNamePrefix(String threadNamePrefix)
  {
    this.threadNamePrefix = threadNamePrefix;
  }

  public ThreadGroup getThreadGroup()
  {
    return threadGroup;
  }

  public void setThreadGroup(ThreadGroup threadGroup)
  {
    this.threadGroup = threadGroup;
  }

  public int getThreadPriority()
  {
    return threadPriority;
  }

  public void setThreadPriority(int threadPriority)
  {
    this.threadPriority = threadPriority;
  }

  public boolean isDaemon()
  {
    return daemon;
  }

  public void setDaemon(boolean daemon)
  {
    this.daemon = daemon;
  }

}
