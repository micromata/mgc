/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: CronTrigger.java,v $
//
// Project   genome
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   03.01.2007
// Copyright Micromata 03.01.2007
//
// $Id: CronTrigger.java,v 1.4 2007/03/09 07:25:11 roger Exp $
// $Revision: 1.4 $
// $Date: 2007/03/09 07:25:11 $
//
/////////////////////////////////////////////////////////////////////////////
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
