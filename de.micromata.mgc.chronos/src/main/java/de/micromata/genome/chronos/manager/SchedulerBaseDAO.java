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

package de.micromata.genome.chronos.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.spi.DispatcherImpl2;
import de.micromata.genome.chronos.spi.JobRunner;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;
import de.micromata.genome.chronos.util.SchedulerFactory;
import de.micromata.genome.chronos.util.TriggerJobUtils;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.CallableX;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.text.PipeValueList;

/**
 * Scheduler Service.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class SchedulerBaseDAO implements SchedulerDAO
{

  /**
   * Must be static.
   */
  private static Dispatcher dispatcher;

  /**
   * Get the global Scheduler manager.
   * 
   * @return the {@link SchedulerManager}
   */
  @Override
  public SchedulerManager getSchedulerManager()
  {
    return ChronosServiceManager.get().getChronosConfigurationService().getScheduleManager();
  }

  /**
   * Creates the filter chain.
   *
   * @param jobRunner the job runner
   * @return the job filter chain
   */
  private JobFilterChain createFilterChain(JobRunner jobRunner)
  {

    JobFilterChain fc = new JobFilterChain();
    fc.setJobRunner(jobRunner);
    fc.setSchedulerDAO(this);
    SchedulerManager sm = SchedulerManager.get();
    if (sm != null) {
      List<JobRunnerFilter> filters = sm.getFilters(jobRunner.getScheduler().getName());
      fc.setFilters(filters);
    }
    return fc;
  }

  protected Dispatcher createDispatcher(String virtualHostName, JobStore jobStore)
  {
    return new DispatcherImpl2(virtualHostName, jobStore);
  }

  @Override
  public Object filterJobRun(JobRunner jobRunner) throws Exception
  {
    JobFilterChain chain = createFilterChain(jobRunner);
    return chain.doFilter();
  }

  @Override
  public Object invokeJob(final JobRunner jobRunner) throws Exception
  {

    final String jobName = jobRunner.getJob().getClass().getSimpleName();
    return LoggingServiceManager.get().getStatsDAO().runLongRunningOp(GenomeLogCategory.Scheduler, jobName,
        jobRunner.getJob(),
        new CallableX<Object, Exception>()
        {
          @Override
          public Object call() throws Exception
          {
            // Folgende Zeile baut ein neues FutureJob-Objekt
            FutureJob fjob = jobRunner.getJob().getExecutor();
            jobRunner.setFutureJob(fjob);
            final Object result = fjob.call(jobRunner.getJob().getJobArguments());
            return result;
          }
        });
  }

  @Override
  public JobStore getJobStore()
  {
    return getDispatcher().getJobStore();
  }

  @Override
  public List<TriggerJobDisplayDO> getAdminJobs(final String hostName, final String jobName, final String state,
      final String schedulerName,
      final int resultCount)

  {
    return getJobStore().getAdminJobs(hostName, jobName, state, schedulerName, resultCount);
  }

  @Override
  public TriggerJobDO getJobByPk(long pk)
  {
    return getJobStore().getJobByPk(pk);
  }

  @Override
  public List<TriggerJobDO> getNextJobs(long minNodeBindTimeout)
  {
    return getJobStore().getNextJobs(minNodeBindTimeout);
  }

  @Override
  public List<SchedulerDO> getSchedulers()
  {
    return getJobStore().getSchedulers();
  }

  /**
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public SchedulerDO createOrGetPersistScheduler(String schedulerName)
  {
    return getJobStore().createOrGetScheduler(schedulerName);
  }

  @Override
  public Scheduler getCreateScheduler(String schedulerName, boolean createByDefault)
  {
    Scheduler sched = getDispatcher().getScheduler(schedulerName);

    if (sched != null) {
      return sched;
    }

    if (createByDefault == false) {
      return null;
    }
    // we have to create a new one
    SchedulerDO schedulerDO = new SchedulerDO();
    schedulerDO.setName(schedulerName);
    schedulerDO.setThreadPoolSize(1);
    schedulerDO.setServiceRetryTime(60);
    schedulerDO.setJobRetryTime(30);
    schedulerDO.setNodeBindingTimeout(0);
    sched = getDispatcher().createOrGetScheduler(schedulerDO);
    return sched;
  }

  @Override
  public Scheduler getScheduler(String name)
  {
    return getCreateScheduler(name, true);
  }

  @Override
  public Scheduler getSchedulerByPk(Long pk)
  {
    return getJobStore().getSchedulerByPk(pk);
  }

  @Override
  public void persist(SchedulerDO scheduler)
  {
    Validate.notNull(scheduler, "Der Scheduler ist null");
    Validate.notNull(scheduler.getName(), "scheduler.name ist null");

    final String name = scheduler.getName();
    SchedulerDO si = createOrGetPersistScheduler(name);
    if (si != null) {
      scheduler.setPk(si.getPk());
    } else {
      scheduler.setPk(SchedulerDO.UNSAVED_SCHEDULER_ID);
    }
    /**
     * @logging
     * @reason Chronos Scheduler wird in die DB geschrieben
     * @action Keine
     */
    GLog.note(GenomeLogCategory.Scheduler, "Persist Scheduler: " + scheduler);
    getJobStore().persist(scheduler);
  }

  @Override
  public void denyNewJobs(final String schedulerName)
  {
    final Scheduler scheduler = getScheduler(schedulerName);
    if (scheduler != null) {
      SchedulerDO sched = scheduler.getDO();
      scheduler.setThreadPoolSize(0);
      sched.setThreadPoolSize(0);
      persist(sched);
    }
  }

  @Override
  public void setJobCount(final int size, final String schedulerName)
  {
    final Scheduler scheduler = getScheduler(schedulerName);
    if (scheduler != null) {
      SchedulerDO sched = scheduler.getDO();
      scheduler.setThreadPoolSize(size);
      sched.setThreadPoolSize(size);
      persist(sched);
    }
  }

  @Override
  public TriggerJobDO buildTriggerJob(final Scheduler scheduler, String jobName, final JobDefinition jobDefinition,
      final Object info,
      final Trigger trigger, final String hostName, State state)
  {
    return getJobStore().buildTriggerJob(scheduler, jobName, jobDefinition, info, trigger, hostName, state);
  }

  @Override
  public long submit(final String schedulerName, String jobName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger)
  {
    return getDispatcher().submit(schedulerName, jobName, jobDefinition, arg, trigger, null);
  }

  @Override
  public long submit(final String schedulerName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger,
      String hostName)
  {
    return submit(schedulerName, null, jobDefinition, arg, trigger, hostName);
  }

  @Override
  public long submit(final String schedulerName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger)
  {
    return submit(schedulerName, null, jobDefinition, arg, trigger);
  }

  @Override
  public long submit(final String schedulerName, String jobName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger, String hostName)
  {
    return getDispatcher().submit(schedulerName, jobName, jobDefinition, arg, trigger, hostName);
  }

  @Override
  public long submitStdAdminJob(String jobId, final Map<String, String> args, Trigger trigger)
  {
    JobBeanDefinition jdef = SchedulerManager.get().getJobDefinition(jobId);
    Trigger tr = (trigger != null ? trigger : TriggerJobDO.parseTrigger(jdef.getTriggerDefinition()));
    String sarg = PipeValueList.encode(args);
    return submit(jdef.getSchedulerName(), jdef.getJobName(), jdef.getJobDefinition(), sarg, tr);
  }

  @Override
  public long submitStdAdminJob(String jobId, final Map<String, String> args)
  {
    JobBeanDefinition jdef = SchedulerManager.get().getJobDefinition(jobId);
    Trigger trigger = TriggerJobDO.parseTrigger(jdef.getTriggerDefinition());
    String sarg = PipeValueList.encode(args);
    return submit(jdef.getSchedulerName(), jdef.getJobName(), jdef.getJobDefinition(), sarg, trigger);
  }

  @Override
  public long submitStdJob(String jobId, final Object arg)
  {
    return submitStdJob(jobId, arg, null);
  }

  @Override
  public long submitStdJob(String jobId, final Object arg, Trigger trigger)
  {
    JobBeanDefinition jdef = SchedulerManager.get().getJobDefinition(jobId);
    Trigger tr = trigger != null ? trigger : TriggerJobDO.parseTrigger(jdef.getTriggerDefinition());
    return submit(jdef.getSchedulerName(), jdef.getJobName(), jdef.getJobDefinition(), arg, tr);
  }

  @Override
  public List<String> getUniqueJobNames()
  {
    return getJobStore().getUniqueJobNames();
  }

  @Override
  public TriggerJobDO getAdminJobByPk(final long pk)
  {
    return getJobStore().getAdminJobByPk(pk);
  }

  @Override
  public List<SchedulerDisplayDO> getAdminSchedulers()
  {
    return getJobStore().getAdminSchedulers();
  }

  @Override
  public Dispatcher getDispatcher()
  {
    if (dispatcher == null) {
      synchronized (this) {
        if (dispatcher != null) {
          return dispatcher;
        }
        init(SchedulerManager.get());
      }
    }
    return dispatcher;
  }

  @Override
  public List<JobResultDO> getResultsForJob(long jobId)
  {
    return getJobStore().getResultsForJob(jobId);
  }

  @Override
  public void updateJob(TriggerJobDO job)
  {
    getJobStore().updateJob(job);
  }

  @Override
  public void addToReservedIfNessary(TriggerJobDO job)
  {
    getDispatcher().addToReservedIfNessary(job);
  }

  @Override
  public int setJobState(long pk, String newState, String oldState)
  {
    int modcount = getJobStore().setJobState(pk, newState, oldState);
    if (modcount == 0) {
      return 0;
    }
    if (State.WAIT.name().equals(newState) == true) {
      TriggerJobDO job = getJobStore().getJobByPk(pk);
      addToReservedIfNessary(job);
    }
    return modcount;
  }

  @Override
  public void initialize()
  {
    try {
      getSchedulerManager();
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      GLog.error(GenomeLogCategory.Scheduler, "Cannot initialize SchedulerManager: " + ex.getMessage(),
          new LogExceptionAttribute(ex));
    }
  }

  @Override
  public void init(SchedulerManager manager)
  {
    if (dispatcher != null) {
      dispatcher.setMinNodeBindTime(manager.getMinNodeBindTime());
      return;
    }

    try {
      dispatcher = createDispatcher(manager.getVirtualHostName());
      dispatcher.setMinNodeBindTime(manager.getMinNodeBindTime());
      createStdSchedulers(manager);
      dispatcher.startup();
      checkBrokenJobs(manager); // nach stehengebliebenen Jobs suchen
      submitStartupJobs(manager);
      checkStandardJobs(manager);
    } catch (Exception ex) {
      /**
       * @logging
       * @reason Ein Fehler beim Initialisieren des Schedulers ist aufgetreten
       * @action Datenbankverbindung ueberpruefen
       */
      throw new LoggedRuntimeException(ex, LogLevel.Fatal, GenomeLogCategory.Scheduler, "Cannot initialize JobStore");
    }
  }

  /**
   * Check standard jobs.
   *
   * @param manager the manager
   */
  private void checkStandardJobs(SchedulerManager manager)
  {
    LocalSettings ls = LocalSettings.get();
    if (ls.getBooleanValue("genome.chronos.skipStandardJobs", false) == true) {
      return;
    }

    for (JobBeanDefinition jbd : manager.getStandardJobs()) {
      submitStandardJob(jbd, true);
    }
  }

  /**
   * Submit standard job.
   *
   * @param jbd the jbd
   * @param checkExistant the check existant
   */
  private void submitStandardJob(JobBeanDefinition jbd, boolean checkExistant)
  {
    String jobname = jbd.getJobName();
    String scheduleName = jbd.getSchedulerName();
    boolean perNode = jbd.isOnePerNode();
    String curHostName = null;
    if (perNode == true) {
      curHostName = getDispatcher().getVirtualHostName();
    }
    if (checkExistant == true) {
      List<? extends TriggerJobDO> existantJobs = getJobStore().findJobs(curHostName, jobname, null, scheduleName, 1);
      if (existantJobs.isEmpty() == false) {
        return;
      }
    }
    Trigger trigger = TriggerJobUtils.createTriggerDefinition(jbd.getTriggerDefinition());
    submit(scheduleName, jobname, jbd.getJobDefinition(), jbd.getJobArgument(), trigger);
  }

  /**
   * Submit startup jobs.
   *
   * @param manager the manager
   */
  private void submitStartupJobs(SchedulerManager manager)
  {
    LocalSettings ls = LocalSettings.get();
    if (ls.getBooleanValue("genome.chronos.skipStartupJobs", false) == true) {
      return;
    }
    for (JobBeanDefinition jbd : manager.getStartupJobs()) {
      submitStandardJob(jbd, false);
    }
  }

  /**
   * Creates the std schedulers.
   *
   * @param manager the manager
   */
  protected void createStdSchedulers(SchedulerManager manager)
  {
    for (SchedulerFactory sf : manager.getScheduleFactories()) {
      sf.setDispatcher(this.getDispatcher());
      if (dispatcher.getScheduler(sf.getSchedulerName()) == null) {
        sf.create(this.getDispatcher().getJobStore());
      }
    }
  }

  /**
   * Check broken jobs.
   *
   * @param manager the manager
   */
  protected void checkBrokenJobs(SchedulerManager manager)
  {
    if (manager.isRestartOwnJobsOnBooting() == false) {
      return;
    }
    final String hostName = dispatcher.getVirtualHostName();
    final JobStore jobStore = dispatcher.getJobStore();
    List<TriggerJobDO> jobl = new ArrayList<>();
    jobl.addAll(jobStore.findJobs(hostName, null, State.SCHEDULED.name(), null, 1000)); // alle meine SCHEDULED Jobs
    jobl.addAll(jobStore.findJobs(hostName, null, State.RUN.name(), null, 1000)); // alle meine RUN Jobs

    // alle gesammelten Jobs zur Ausfuehrung freigeben
    long now = System.currentTimeMillis();
    for (TriggerJobDO job : jobl) {
      long mod = job.getModifiedAt().getTime();
      long dif = now - mod;
      if (dif > manager.getRestartOwnJobTimeoutInMillis()) {
        /**
         * @logging
         * @reason Starte einen Job neu, der bei seiner letzten Ausfuehrung unterbrochen wurde.
         * @action Keine
         */
        GLog.warn(GenomeLogCategory.Scheduler, "Restarted Job after application restart. JobPk: " + job.getPk());
        jobStore.setJobState(job.getPk(), State.WAIT.name(), job.getState().name());
      } else {
        /**
         * @logging
         * @reason Starte einen Job Nicht neu, der beim letzten Lauf stehen geblieben ist, da modifiedat zu jung ist.
         * @action Keine
         */
        GLog.warn(GenomeLogCategory.Scheduler, "Did not restart Job after application restart, as its modification "
            + "timestamp is yet too recent. JobPk: "
            + job.getPk());
      }
    }
  }

  @Override
  public void shutdown()
  {
    SchedulerManager manager = SchedulerManager.get();
    if (manager != null) {
      try {
        getDispatcher().shutdown();
      } catch (InterruptedException ex) {
        // nothing
      }
    }
  }

  @Override
  public void restart()
  {
    SchedulerManager manager = SchedulerManager.get();
    if (manager != null) {
      getDispatcher().startup();
    }
  }
  /**
   * Use only in test cases
   */
  public static void _resetDispatcher()
  {
    dispatcher = null;
  }

  @Override
  public String getShortApplicationName()
  {
    return "MGC";
  }
}
