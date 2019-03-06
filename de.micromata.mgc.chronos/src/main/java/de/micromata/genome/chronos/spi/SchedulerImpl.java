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

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.manager.LogJobEventAttribute;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;

/**
 * Gruppiert Jobs bezüglich eines technischen Sachverhalts, z.B. einem Dienst wie Mail.
 * <p>
 * Repräsentiert im Gegensatz zum {@link SchedulerDO} die Runtime-Sicht auf einen {@link Scheduler}.
 * </p>
 * 
 * @author roger
 */
public class SchedulerImpl implements Scheduler, RejectedExecutionHandler
{

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(SchedulerImpl.class);

  /**
   * The name.
   */
  private String name;

  /**
   * The scheduler id.
   */
  private long schedulerId = -1;

  /**
   * The executor.
   */
  private SchedulerThreadPoolExecutor executor;

  /**
   * The queue.
   */
  private BlockingQueue<Runnable> queue;

  /**
   * The paused.
   */
  private boolean paused = false;

  /**
   * The next runtime.
   */
  protected long nextRuntime;

  /**
   * The service retry time.
   */
  private int serviceRetryTime;

  /**
   * The job retry time.
   */
  private int jobRetryTime;

  /**
   * The job max retry count.
   */
  private int jobMaxRetryCount;

  /**
   * The node binding timeout.
   */
  private int nodeBindingTimeout;

  /**
   * The thread pool size.
   */
  private int threadPoolSize;

  /**
   * Dispatcher owns this scheduler.
   */
  private Dispatcher dispatcher;

  @Override
  public void setThreadPoolSize(int threadPoolSize)
  {
    this.threadPoolSize = threadPoolSize;
    if (executor != null) {
      adjustThreadPoolSize(threadPoolSize);
    }
  }

  @Override
  public int getThreadPoolSize()
  {
    return this.threadPoolSize;
  }

  /**
   * Instantiates a new scheduler impl.
   */
  public SchedulerImpl()
  {
    super();
  }

  /**
   * Ruft {@link #initThreadPool(int, String)} und {@link #initProperties(SchedulerDO)} auf.
   *
   * @param schedulerDO the scheduler do
   * @param dispatcher the dispatcher
   */
  public SchedulerImpl(final SchedulerDO schedulerDO, final Dispatcher dispatcher)
  {
    Validate.notNull(schedulerDO, "schedulerDO is null");
    final String schedulerName = schedulerDO.getName();
    Validate.notNull(schedulerDO, "schedulerDO.name is null");
    Validate.notNull(dispatcher, "dispatcher is null");
    this.dispatcher = dispatcher;
    initThreadPool(schedulerDO.getThreadPoolSize(), schedulerName);
    initProperties(schedulerDO);
  }

  /**
   * Kopiert die Properties von DB-Objekt außer {@link SchedulerDO#getThreadPoolSize()} und einer schon gesetzten Id
   * oder Namen.
   * <p>
   * Also wird eine Id nie reinitialisiert.
   * </p>
   *
   * @param schedulerDO the scheduler do
   */
  private void initProperties(final SchedulerDO schedulerDO)
  {
    if (this.schedulerId == SchedulerDO.UNSAVED_SCHEDULER_ID) {
      this.schedulerId = schedulerDO.getPk();
    }
    if (this.name == null) {
      this.name = schedulerDO.getName();
    }
    this.serviceRetryTime = schedulerDO.getServiceRetryTime();
    this.jobRetryTime = schedulerDO.getJobRetryTime();
    this.jobMaxRetryCount = schedulerDO.getJobMaxRetryCount();
    this.nodeBindingTimeout = schedulerDO.getNodeBindingTimeout();
    this.threadPoolSize = schedulerDO.getThreadPoolSize();
  }

  /**
   * Erzeugt ein neues DO als Kopie der eigenen Attribute.
   * 
   * @return the {@link SchedulerDO}
   */
  @Override
  public SchedulerDO getDO()
  {
    final SchedulerDO schedulerDO = new SchedulerDO();
    schedulerDO.setPk(schedulerId);
    schedulerDO.setName(name);
    schedulerDO.setServiceRetryTime(serviceRetryTime);
    schedulerDO.setJobRetryTime(jobRetryTime);
    schedulerDO.setJobMaxRetryCount(jobMaxRetryCount);
    schedulerDO.setThreadPoolSize(threadPoolSize);
    schedulerDO.setNodeBindingTimeout(nodeBindingTimeout);
    return schedulerDO;
  }

  /**
   * Reinitialisiert den Scheduler und adjustiert ggf. den Thread-Pool neu.
   * 
   * @param schedulerDO the do
   * @see #initProperties(SchedulerDO)
   * @see #initThreadPool(int, String)
   */
  @Override
  public void reInit(final SchedulerDO schedulerDO)
  {
    initProperties(schedulerDO);
    adjustThreadPoolSize(schedulerDO.getThreadPoolSize());
  }

  /**
   * Instanziiert den Thread-Pool.
   *
   * @param threadPoolSize the thread pool size
   * @param name the name
   */
  private void initThreadPool(final int threadPoolSize, final String name)
  {
    queue = new LinkedBlockingQueue<Runnable>();
    this.threadPoolSize = threadPoolSize;

    int i = threadPoolSize;
    if (threadPoolSize == 0) {
      i = 1;
      GLog.warn(GenomeLogCategory.Scheduler, "ThreadPoolSize is given with 0: " + name);
    }
    // es muss mind. ein Thread sein
    executor = new SchedulerThreadPoolExecutor(threadPoolSize, i, 1, TimeUnit.SECONDS, queue, this);
    SchedulerThreadFactory tfactory = new SchedulerThreadFactory();
    String appId = ChronosServiceManager.get().getSchedulerDAO().getShortApplicationName();
    String threadGroupName = "JCWTG[" + appId + "]: " + dispatcher.getDispatcherName() + "; " + name;
    ThreadGroup threadGroup = new ThreadGroup(dispatcher.getCreateDispatcherThreadGroup(), threadGroupName);
    tfactory.setThreadGroup(threadGroup);
    tfactory.setThreadNamePrefix("JCWT[" + appId + "]: " + dispatcher.getDispatcherName() + "; " + name);
    executor.setThreadFactory(tfactory);
  }

  /**
   * Setzt die neue Größe des Thread-Pools.
   *
   * @param newSize the new size
   */
  public synchronized void adjustThreadPoolSize(int newSize)
  {
    this.threadPoolSize = newSize;

    // work around das Framework kann keine 0
    // es wird also immer mind. ein Thread allociert
    if (newSize == 0) {
      newSize = 1;
      paused = true;
    }
    executor.setMaximumPoolSize(Math.max(newSize, executor.getCorePoolSize()));
    executor.setCorePoolSize(newSize);
  }

  synchronized void setNextRuntime(final long lastRuntime)
  {
    nextRuntime = lastRuntime;
  }

  /**
   * Führ einen Job aus.
   * Versucht den Job in der Datenbank für sich zu reservieren {@link JobStore#reserveJob(TriggerJobDO)} und
   * führt ihn dann mit dem {@link ThreadPoolExecutor#execute(Runnable)} aus.
   *
   * @see de.micromata.genome.chronos.Scheduler#executeJob(TriggerJobDO, JobStore)
   */
  @Override
  public synchronized boolean executeJob(final TriggerJobDO job, JobStore jobStore)
  {
    if (checkPausedScheduler() == true) {
      return false;
    }
    nextRuntime = System.currentTimeMillis();
    adjustPoolSize();

    if (checkThreadPoolExhausted(job) == true) {

      return false;
    }
    TriggerJobDO jobToExecute = jobStore.reserveJob(job);
    if (jobToExecute == null) {
      /**
       * @logging
       * @reason cannot reserve job (set to SCHEDULE), probably because other node already reserved it.
       * @action nothing
       */
      GLog.info(GenomeLogCategory.Scheduler, "Cannot reservice job",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null, null, this)));
      return false;
    }

    executeJobNow(jobToExecute, jobStore);
    return true;
  }

  protected JobRunner createJobRunner(Scheduler scheduler, TriggerJobDO job)
  {
    return new JobRunner(this, job);
  }

  /**
   * Execute job now.
   *
   * @param jobToExecute the job to execute
   * @param jobStore the job store
   */
  public synchronized void executeJobNow(TriggerJobDO jobToExecute, JobStore jobStore)
  {
    try {
      executor.execute(createJobRunner(this, jobToExecute));
    } catch (Exception ex) {
      /**
       * @logging
       * @reason Chronos Dispatcher Konnte einen Job nicht starten
       * @action TechAdmin kontaktieren
       */
      GLog.error(GenomeLogCategory.Scheduler, "Failed to start execution job: " + jobToExecute.getJobDefinitionString(),
          new LogExceptionAttribute(ex),
          new LogJobEventAttribute(new JobEventImpl(jobToExecute, null, null, State.SCHEDULED, this)));
      JobResultDO result = new JobResultDO();
      // result.setResultObject(ExceptionUtils.getStackTrace(ex));
      jobStore.jobAborted(jobToExecute, result, ex, this);
    }
  }

  /**
   * Check thread pool exhausted.
   *
   * @param jobId the job id
   * @return true, if successful
   */
  private boolean checkThreadPoolExhausted(final TriggerJobDO jobId)
  {
    if (threadPoolSize == 0) {
      GLog.trace(GenomeLogCategory.Scheduler,
          "scheduler is deactivated and rejecting job. scheduler: " + name + "; job: " + jobId);
      return true;
    }
    if (executor.getActiveCount() >= executor.getCorePoolSize()) {
      if (GLog.isTraceEnabled() == true) {
        GLog.trace(GenomeLogCategory.Scheduler, "Scheduler threadpool full: "
            + getName()
            + "; active: "
            + executor.getActiveCount()
            + "; max: "
            + executor.getCorePoolSize());
      }
      if (log.isDebugEnabled() == true) {
        log.debug("Scheduler threadpool full: "
            + getName()
            + "; active: "
            + executor.getActiveCount()
            + "; max: "
            + executor.getCorePoolSize());
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean isRunning()
  {
    return threadPoolSize > 0;
  }

  /**
   * 
   * @param waitForShutdown -1 == 1 h other milliseconds
   * @return true if all jobs are terminated
   */
  @Override
  public boolean shutdown(final long waitForShutdown)
  {
    long realWait = waitForShutdown;
    if (realWait == -1) {
      realWait = 1000 * 60 * 60; // 1 h
    }
    executor.shutdown();
    if (executor.isTerminated() == true) {
      return true;
    }
    dispatcher.wakeup();

    /**
     * @logging
     * @reason Warten auf Scheduler beim Herunterfahren
     * @action Keine
     */
    GLog.note(GenomeLogCategory.Scheduler, "Wait for unfinished Jobs in Scheduler: " + name);
    try {
      boolean finished = executor.awaitTermination(realWait, TimeUnit.MILLISECONDS);
      /**
       * @logging
       * @reason Beim Herunterfahren sind alle Jobs beendet
       * @action Keine
       */
      GLog.note(GenomeLogCategory.Scheduler, "All Jobs finished in Scheduler: " + name);
      return finished;
    } catch (InterruptedException ex) {
      /**
       * @logging
       * @reason Scheduler wurde mit einer Interruption beendet
       * @action Keine
       */
      GLog.warn(GenomeLogCategory.Scheduler, "Scheduler shotdown with interrupt: " + name);
      return executor.isTerminated();
    }

  }

  /**
   * Check paused scheduler.
   *
   * @return true, if successful
   */
  private boolean checkPausedScheduler()
  {
    if (paused == true) {
      if (System.currentTimeMillis() < nextRuntime) {
        if (GLog.isTraceEnabled()) {
          GLog.trace(GenomeLogCategory.Scheduler, "scheduler will paused. name: "
              + name
              + "; until: "
              + new Date(nextRuntime)
              + "; "
              + (nextRuntime - System.currentTimeMillis())
              + " ms");
        }
        if (log.isDebugEnabled() == true) {
          log.debug("scheduler will paused. name: "
              + name
              + "; until: "
              + new Date(nextRuntime)
              + "; "
              + (nextRuntime - System.currentTimeMillis())
              + " ms");
        }
        return true;
      }
      paused = false;
    }
    return false;
  }

  /**
   * Adjust pool size.
   */
  protected void adjustPoolSize()
  {
    if (executor.getActiveCount() < executor.getMaximumPoolSize()
        && executor.getCorePoolSize() < executor.getMaximumPoolSize()) {

      GLog.note(GenomeLogCategory.Scheduler,
          "Scheduler; Try to reduce pool size to: " + executor.getCorePoolSize() + "; sched: " + name);
      // logging.log(Severity.NOTE, "scheduler " + name + " try to reduce
      // pool size to " + executor.getCorePoolSize());
      executor.setMaximumPoolSize(Math.max(executor.getCorePoolSize(), executor.getMaximumPoolSize()));
    }
  }

  @Override
  public int getFreeJobSlotsCount()
  {
    return executor.getCorePoolSize() - executor.getActiveCount();
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public synchronized void resume()
  {
    paused = false;
    nextRuntime = System.currentTimeMillis() - 1000;
    if (GLog.isInfoEnabled() == true) {
      GLog.info(GenomeLogCategory.Scheduler, "Scheduler resumed: " + this);
    }
  }

  @Override
  public synchronized void suspend()
  {
    paused = true;
    nextRuntime = Long.MAX_VALUE;
    if (GLog.isInfoEnabled() == true) {
      GLog.info(GenomeLogCategory.Scheduler, "Scheduler suspended: " + this);
    }
  }

  @Override
  public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor)
  {
    GLog.warn(GenomeLogCategory.Scheduler, "Executor reject execution: " + this);
    throw new UnsupportedOperationException();
  }

  @Override
  public int getServiceRetryTime()
  {
    return serviceRetryTime;
  }

  @Override
  public void setServiceRetryTime(final int serviceRetryTime)
  {
    this.serviceRetryTime = serviceRetryTime;
  }

  @Override
  public int getJobRetryTime()
  {
    return jobRetryTime;
  }

  @Override
  public void setJobRetryTime(final int jobRetryTime)
  {
    this.jobRetryTime = jobRetryTime;
  }

  @Override
  public synchronized void pause(final int seconds)
  {
    paused = true;
    nextRuntime = System.currentTimeMillis() + (1000L * seconds);
  }

  @Override
  public long getId()
  {
    return schedulerId;
  }

  @Override
  public String toString()
  {
    if (executor == null) {
      return "Scheduler[<uninitialized>]";
    }
    return "Scheduler["
        + name
        + "] with threadPoolSize="
        + executor.getCorePoolSize()
        + " id="
        + schedulerId
        + " active="
        + executor.getActiveCount()
        + " total="
        + executor.getCompletedTaskCount()
        + " waiting="
        + executor.getQueue().size();
  }

  @Override
  public Stats getSchedulerStats()
  {
    Stats s = new Stats();
    s.poolSize = executor.getCorePoolSize();
    s.poolActive = executor.getActiveCount();
    s.poolCompleted = executor.getCompletedTaskCount();
    s.poolWaiting = executor.getQueue().size();
    s.poolTaskCount = executor.getTaskCount();
    return s;
  }

  public String getVirtualHostName()
  {
    return dispatcher.getVirtualHostName();
  }

  public Scheduler getScheduler()
  {
    return this;
  }

  public long getSchedulerId()
  {
    return schedulerId;
  }

  @Override
  public void setSchedulerId(long schedulerId)
  {
    this.schedulerId = schedulerId;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public int getJobMaxRetryCount()
  {
    return jobMaxRetryCount;
  }

  public void setJobMaxRetryCount(int jobMaxRetryCount)
  {
    this.jobMaxRetryCount = jobMaxRetryCount;
  }

  @Override
  public Dispatcher getDispatcher()
  {
    return dispatcher;
  }

  @Override
  public int getNodeBindingTimeout()
  {
    return nodeBindingTimeout;
  }

  @Override
  public void setNodeBindingTimeout(int nodeBindingTimeout)
  {
    this.nodeBindingTimeout = nodeBindingTimeout;
  }

}