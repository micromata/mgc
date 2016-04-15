/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: Trigger.java,v $
//
// Project   chronos
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   19.12.2006
// Copyright Micromata 19.12.2006
//
// $Id: Trigger.java,v 1.5 2007/03/09 07:25:10 roger Exp $
// $Revision: 1.5 $
// $Date: 2007/03/09 07:25:10 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import java.util.Date;

/**
 * The Interface Trigger.
 *
 * @author roger, wolle
 */
public interface Trigger
{

  /**
   * calcuate the next trigger definition.
   *
   * @param now the now
   * @return may return null, if no future trigger results
   */
  public Date getNextFireTime(Date now);

  /**
   * 
   * @return the internal next firetime, may be null
   */
  public Date getInternalNextFireTime();

  /**
   * sets the internal next fire time
   * 
   * @param nextFireTime
   */
  public void setNextFireTime(Date nextFireTime);

  /**
   * Vorbereitung auf den nächsten Auslösezeitpunkt.
   *
   * @param scheduler the scheduler
   * @param cause the cause
   * @return nextFireTime null wenn nicht mehr ausgefuehrt werden soll
   */

  public Date updateAfterRun(Scheduler scheduler, JobCompletion cause);

  /**
   * 
   * @return the trigger definition
   */
  public String getTriggerDefinition();

}
