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

import org.apache.log4j.Logger;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.ForceRetryException;
import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.FutureJobStatusListener;
import de.micromata.genome.chronos.JobAbortException;
import de.micromata.genome.chronos.JobCompletion;
import de.micromata.genome.chronos.JobRetryException;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.RetryNextRunException;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.ServiceUnavailableException;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.manager.LogJobEventAttribute;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeRuntimeException;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LoggingContext;

/**
 * Ausführungsobjekt, welches das wirkliche Runtime-Job-Objekt erzeugt, startet und die Fehlerbehandlung tätigt.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JobRunner implements Runnable
{

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(JobRunner.class);

  /**
   * The scheduler.
   */
  private Scheduler scheduler;

  /**
   * The job.
   */
  private TriggerJobDO job;

  /**
   * The job store.
   */
  private JobStore jobStore;

  /**
   * The future job.
   */
  private FutureJob futureJob;

  /**
   * Instantiates a new job runner.
   *
   * @param scheduler the scheduler
   * @param job the job
   */
  public JobRunner(final Scheduler scheduler, final TriggerJobDO job)
  {
    this.scheduler = scheduler;
    this.job = job;

    this.jobStore = scheduler.getDispatcher().getJobStore();

    if (GLog.isTraceEnabled() == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "New JobRunner",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null,
              null, scheduler)));
    }
  }

  /**
   * Falls es eine LogAttributeException vorhanden ist, nimm es. Sonst die originalle Exception
   *
   * @param ex the ex
   * @return the log exception attribute
   */
  protected LogAttribute getLogExceptionAttribute(LogAttributeRuntimeException ex)
  {
    if (ex.getLogAttributeMap().containsKey(GenomeAttributeType.TechReasonException.name())) {
      return ex.getLogAttributeMap().get(GenomeAttributeType.TechReasonException.name());
    }
    return new LogExceptionAttribute(ex);
  }

  protected void initThread()
  {
  }

  protected void finalizeThread()
  {
  }

  /**
   * Hier wird der Runtime-Job aus dem {@link TriggerJobDO} erzeugt und gestartet.
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  @SuppressWarnings("unused")
  public void run()
  {
    // TODO genome hier filter concept umsetzen
    String state = "start";
    Exception handleEx = null;
    JobResultDO resultInfo = null;
    JobEventImpl event = null;
    LoggingContext.createNewContext();

    try {
      initThread();
      SchedulerThread sct = (SchedulerThread) Thread.currentThread();
      sct.setJobId(job.getPk());
      try {

        job.setHostName(HostUtils.getNodeName());

        if (GLog.isTraceEnabled() == true) {
          GLog.trace(GenomeLogCategory.Scheduler, "Start job",
              new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null,
                  null, scheduler)));
        }
        if (log.isDebugEnabled() == true) {
          log.debug("Start job: " + job.getPk());
        }
        resultInfo = jobStore.jobStarted(job, scheduler);
        event = new JobEventImpl(job, job.getJobDefinition(), resultInfo, null, scheduler);
        if (GLog.isInfoEnabled() == true) {
          GLog.info(GenomeLogCategory.Scheduler, "JobRunner resultInfo: " + resultInfo,
              new LogJobEventAttribute(event));
        }
        if (log.isDebugEnabled() == true) {
          log.debug("Job finished: " + job.getPk());
        }
        Object result;
        try {
          result = ChronosServiceManager.get().getSchedulerDAO().filterJobRun(this);
        } finally {
          // need for GenomeDaoManager.
          initThread();
          //          DynDaoManager.threadInitDynDaoManager();
        }
        // final Object result =
        // job.getExecutor().call(job.getJobArguments());
        state = "completed";
        completedJob(resultInfo, result);
      } catch (final ServiceUnavailableException ex) {
        state = "ServiceUnavailableException";
        handleEx = ex;
        if (ex.isSilent() == false) {
          /**
           * @logging
           * @reasin
           * @action
           */
          GLog.warn(GenomeLogCategory.Scheduler, "JobRunner serviceUnavailabe: " + ex, getLogExceptionAttribute(ex),
              new LogJobEventAttribute(event));
        }
        handleServiceError(resultInfo, ex);
      } catch (final ForceRetryException ex) {
        state = "ForceRetryException";
        handleEx = ex;
        if (ex.isSilent() == false) {
          /**
           * @logging
           * @reasin
           * @action
           */
          GLog.warn(GenomeLogCategory.Scheduler, "JobRunner force retry: " + ex, getLogExceptionAttribute(ex),
              new LogJobEventAttribute(
                  event));
        }
        handleRetry(resultInfo, ex);
      } catch (final RetryNextRunException ex) {
        state = "RetryNextRunException";
        handleEx = ex;
        if (ex.isSilent() == false) {
          GLog.warn(GenomeLogCategory.Scheduler, "JobRunner abort with nextretry: " + getLogExceptionAttribute(ex),
              new LogJobEventAttribute(event));
        }
        handleRetryNextRun(resultInfo, ex);
      } catch (final JobRetryException ex) {
        handleEx = ex;
        state = "RetryException";
        if (ex.isSilent() == false) {
          GLog.warn(GenomeLogCategory.Scheduler, "JobRunner retry: " + ex, new LogExceptionAttribute(ex),
              new LogJobEventAttribute(event));
        }
        handleUnexpected(resultInfo, ex);
      } catch (final JobAbortException ex) {
        handleEx = ex;
        state = "JobAbortException";

        /**
         * @logging
         * @reason
         * @action
         */
        GLog.warn(GenomeLogCategory.Scheduler, "JobRunner abort: " + ex, getLogExceptionAttribute(ex),
            new LogJobEventAttribute(event));
        handleFailure(resultInfo, ex);
      } catch (final Exception ex) {
        handleEx = ex;
        state = "Exception";
        /**
         * @logging
         * @reason
         * @action
         * 
         */
        GLog.warn(GenomeLogCategory.Scheduler, "JobRunner unexpected: " + ex, new LogExceptionAttribute(ex),
            new LogJobEventAttribute(event));
        handleUnexpected(resultInfo, ex);
      } finally {

        sct.setJobId(-1);
        // NOT, SchedulerThreadPoolExecutor should do this
        // scheduler.getDispatcher().wakeup(); // problem this synchronizes Dispatcher
      }
    } catch (Exception ex) {
      try {
        long jobId = -1;
        if (job != null) {
          jobId = job.getPk();
        }
        /**
         * @logging
         * @reason
         * @action
         */
        GLog.error(GenomeLogCategory.Scheduler,
            "JobRunner fail handle: " + state + "; jobId: " + jobId + "; " + ex.getMessage(),
            new LogExceptionAttribute(ex));
      } catch (Exception ex1) {
        // OOOPs
      }
    } finally {
      finalizeThread();

    }
  }

  public long getJobId()
  {
    if (job == null) {
      return -1;
    }
    return job.getPk();
  }

  /**
   * Handle unexpected.
   *
   * @param resultInfo the result info
   * @param ex the ex
   */
  private void handleUnexpected(final JobResultDO resultInfo, final Exception ex)
  {
    if (scheduler.getJobMaxRetryCount() <= job.getRetryCount()) {
      handleFailure(resultInfo, ex);
    } else {
      handleRetry(resultInfo, ex);
    }
  }

  /**
   * Job should be stopped.
   *
   * @param resultInfo the result info
   * @param ex the ex
   */
  private void handleFailure(final JobResultDO resultInfo, final Exception ex)
  {
    if (GLog.isTraceEnabled() == true) {
      /**
       * @logging
       * @reason
       * @action
       */
      GLog.trace(GenomeLogCategory.Scheduler, "Failed job",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null,
              State.STOP, scheduler)));
    }

    job.setFireTime(null);
    job.setState(State.STOP);
    jobStore.jobAborted(job, resultInfo, ex, scheduler);
    if (getFutureJob() instanceof FutureJobStatusListener) {
      FutureJobStatusListener listener = (FutureJobStatusListener) getFutureJob();
      listener.finalFail(this, resultInfo, ex);
    }
  }

  /**
   * Handle retry.
   *
   * @param resultInfo the result info
   * @param ex the ex
   */
  private void handleRetry(final JobResultDO resultInfo, final Exception ex)
  {
    if (GLog.isTraceEnabled() == true) {
      /**
       * @logging
       * @reason
       * @action
       */
      GLog.trace(GenomeLogCategory.Scheduler, "Retry job",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null,
              State.RETRY, scheduler)));
    }

    Date nextRun = job.getTrigger().updateAfterRun(scheduler, JobCompletion.EXPECTED_RETRY);
    job.setFireTime(nextRun);
    if (checkJobIsInRun() == true) {
      if (nextRun != null) {
        job.setState(State.WAIT);
      } else {
        job.setState(State.STOP);
      }
    }
    jobStore.jobRetry(job, resultInfo, ex, scheduler);

    scheduler.getDispatcher().addToReservedIfNessary(job);

  }

  /**
   * Handle retry next run.
   *
   * @param resultInfo the result info
   * @param ex the ex
   */
  private void handleRetryNextRun(final JobResultDO resultInfo, final Exception ex)
  {
    if (GLog.isTraceEnabled() == true) {
      /**
       * @logging
       * @reason
       * @action
       */
      GLog.trace(GenomeLogCategory.Scheduler, "Retry job",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null,
              State.RETRY, scheduler)));
    }

    Date nextRun = job.getTrigger().updateAfterRun(scheduler, JobCompletion.JOB_COMPLETED);
    job.setFireTime(nextRun);
    if (checkJobIsInRun() == false) {
      if (nextRun != null) {
        job.setState(State.WAIT);
      } else {
        job.setState(State.STOP);
      }
    }
    jobStore.jobRetry(job, resultInfo, ex, scheduler);
    scheduler.getDispatcher().addToReservedIfNessary(job);
  }

  /**
   * Handle service error.
   *
   * @param resultInfo the result info
   * @param ex the ex
   */
  private void handleServiceError(final JobResultDO resultInfo, final ServiceUnavailableException ex)
  {
    if (GLog.isTraceEnabled() == true) {
      /**
       * @logging
       * @reason
       * @action
       */
      GLog.trace(GenomeLogCategory.Scheduler, "Service unavailable",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(),
              null, State.RETRY, scheduler)));
    }

    Date nextRun = job.getTrigger().updateAfterRun(scheduler, JobCompletion.SERVICE_UNAVAILABLE);
    job.setFireTime(nextRun);
    if (checkJobIsInRun() == false) {
      if (nextRun != null) {
        job.setState(State.WAIT);
      } else {
        job.setState(State.STOP);
      }
    }
    jobStore.serviceRetry(job, resultInfo, ex, scheduler);
    scheduler.pause(scheduler.getServiceRetryTime());
    scheduler.getDispatcher().addToReservedIfNessary(job);
  }

  /**
   * Completed job.
   *
   * @param resultInfo the result info
   * @param result the result
   */
  private void completedJob(final JobResultDO resultInfo, final Object result)
  {
    boolean traceEnabled = GLog.isTraceEnabled();
    if (traceEnabled == true) {
      /**
       * @logging
       * @reason
       * @action
       */
      GLog.trace(GenomeLogCategory.Scheduler, "Job completed",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null,
              State.FINISHED, scheduler)));
    }

    Date nextRun = job.getTrigger().updateAfterRun(scheduler, JobCompletion.JOB_COMPLETED);

    if (checkJobIsInRun() == true) {
      if (nextRun != null) {
        job.setState(State.WAIT);
      } else {
        job.setState(State.FINISHED);
      }
    }
    jobStore.jobCompleted(job, resultInfo, result, scheduler, nextRun);
    scheduler.getDispatcher().addToReservedIfNessary(job);
    if (traceEnabled == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "Job updated and finished",
          new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null, State.FINISHED, scheduler)));
    }
  }

  /**
   * If the state of the job was modified from outside and is not RUN, do not update job.
   *
   * @return true, if successful
   */
  protected boolean checkJobIsInRun()
  {
    SchedulerDAO schedDao = ChronosServiceManager.get().getSchedulerDAO();
    TriggerJobDO dbjob = schedDao.getJobByPk(job.getPk());
    if (dbjob.getState() != State.RUN) {
      GLog.warn(GenomeLogCategory.Scheduler, "Job is not in RUN, stopping further retry",
          new LogJobEventAttribute(new JobEventImpl(dbjob,
              dbjob.getJobDefinition(), null, State.FINISHED, scheduler)));
      job.setState(dbjob.getState());
      return false;
    }
    return true;
  }

  public Scheduler getScheduler()
  {
    return scheduler;
  }

  public void setScheduler(Scheduler scheduler)
  {
    this.scheduler = scheduler;
  }

  public TriggerJobDO getJob()
  {
    return job;
  }

  public void setJob(TriggerJobDO job)
  {
    this.job = job;
  }

  public JobStore getJobStore()
  {
    return jobStore;
  }

  public void setJobStore(JobStore jobStore)
  {
    this.jobStore = jobStore;
  }

  public FutureJob getFutureJob()
  {
    return futureJob;
  }

  public void setFutureJob(FutureJob futureJob)
  {
    this.futureJob = futureJob;
  }

}
