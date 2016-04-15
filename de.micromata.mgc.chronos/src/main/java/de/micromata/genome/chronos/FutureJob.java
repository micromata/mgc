/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: FutureJob.java,v $
//
// Project   chronos
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   19.12.2006
// Copyright Micromata 19.12.2006
//
// $Id: FutureJob.java,v 1.3 2007/02/20 14:06:21 hx Exp $
// $Revision: 1.3 $
// $Date: 2007/02/20 14:06:21 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * Interface des eigentliches Runtime-Jobs.
 * 
 */
public interface FutureJob
{
  /**
   * Ausfuehrung
   * 
   * @param argument
   * @return
   * @throws Exception
   */
  public Object call(Object argument) throws Exception;

  /**
   * Sets the trigger job do.
   *
   * @param triggerJobDO the new trigger job do
   */
  public void setTriggerJobDO(TriggerJobDO triggerJobDO);

  /**
   * Gets the trigger job do.
   *
   * @return the trigger job do
   */
  public TriggerJobDO getTriggerJobDO();

}
