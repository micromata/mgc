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

import org.apache.commons.lang3.time.StopWatch;

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
