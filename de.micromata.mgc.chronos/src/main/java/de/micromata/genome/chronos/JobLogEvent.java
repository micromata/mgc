/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: JobEvent.java,v $
//
// Project   jchronos
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   02.01.2007
// Copyright Micromata 02.01.2007
//
// $Id: JobEvent.java,v 1.4 2007/02/25 13:38:59 roger Exp $
// $Revision: 1.4 $
// $Date: 2007/02/25 13:38:59 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * For logging a Job.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JobLogEvent
{

  /**
   * Gets the scheduler.
   *
   * @return the scheduler
   */
  Scheduler getScheduler();

  /**
   * Gets the job.
   *
   * @return the job
   */
  TriggerJobDO getJob();

  /**
   * Gets the job definition.
   *
   * @return the job definition
   */
  JobDefinition getJobDefinition();

  /**
   * Gets the job result.
   *
   * @return the job result
   */
  JobResultDO getJobResult();

  /**
   * Gets the job status.
   *
   * @return the job status
   */
  State getJobStatus();
}
