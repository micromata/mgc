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

import java.util.Date;
import java.util.List;

import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;

/**
 * Zentrales Interface für die zu speichernden Jobs. Jeder JobStore muss zu Beginn der Applikation einmalig initialisert werden damit der
 * {@link Dispatcher} seine Arbeit verrichten kann.
 * 
 * @author Roger Rene Kommer, Wolfgang Jung (w.jung@micromata.de)
 * 
 */
public interface JobStore
{
  /**
   * Prefix für die Job/Schedulernamen um mehrerer JobStores auf einer gemeinsamen betreiben zu können.
   * 
   * @param prefix Der Prefix, muss nicht notwendigerweise gesetzt sein. In einer Mehrbenutzerentwicklerdatenbank sollte dies der User sein.
   */
  Dispatcher getDispatcher();

  /**
   * Sets the dispatcher.
   *
   * @param dispatcher the new dispatcher
   */
  void setDispatcher(Dispatcher dispatcher);

  /**
   * Gets the schedulers.
   *
   * @return the schedulers
   */
  List<SchedulerDO> getSchedulers();

  /**
   * Liefert eine Sequenz fuer eine JobID (PK)
   * 
   * @return the next id
   */
  long getNextJobId();

  /**
   * Liefert eine Sequenz fuer eine Scheduler (PK)
   * 
   * @return the next id in the scheduler
   */
  long getNextSchedulerId();

  long getNextJobResultId();

  /**
   * Build from the parameters a new TriggerJobDO, but does not safe the the job.
   * 
   * The PK of the job will already be set, although the job itself is not inserted into db
   *
   * @param scheduler the scheduler
   * @param jobDefinition the job definition
   * @param info the info
   * @param trigger the trigger
   * @param hostName the host name
   * @param state the state
   * @return the trigger job do
   */
  TriggerJobDO buildTriggerJob(final Scheduler scheduler, final JobDefinition jobDefinition, final Object info,
      final Trigger trigger, final String hostName, State state);

  /**
   * Build from the parameters a new TriggerJobDO, but does not safe the the job.
   * 
   * The PK of the job will already be set, although the job itself is not inserted into db
   *
   * @param scheduler the scheduler
   * @param jobName the job name
   * @param jobDefinition the job definition
   * @param info the info
   * @param trigger the trigger
   * @param hostName the host name
   * @param state the state
   * @return the trigger job do
   */
  TriggerJobDO buildTriggerJob(final Scheduler scheduler, String jobName, final JobDefinition jobDefinition, final Object info,
      final Trigger trigger, final String hostName, State state);

  /**
   * Inserts a new Job.
   *
   * @param job the job
   */
  void insertJob(TriggerJobDO job);

  /**
   * Update job.
   *
   * @param job the job
   */
  void updateJob(TriggerJobDO job);

  /**
   * Update the job with given result.
   *
   * @param job the job
   * @param jobResult the job result
   */
  void updateJobWithResult(TriggerJobDO job, JobResultDO jobResult);

  /**
   * Insert result.
   *
   * @param result the result
   */
  void insertResult(JobResultDO result);

  /**
   * Gibt maximal {@link Scheduler} Jobs des gegebenen Schedulers zurück, die im State Wait sind.
   *
   * @param scheduler the scheduler
   * @param foreignJobs Sollen auch Jobs, mit einer anderen Node gesucht werden?
   * @return the next jobs
   */
  List<TriggerJobDO> getNextJobs(Scheduler scheduler, boolean foreignJobs);

  /**
   * Alternative implementation for getting jobs to run for all schedulers.
   *
   * @param minNodeBindTimeout (or backward) in milliseconds relativ to nextFireTime
   * @return the next jobs
   */
  List<TriggerJobDO> getNextJobs(long minNodeBindTimeout);

  /**
   * Gibt alle Jobs mit dem Status zurueck.
   * 
   * ACHTUNG wird nur in Tests verwendet.
   * 
   * @param scheduler kann null sein.
   * @param fromDate bezieht sich auf modifiedat kann null sein
   * @param untilDate bezieht sich auf modifiedat kann null sein
   * @param state bezieht sich auf modifiedat kann null sein
   * @return List of Jobs
   */
  List<TriggerJobDO> getJobs(final Scheduler scheduler, final Date fromDate, final Date untilDate, final State state);

  /**
   * Gibt einen Scheduler aus der Datenbank zurück.
   * <p>
   * Ist er noch nicht existent, so wird er erzeugt und ein vorbefülltes DO zurück gegeben.
   * </p>
   *
   * @param schedulerName the scheduler name
   * @return the scheduler do
   */
  SchedulerDO createOrGetScheduler(String schedulerName);

  /**
   * Gets the scheduler by pk.
   *
   * @param pk the pk
   * @return the scheduler by pk
   */
  Scheduler getSchedulerByPk(Long pk);

  /**
   * Submit.
   *
   * @param scheduler the scheduler
   * @param jobName the job name
   * @param executor the executor
   * @param info the info
   * @param trigger the trigger
   * @param hostName the host name
   * @param state the state
   * @return the trigger job do
   */
  TriggerJobDO submit(Scheduler scheduler, String jobName, JobDefinition executor, Object info, Trigger trigger, String hostName,
      State state);

  // public int getThreadPoolSize(Scheduler scheduler);

  /**
   * Service retry.
   *
   * @param job the job
   * @param resultInfo the result info
   * @param ex the ex
   * @param Scheduler the scheduler
   */
  void serviceRetry(TriggerJobDO job, JobResultDO resultInfo, ServiceUnavailableException ex, Scheduler Scheduler);

  /**
   * Job started.
   *
   * @param job the job
   * @param scheduler the scheduler
   * @return the job result do
   */
  JobResultDO jobStarted(TriggerJobDO job, Scheduler scheduler);

  /**
   * Job retry.
   *
   * @param job the job
   * @param resultInfo the result info
   * @param ex the ex
   * @param Scheduler the scheduler
   */
  void jobRetry(TriggerJobDO job, JobResultDO resultInfo, Exception ex, Scheduler Scheduler);

  /**
   * Job completed.
   *
   * @param job the job
   * @param jobResult the job result
   * @param result the result
   * @param scheduler the scheduler
   * @param nextRun the next run
   */
  void jobCompleted(TriggerJobDO job, JobResultDO jobResult, Object result, Scheduler scheduler, Date nextRun);

  /**
   * Aborts job. Set Jobresult and state State.STOP
   *
   * @param job the job
   * @param jobResult the job result
   * @param ex the ex
   * @param scheduler the scheduler
   */
  void jobAborted(TriggerJobDO job, JobResultDO jobResult, Throwable ex, Scheduler scheduler);

  /**
   * Loescht job, aber nur wenn kein JobResult vorhanden ist.
   *
   * @param job the job
   * @param jobResult may be null
   * @param scheduler the scheduler
   */
  void jobRemove(TriggerJobDO job, JobResultDO jobResult, Scheduler scheduler);

  /**
   * Job result remove.
   *
   * @param job the job
   * @param jobResult the job result
   * @param scheduler the scheduler
   */
  void jobResultRemove(TriggerJobDO job, JobResultDO jobResult, Scheduler scheduler);

  /**
   * Gets the results.
   *
   * @param impl the impl
   * @param maxResults the max results
   * @return the results
   */
  List<JobResultDO> getResults(TriggerJobDO impl, int maxResults);

  /**
   * Shutdown.
   *
   * @throws InterruptedException the interrupted exception
   */
  void shutdown() throws InterruptedException;

  // public boolean setInactiveTriggerActive(Trigger trigger, Job job);
  /**
   * Versucht den Job zu reservieren.
   * <p>
   * Liefert den Job und setzt ihn auf WAIT auf ACTIVE. Dies muss eine atomare Operation sein.
   * </p>
   *
   * @param job the job
   * @return the trigger job do
   */
  TriggerJobDO reserveJob(TriggerJobDO job);

  /**
   * Persist.
   *
   * @param scheduler the scheduler
   */
  void persist(SchedulerDO scheduler);

  /**
   * Within transaction.
   *
   * @param runnable the runnable
   */
  void withinTransaction(final Runnable runnable);

  /**
   * mapping getAdminJobById.
   *
   * @param pk the pk
   * @return the admin job by pk
   */
  TriggerJobDO getAdminJobByPk(long pk);

  /**
   * getJob.
   *
   * @param pk the pk
   * @return the job by pk
   */
  TriggerJobDO getJobByPk(long pk);

  /**
   * ibatis: setJobState
   * 
   * Update the state of given Job.
   *
   * @param pk the pk
   * @param newState the new state
   * @param oldState the old state
   * @return 0 if stored state is not oldState
   */
  int setJobState(long pk, String newState, String oldState);

  /**
   * Gets the admin jobs.
   *
   * @param hostName may be null
   * @param jobName the job name
   * @param state may be null. In this case all Jobs with state != 'CLOSED' are returned
   * @param schedulerName may be null
   * @param resultCount the result count
   * @return the admin jobs
   */
  List<TriggerJobDisplayDO> getAdminJobs(String hostName, String jobName, String state, String schedulerName, int resultCount);

  /**
   * Gets the admin jobs.
   *
   * @param hostName may be null
   * @param state may be null. In this case all Jobs with state != 'CLOSED' are returned
   * @param schedulerName may be null
   * @param resultCount the result count
   * @return the admin jobs
   */
  List<TriggerJobDisplayDO> getAdminJobs(String hostName, String state, String schedulerName, int resultCount);

  /**
   * Gets the admin jobs.
   *
   * @param hostName the host name
   * @param jobName the job name
   * @param state the state
   * @param schedulerName the scheduler name
   * @param resultCount the result count
   * @param withLastResult the with last result
   * @return the admin jobs
   */
  List<TriggerJobDisplayDO> getAdminJobs(String hostName, String jobName, String state, String schedulerName,
      int resultCount,
      boolean withLastResult);

  /**
   * Find trigger jobs.
   *
   * @param hostName the host name
   * @param jobName the job name
   * @param state the state
   * @param schedulerName the scheduler name
   * @param resultCount the result count
   * @return the list<? extends trigger job d o>
   */
  List< ? extends TriggerJobDO> findJobs(String hostName, String jobName, String state, String schedulerName,
      int resultCount);

  /**
   * ibatis: getAdminJobResults.
   *
   * @param jobId the job id
   * @return the results for job
   */
  List<JobResultDO> getResultsForJob(long jobId);

  /**
   * Return the job result for given pk.
   *
   * @param resultId the result id
   * @return the result by pk
   */
  JobResultDO getResultByPk(long resultId);

  List<SchedulerDisplayDO> getAdminSchedulers();

  /**
   * Return number of jobs with given state.
   *
   * @param state if state == null, return all
   * @return the job count
   */
  long getJobCount(State state);

  /**
   * return number of all job results with given state inside the jobresult, not associated job.
   * 
   * ATTENTION Only used for tests.
   *
   * @param state the state
   * @return the job result count
   */
  long getJobResultCount(State state);

  /**
   * Löscht den angegebenen Scheduler.
   *
   * @param pk the pk
   */
  void deleteScheduler(Long pk);

  /**
   * Delete a Job with optional JobResults.
   * 
   * @param pk pk of the job to delete.
   * @return true if job was deleted
   */
  boolean deleteJobWithResults(Long pk);

  List<String> getJobNames();

  List<String> getUniqueJobNames();
}
