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

package de.micromata.genome.chronos.util;

import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;

/**
 * Internal class to create a shedulder.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SchedulerFactory
{

  /**
   * The dispatcher.
   */
  private Dispatcher dispatcher;

  /**
   * The scheduler name.
   */
  private String schedulerName;

  /**
   * The start on create.
   */
  private boolean startOnCreate = true;

  /**
   * The startup timeout.
   */
  private int startupTimeout;

  /**
   * The job retry time.
   */
  private int jobRetryTime = 60;

  /**
   * The job max retry count.
   */
  private int jobMaxRetryCount = 0;

  /**
   * The service retry time.
   */
  private int serviceRetryTime = 60;

  /**
   * The thread pool size.
   */
  private int threadPoolSize = 2;

  /**
   * The node binding timeout.
   */
  private int nodeBindingTimeout = 0;

  /**
   * Sets the scheduler name.
   *
   * @param schedulerName the new scheduler name
   */
  public void setSchedulerName(final String schedulerName)
  {
    this.schedulerName = schedulerName;
  }

  /**
   * Sets the start on create.
   *
   * @param startOnCreate the new start on create
   */
  public void setStartOnCreate(final boolean startOnCreate)
  {
    this.startOnCreate = startOnCreate;
  }

  /**
   * Sets the startup timeout.
   *
   * @param startupTimeoutInSeconds the new startup timeout
   */
  public void setStartupTimeout(final int startupTimeoutInSeconds)
  {
    this.startupTimeout = startupTimeoutInSeconds;
  }

  /**
   * Sets the service retry time.
   *
   * @param serviceRetryTimeInSeconds the new service retry time
   */
  public void setServiceRetryTime(final int serviceRetryTimeInSeconds)
  {
    this.serviceRetryTime = serviceRetryTimeInSeconds;
  }

  /**
   * Sets the job retry time.
   *
   * @param jobRetryTimeInSeconds the new job retry time
   */
  public void setJobRetryTime(final int jobRetryTimeInSeconds)
  {
    this.jobRetryTime = jobRetryTimeInSeconds;
  }

  /**
   * Sets the thread pool size.
   *
   * @param threadPoolSize the new thread pool size
   */
  public void setThreadPoolSize(int threadPoolSize)
  {
    this.threadPoolSize = threadPoolSize;
  }

  /**
   * Creates the.
   *
   * @param jobStore the job store
   * @return the scheduler
   */
  public Scheduler create(JobStore jobStore)
  {
    final SchedulerDO scheduler = new SchedulerDO();
    scheduler.setName(schedulerName);
    scheduler.setServiceRetryTime(serviceRetryTime);
    scheduler.setJobRetryTime(jobRetryTime);
    scheduler.setJobMaxRetryCount(jobMaxRetryCount);
    scheduler.setThreadPoolSize(threadPoolSize);
    scheduler.setNodeBindingTimeout(nodeBindingTimeout);
    jobStore.persist(scheduler);
    final Scheduler schedulerL = dispatcher.createOrGetScheduler(scheduler);

    schedulerL.resume();

    if (startOnCreate == false) {
      schedulerL.pause(startupTimeout);
    }

    return schedulerL;
  }

  /**
   * Sets the dispatcher.
   *
   * @param dispatcher the new dispatcher
   */
  public void setDispatcher(Dispatcher dispatcher)
  {
    this.dispatcher = dispatcher;
  }

  /**
   * Gets the node binding timeout.
   *
   * @return the node binding timeout
   */
  public int getNodeBindingTimeout()
  {
    return nodeBindingTimeout;
  }

  /**
   * Sets the node binding timeout.
   *
   * @param nodeBindingTimeout the new node binding timeout
   */
  public void setNodeBindingTimeout(int nodeBindingTimeout)
  {
    this.nodeBindingTimeout = nodeBindingTimeout;
  }

  /**
   * Gets the job retry time.
   *
   * @return the job retry time
   */
  public int getJobRetryTime()
  {
    return jobRetryTime;
  }

  /**
   * Gets the scheduler name.
   *
   * @return the scheduler name
   */
  public String getSchedulerName()
  {
    return schedulerName;
  }

  /**
   * Gets the service retry time.
   *
   * @return the service retry time
   */
  public int getServiceRetryTime()
  {
    return serviceRetryTime;
  }

  /**
   * Checks if is start on create.
   *
   * @return true, if is start on create
   */
  public boolean isStartOnCreate()
  {
    return startOnCreate;
  }

  /**
   * Gets the startup timeout.
   *
   * @return the startup timeout
   */
  public int getStartupTimeout()
  {
    return startupTimeout;
  }

  /**
   * Gets the thread pool size.
   *
   * @return the thread pool size
   */
  public int getThreadPoolSize()
  {
    return threadPoolSize;
  }

  /**
   * Gets the job max retry count.
   *
   * @return the job max retry count
   */
  public int getJobMaxRetryCount()
  {
    return jobMaxRetryCount;
  }

  /**
   * Sets the job max retry count.
   *
   * @param jobMaxRetryCount the new job max retry count
   */
  public void setJobMaxRetryCount(int jobMaxRetryCount)
  {
    this.jobMaxRetryCount = jobMaxRetryCount;
  }

}
