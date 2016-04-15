/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: JobCompletion.java,v $
//
// Project   jchronos
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   04.01.2007
// Copyright Micromata 04.01.2007
//
// $Id: JobCompletion.java,v 1.2 2007/02/25 13:38:59 roger Exp $
// $Revision: 1.2 $
// $Date: 2007/02/25 13:38:59 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

/**
 * The Enum JobCompletion.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public enum JobCompletion
{

  /**
   * The service unavailable.
   */
  SERVICE_UNAVAILABLE,

  /**
   * The exception.
   */
  EXCEPTION,

  /**
   * The expected retry.
   */
  EXPECTED_RETRY,

  /**
   * The thread pool exhausted.
   */
  THREAD_POOL_EXHAUSTED,

  /**
   * The job completed.
   */
  JOB_COMPLETED;
}
