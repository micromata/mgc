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
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;

/**
 * Starts job in every x seconds.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class PeriodicalTrigger implements Trigger
{

  /**
   * The next fire time.
   */
  private Date nextFireTime;

  /**
   * The break in seconds.
   */
  private final Integer breakInSeconds;

  /**
   * A job triggered by this trigger will be repeated every x seconds untill it's status is set to "STOP", "FINISHED" or "CLOSED" manually
   * in the genome console.
   *
   * @param arg the arg
   */
  public PeriodicalTrigger(final String arg)
  {
    if (arg.startsWith("p") == false) {
      throw new IllegalArgumentException("wrong string format (prefiix 'p' is missing: " + arg);
    }
    breakInSeconds = Integer.parseInt(arg.substring(1));
    nextFireTime = new Date(System.currentTimeMillis() + breakInSeconds * 1000);
  }

  @Override
  public String getTriggerDefinition()
  {
    return "p" + breakInSeconds.toString();
  }

  @Override
  public Date updateAfterRun(final Scheduler scheduler, final JobCompletion cause)
  {
    switch (cause) {
      case JOB_COMPLETED:
      case EXCEPTION:
        nextFireTime = new Date(System.currentTimeMillis() + breakInSeconds * 1000);
        break;
      case EXPECTED_RETRY: {
        final Date rd = new Date(new Date().getTime() + scheduler.getJobRetryTime() * 1000);
        final Date nf = new Date(System.currentTimeMillis() + breakInSeconds * 1000);
        final long nt = Math.min(rd.getTime(), nf.getTime());
        nextFireTime = new Date(nt);
        break;
      }
      case SERVICE_UNAVAILABLE:
        final Date rd = new Date(new Date().getTime() + scheduler.getServiceRetryTime() * 1000);
        final Date nf = new Date(System.currentTimeMillis() + breakInSeconds * 1000);
        final long nt = Math.min(rd.getTime(), nf.getTime());
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

  @Override
  public Date getNextFireTime(final Date now)
  {
    return new Date(nextFireTime.getTime());
  }

  @Override
  public void setNextFireTime(final Date nextFireTime)
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
