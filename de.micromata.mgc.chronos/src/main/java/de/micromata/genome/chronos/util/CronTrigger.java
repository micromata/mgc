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

import java.util.Date;

import de.micromata.genome.chronos.JobCompletion;
import de.micromata.genome.chronos.JobDebugUtils;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.CronExpression;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;

/**
 * The Class CronTrigger.
 *
 * @author roger
 * @see CronExpression
 */
public class CronTrigger implements Trigger
{

  /**
   * The next fire time.
   */
  private Date nextFireTime;

  /**
   * The cron expression.
   */
  private final CronExpression cronExpression;

  /**
   * Instantiates a new cron trigger.
   *
   * @param arg the arg
   */
  public CronTrigger(final String arg)
  {
    cronExpression = new CronExpression(arg);
  }

  @Override
  public String getTriggerDefinition()
  {
    return cronExpression.toString();
  }

  @Override
  public Date updateAfterRun(final Scheduler scheduler, final JobCompletion cause)
  {
    switch (cause) {
      case JOB_COMPLETED:
      case EXCEPTION:
        nextFireTime = calculateNext();
        break;
      case EXPECTED_RETRY: {
        Date rd = new Date(new Date().getTime() + scheduler.getJobRetryTime() * 1000);
        Date nf = calculateNext();
        long nt = Math.min(rd.getTime(), nf.getTime());
        nextFireTime = new Date(nt);
        break;
      }
      case SERVICE_UNAVAILABLE:
        Date rd = new Date(new Date().getTime() + scheduler.getServiceRetryTime() * 1000);
        Date nf = calculateNext();
        long nt = Math.min(rd.getTime(), nf.getTime());
        nextFireTime = new Date(nt);
        break;
      case THREAD_POOL_EXHAUSTED:
        // ????
        break;
      default:
        throw new IllegalArgumentException("Unexpected switch case: " + cause);
    }
    if (GLog.isTraceEnabled() == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "Update firetime to: "
          + JobDebugUtils.dateToString(nextFireTime)
          + " after "
          + cause
          + " for "
          + this);
    }
    return nextFireTime == null ? null : new Date(nextFireTime.getTime());
  }

  /**
   * Calculate next.
   *
   * @return the date
   */
  private Date calculateNext()
  {
    // FIXME warum wird altes nextFireTime wiederverwendet. Sollte das nicht IMMER now sein!?! GENOME-1200
    // ALT
    if (nextFireTime == null) {
      nextFireTime = new Date();
    }
    nextFireTime = cronExpression.getNextFireTime(nextFireTime);
    // new
    // nextFireTime = cronExpression.getNextFireTime(new Date());
    return nextFireTime;
  }

  @Override
  public Date getNextFireTime(final Date now)
  {
    return cronExpression.getNextFireTime(now);
  }

  @Override
  public void setNextFireTime(Date nextFireTime)
  {
    this.nextFireTime = nextFireTime;
  }

  @Override
  public Date getInternalNextFireTime()
  {
    return nextFireTime;
  }

  @Override
  public String toString()
  {
    return JobDebugUtils.triggerToString(this);
  }
}
