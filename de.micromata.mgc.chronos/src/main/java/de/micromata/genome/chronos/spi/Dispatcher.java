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

package de.micromata.genome.chronos.spi;

import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.SchedulerConfigurationException;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * Interface which implements the Thread, which dispatches Scheduler Requests.
 * 
 * Do not use this directly, but SchedulerDAO.
 * 
 * @author roger
 * 
 */
public interface Dispatcher
{

  /**
   * return true, if the dispatcher is running.
   *
   * @return true, if is running
   */
  public boolean isRunning();

  /**
   * Starts the Dispatcher.
   */
  public void startup();

  /**
   * Shutdown.
   *
   * @throws InterruptedException the interrupted exception
   * @see de.micromata.genome.chronos.spi.Dispatcher.shutdown(long)
   */
  public void shutdown() throws InterruptedException;

  /**
   * wake up sleeping dispatcher.
   */
  public void wakeup();

  /**
   * Shutdown.
   *
   * @param waitForShutdown time to wait for shutdown. <= 0 means wait forever.
   * @throws InterruptedException the interrupted exception
   */
  public void shutdown(final long waitForShutdown) throws InterruptedException;

  /**
   * Set the minimal node bind timeout.
   *
   * @param minNodeBindTimeout the new min node bind time
   */
  public void setMinNodeBindTime(long minNodeBindTimeout);

  /**
   * Gibt den Scheduler mit dem angegebenen Namen zurück oder <code>null</code>.
   * <p>
   * Hier wird <u>nicht</u> auf die Datenbank zugegriffen. Dafür ist {@link #createOrGetScheduler(SchedulerDO)} zu benutzen.
   * </p>
   *
   * @param name the name
   * @return the scheduler
   */
  public Scheduler getScheduler(final String name);

  /**
   * Gets the scheduler by pk.
   *
   * @param pk the pk
   * @return the scheduler by pk
   */
  public Scheduler getSchedulerByPk(Long pk);

  /**
   * Gibt zu einem {@link SchedulerDO} die entspechende reinitialisierte {@link Scheduler} zurück, oder erzeugt diese neu.
   * <p>
   * Ein neu angelegter Scheduler wird unmittelbar persisitiert und unter dem Namen abgespeichert.
   * </p>
   *
   * @param schedulerDO the scheduler do
   * @return the scheduler
   * @see #schedulerByPk
   */
  public Scheduler createOrGetScheduler(final SchedulerDO schedulerDO);

  /**
   * Submits a new Job.
   * 
   * If the dispatcher is not started, the job will just inserted into the JobStore.
   * 
   * @param schedulerName must not be null
   * @param jobDefinition must not be null
   * @param arg may be null
   * @param trigger must not be null
   * @param hostName if null, uses the current hostname
   * @return Job reference (pk)
   * @throws SchedulerConfigurationException wenn ein nicht registrierter Scheduler angesprochen wird
   */
  public long submit(String schedulerName, JobDefinition jobDefinition, Object arg, Trigger trigger, String hostName);

  /**
   * Submits a new Job.
   * 
   * If the dispatcher is not started, the job will just inserted into the JobStore.
   *
   * @param schedulerName must not be null
   * @param jobName the job name
   * @param jobDefinition must not be null
   * @param arg may be null
   * @param trigger must not be null
   * @param hostName if null, uses the current hostname
   * @return Job reference (pk)
   * @throws SchedulerConfigurationException wenn ein nicht registrierter Scheduler angesprochen wird
   */
  public long submit(String schedulerName, String jobName, JobDefinition jobDefinition, Object arg, Trigger trigger, String hostName);

  /**
   * returns the JobStore assigned with this dispatcher.
   *
   * @return the job store
   */
  public JobStore getJobStore();

  /**
   * Gets the virtual host name.
   *
   * @return the (virtual or real) host name this dispatcher is running
   */
  public String getVirtualHostName();

  /**
   * Create the thread group for dispatcher.
   *
   * @return the creates the dispatcher thread group
   */
  public ThreadGroup getCreateDispatcherThreadGroup();

  /**
   * return the name of the dispatcher.
   *
   * @return the dispatcher name
   */
  public String getDispatcherName();

  /**
   * If job is waiting and applies to this node and nextfiretime is in near future, add this job to the local queue.
   *
   * @param job the job
   */
  public void addToReservedIfNessary(TriggerJobDO job);
}
