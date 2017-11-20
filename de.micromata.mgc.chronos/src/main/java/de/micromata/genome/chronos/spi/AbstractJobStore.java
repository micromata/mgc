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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.ServiceUnavailableException;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;
import de.micromata.genome.util.types.Holder;

/**
 * A job store is asigned to a dispacher.
 *
 * @author roger
 */
public abstract class AbstractJobStore implements JobStore
{

  /**
   * The dispatcher.
   */
  private Dispatcher dispatcher;

  /**
   * Instantiates a new abstract job store.
   */
  public AbstractJobStore()
  {

  }

  @Override
  public TriggerJobDO buildTriggerJob(final Scheduler scheduler, String jobName, final JobDefinition jobDefinition,
      final Object info,
      final Trigger trigger, final String hostName, State state)
  {
    final long schedulerId = scheduler.getId();
    Validate.isTrue(schedulerId != SchedulerDO.UNSAVED_SCHEDULER_ID,
        "Es wurde submit auf einen nicht persistenten Scheduler versucht.");
    //    long jobId = getNextJobId();
    TriggerJobDO job = new TriggerJobDO();
    job.setTrigger(trigger);
    job.setFireTime(trigger.getNextFireTime(new Date()));
    job.setJobName(jobName);
    job.setJobDefinition(jobDefinition);
    job.setJobArguments(info);
    job.setJobStore(this);
    //    job.setPk(jobId);
    job.setHostName(hostName);
    if (state == null) {
      state = State.WAIT;
    }
    job.setState(state);

    job.setScheduler(scheduler.getId());
    return job;
  }

  @Override
  public TriggerJobDO buildTriggerJob(final Scheduler scheduler, final JobDefinition jobDefinition, final Object info,
      final Trigger trigger, final String hostName, State state)
  {

    return buildTriggerJob(scheduler, null, jobDefinition, info, trigger, hostName, state);
  }

  /**
   * Fügt den einen neuen Job für den Scheduler ein und persistiert ihn.
   * 
   * @see de.micromata.jchronos.JobStore#submit(de.micromata.jchronos.spi. Scheduler de.micromata.jchronos.JobDefinition, java.lang.Object,
   *      de.micromata.jchronos.Trigger)
   */
  @Override
  public TriggerJobDO submit(final Scheduler scheduler, String jobName, final JobDefinition jobDefinition,
      final Object info,
      final Trigger trigger, final String hostName, State state)
  {
    TriggerJobDO job = buildTriggerJob(scheduler, jobName, jobDefinition, info, trigger, hostName, state);
    insertJob(job);
    return job;
  }

  @Override
  public void serviceRetry(final TriggerJobDO job, final JobResultDO jobResult, final ServiceUnavailableException ex,
      final Scheduler scheduler)
  {
    withinTransaction(new Runnable() {
      @Override
      public void run()
      {
        job.setState(State.WAIT);
        //        job.setResult(jobResult);
        jobResult.setResultObject(exceptionToResultObject(ex));
        jobResult.setState(State.RETRY);
        updateJobWithResult(job, jobResult);
      }
    });
  }

  @Override
  public JobResultDO jobStarted(final TriggerJobDO job, final Scheduler scheduler)
  {
    final Holder<JobResultDO> ret = new Holder<JobResultDO>();

    withinTransaction(new Runnable() {
      @Override
      public void run()
      {

        job.setState(State.RUN);
        Dispatcher disp = scheduler.getDispatcher();// Dispatcher.getInstance();

        job.setHostName(disp.getVirtualHostName());
        JobResultDO result = new JobResultDO();
        result.setExecutionStart(new Date());
        result.setHostName(disp.getVirtualHostName());
        updateJob(job);
        ret.set(result);
      }
    });
    return ret.get();
  }

  /**
   * Exception to result object.
   *
   * @param ex the ex
   * @return the object
   */
  protected Object exceptionToResultObject(Throwable ex)
  {
    return ExceptionUtils.getStackTrace(ex);
  }

  @Override
  public void jobAborted(final TriggerJobDO job, final JobResultDO jobResult, final Throwable ex,
      final Scheduler scheduler)
  {
    withinTransaction(new Runnable() {
      @Override
      public void run()
      {
        job.setState(State.STOP);
        //        job.setResult(jobResult);
        jobResult.setResultObject(exceptionToResultObject(ex));
        jobResult.setState(State.STOP);
        updateJobWithResult(job, jobResult);

      }
    });
  }

  @Override
  public void jobCompleted(final TriggerJobDO job, final JobResultDO jobResult, final Object result,
      final Scheduler scheduler,
      final Date nextRun)
  {

    withinTransaction(new Runnable() {
      @Override
      public void run()
      {
        if (nextRun != null) {
          job.setFireTime(nextRun);
          job.setState(State.WAIT);
          job.setRetryCount(0);
        } else {
          job.setState(State.FINISHED);
        }
        if (result == null) {
          if (nextRun == null && job.hasFailureResult() == false) {
            jobRemove(job, jobResult, scheduler);
          } else {
            updateJob(job);
          }
        } else {
          jobResult.setResultObject(result);
          //          (job).setResult(jobResult);
          updateJobWithResult(job, jobResult);
        }

      }
    });
  }

  /**
   * Setzt die Daten für einem neuen Versuch und speichert den Job ab.
   * 
   * @see de.micromata.genome.chronos.JobStore#jobRetry(de.micromata.genome.chronos.spi.jdbc.TriggerJobDO,
   *      de.micromata.genome.chronos.spi.jdbc.JobResultDO, de.micromata.genome.chronos.RetryException,
   *      de.micromata.genome.chronos.spi.Scheduler)
   */
  @Override
  public void jobRetry(final TriggerJobDO job, final JobResultDO jobResult, final Exception ex,
      final Scheduler scheduler)
  {
    withinTransaction(new Runnable() {
      @Override
      public void run()
      {
        job.setState(State.WAIT);
        //        job.setResult(jobResult);
        jobResult.setResultObject(exceptionToResultObject(ex));
        jobResult.setState(State.RETRY);
        job.increaseRetryCount();
        updateJobWithResult(job, jobResult);
      }
    });
  }

  /**
   * Speichert den Job ab und, wenn ungleich <code>null</code>, auch das {@link JobResultDO}.
   */
  @Override
  public void updateJobWithResult(final TriggerJobDO job, JobResultDO jobResult)
  {
    if (jobResult != null) {
      jobResult.setJobPk(job.getPk());
      // job.getResult().setJobName(job.getJobName());
      if (jobResult.getState() == null) {
        jobResult.setState(job.getState());
      }
      insertResult(jobResult);
      job.setCurrentResultPk(jobResult.getPk());
    }
    updateJob(job);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<TriggerJobDisplayDO> getAdminJobs(String hostName, String state, String schedulerName, int resultCount)
  {
    return getAdminJobs(hostName, null, state, schedulerName, resultCount);
  }

  @Override
  public List<TriggerJobDisplayDO> getAdminJobs(String hostName, String jobName, String state, String schedulerName, int resultCount)
  {
    return getAdminJobs(hostName, jobName, state, schedulerName, resultCount, true);
  }

  @Override
  public List<String> getUniqueJobNames()
  {
    Set<String> jobNames = new HashSet<String>(getJobNames());
    return new ArrayList<String>(jobNames);
  }

  @Override
  public Scheduler getSchedulerByPk(Long pk)
  {
    return getDispatcher().getSchedulerByPk(pk);
  }

  @Override
  public List< ? extends TriggerJobDO> findJobs(String hostName, String jobName, String state, String schedulerName, int resultCount)
  {
    return getAdminJobs(hostName, jobName, state, schedulerName, resultCount);
  }

  @Override
  public Dispatcher getDispatcher()
  {
    return dispatcher;
  }

  @Override
  public void setDispatcher(Dispatcher dispatcher)
  {
    this.dispatcher = dispatcher;
  }
}
