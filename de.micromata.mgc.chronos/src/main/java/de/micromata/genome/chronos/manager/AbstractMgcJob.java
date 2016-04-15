/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   20.01.2008
// Copyright Micromata 20.01.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.manager;

import org.apache.commons.lang.time.StopWatch;

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobControlException;
import de.micromata.genome.chronos.JobRetryException;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.logging.PerformanceCollector;

/**
 * Abstract basic job.
 *
 * @author roger
 */
public abstract class AbstractMgcJob extends AbstractFutureJob
{

  /**
   * Call the job.
   *
   * @param argument the argument
   * @param configTime the config time
   * @return the object
   * @throws Exception the exception
   */
  public abstract Object callJob(Object argument) throws Exception;

  /**
   * Prepare job.
   *
   * @param job the job
   * @return the stop watch
   */
  public static StopWatch prepareJob(FutureJob job)
  {
    StopWatch sw = PerformanceCollector.startPerformanceLog();
    return sw;
  }

  /**
   * Finish job.
   *
   * @param job the job
   * @param sw the sw
   * @param waitTime the wait time
   */
  public static void finishJob(FutureJob job, StopWatch sw, long waitTime)
  {
    PerformanceCollector.add(job.getClass().getName(), sw, GenomeLogCategory.Scheduler, waitTime);
  }

  @Override
  public Object call(Object argument) throws Exception
  {
    StopWatch sw = prepareJob(this);
    long waitTime = getWaitTime();

    try {
      Object arg = argument;

      return callJob(arg);

    } catch (JobControlException ex) {
      throw ex;
    } catch (LoggedRuntimeException ex) {
      throw new JobRetryException(ex.getMessage(), ex, true);
    } catch (Exception ex) {
      /**
       * @logging
       * @reason Ein Job wirft eine Exception
       * @action Exception ueberpruefen
       */
      GLog.error(GenomeLogCategory.Scheduler,
          "GenomeJob Failed. JobName: " + getClass().getSimpleName() + ": " + ex.getMessage(),
          new LogExceptionAttribute(ex));
      throw new JobRetryException(ex.getMessage(), ex, true);
    } finally {
      finishJob(this, sw, waitTime);
    }
  }
}
