//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.chronos;

import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * Gruppiert Jobs nach technischer Sicht, z.B. bzgl. eines Services wie Mail-Jobs.
 * <p>
 * Hier werden die Jobs angestossen und auf die Threads verteilt.
 * </p>
 * 
 * @author roger@micromata.de
 */

public interface Scheduler
{
  /**
   * Internal statistics about a scheduler.
   * 
   * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
   *
   */
  public static class Stats
  {

    /**
     * The pool size.
     */
    public int poolSize;

    /**
     * The pool active.
     */
    public int poolActive;

    /**
     * The pool completed.
     */
    public long poolCompleted;

    /**
     * The pool waiting.
     */
    public int poolWaiting;

    /**
     * The pool task count.
     */
    public long poolTaskCount;
  }

  /**
   * @return unique symbolic Name of the scheduler
   */
  public String getName();

  /**
   * Stop executing jobs.
   */
  public void suspend();

  /**
   * resume executing jobs.
   */
  public void resume();

  /**
   * return the db pk of the scheduler.
   * @return the id of the scheduler
   */
  public long getId();

  /**
   * set an persist the service retry time
   * 
   * @param serviceRetryTimeInSeconds the time in seconds before next retry
   */
  public void setServiceRetryTime(int serviceRetryTimeInSeconds);

  /**
   * 
   * @return the service retry time of this scheduler
   */
  public int getServiceRetryTime();

  /**
   * set the job retry time of this scheduler
   * 
   * @param jobRetryTimeInSeconds the time in seconds before the next retry
   */
  public void setJobRetryTime(int jobRetryTimeInSeconds);

  /**
   * 
   * @return the job retry time of this scheduler in seconds
   */
  public int getJobRetryTime();

  /**
   * 
   * @return the maximum job retry for this scheduler
   */
  public int getJobMaxRetryCount();

  /**
   * 
   * @return the dispatcher used for this scheduler
   */
  public Dispatcher getDispatcher();

  /**
   * Pauses execution of jobs due ServiceUnavailable.
   *
   * @param seconds suspend the scheduler for given time
   */
  public void pause(int seconds);

  /**
   * 
   * @return true if scheduler is running with at least 1 thread. Paused scheduler are also running
   */
  public boolean isRunning();

  /**
   * Execute the given job.
   * 
   * This will be called inside the scheduler thread
   *
   * @param job the job
   * @param jobStore the job store
   * @return true, if successful
   */
  public boolean executeJob(TriggerJobDO job, JobStore jobStore);

  /**
   * set the number of maximum threads used for this scheduler
   * 
   * @param threadPoolSize the size of the pool
   */
  public void setThreadPoolSize(int threadPoolSize);

  /**
   * 
   * @return the maximum thread count used by this scheduler
   */
  public int getThreadPoolSize();

  /**
   * 
   * @return the node bind timeout in seconds
   */
  public int getNodeBindingTimeout();

  /**
   * Sets the node bind time out. In this time a job will be executed on node
   * 
   * @param nodeBindingTimeout in seconds
   */
  public void setNodeBindingTimeout(int nodeBindingTimeout);

  /**
   * 
   * @return the internal statistics about this scheduler
   */
  public Stats getSchedulerStats();

  /**
   * shut down the scheduler thread pool.
   *
   * @param waitForShutdown if -1 wait unlimited time
   * @return true if the scheduler thread pool was shut down successfully
   */
  public boolean shutdown(final long waitForShutdown);

  /**
   * 
   * @return the DO for persistence of this scheduler
   */
  public SchedulerDO getDO();

  /**
   * reinitialize the scheduler.
   *
   * @param schedulerDO the scheduler do
   */
  public void reInit(final SchedulerDO schedulerDO);

  /**
   * Sets the internal pk for this scheduler
   * 
   * @param schedId the id of the scheduler to set
   */
  public void setSchedulerId(long schedId);

  /**
   * 
   * @return the free threads in this scheduler thread pool
   */
  public int getFreeJobSlotsCount();

}
