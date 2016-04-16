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

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.chronos.JobCompletion;
import de.micromata.genome.chronos.JobDebugUtils;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;

/**
 * Starts in next x milliseconds.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DelayTrigger implements Trigger
{

  /**
   * The next fire time.
   */
  private Date nextFireTime;

  /**
   * The millis.
   */
  private final long millis;

  /**
   * Instantiates a new delay trigger.
   *
   * @param millisToWait the millis to wait
   */
  public DelayTrigger(final long millisToWait)
  {
    millis = millisToWait;
    nextFireTime = new Date(System.currentTimeMillis() + millisToWait);
  }

  /**
   * Instantiates a new delay trigger.
   *
   * @param arg the arg
   */
  public DelayTrigger(final String arg)
  {
    if (arg.startsWith("+") == false) {
      throw new IllegalArgumentException("wrong string format (prefiix '+' is missing: " + arg);
    }
    millis = Long.parseLong(StringUtils.trim(arg.substring(1)));
    nextFireTime = new Date(System.currentTimeMillis() + millis);
  }

  @Override
  public String getTriggerDefinition()
  {
    return "+" + millis;
  }

  /**
   * Update after run.
   *
   * @param scheduler the scheduler
   * @param cause the cause
   * @return the date
   */
  @Override
  public Date updateAfterRun(final Scheduler scheduler, final JobCompletion cause)
  {
    long retryTime = 0;
    switch (cause) {
      case JOB_COMPLETED:
      case EXCEPTION:
        nextFireTime = null;
        break;
      case EXPECTED_RETRY:
        retryTime = scheduler.getJobRetryTime() * 1000;
        nextFireTime = new Date(new Date().getTime() + retryTime);
        break;
      case SERVICE_UNAVAILABLE:
        retryTime = scheduler.getServiceRetryTime() * 1000;
        nextFireTime = new Date(new Date().getTime() + retryTime);
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
          + "; retrytime: "
          + retryTime
          + " for "
          + this);
    }
    return nextFireTime == null ? null : new Date(nextFireTime.getTime());
  }

  public long getTriggerDelay()
  {
    return millis;
  }

  /**
   * Gets the next fire time.
   *
   * @param now the now
   * @return the next fire time
   */
  @Override
  public Date getNextFireTime(final Date now)
  {
    return nextFireTime == null ? null : new Date(nextFireTime.getTime());
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

  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString()
  {
    return JobDebugUtils.triggerToString(this);
  }
}
