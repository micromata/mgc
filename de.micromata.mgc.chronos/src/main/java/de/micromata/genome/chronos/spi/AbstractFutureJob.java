/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    worker@micromata.de
// Created   20.02.2007
// Copyright Micromata 20.02.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi;

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * Hält zusätzlich den {@link TriggerJobDO}.
 * 
 * DO NOT THIS DIRECTLY in Genome, but AbstractGenomeJob or better AbstractCommandLineJob
 * 
 * @author H.Spiewok@micromata.de
 * 
 */
public abstract class AbstractFutureJob implements FutureJob
{

  /**
   * The trigger job do.
   */
  private TriggerJobDO triggerJobDO;

  public long getWaitTime()
  {
    if (triggerJobDO != null && triggerJobDO.getFireTime() != null) {
      return System.currentTimeMillis() - triggerJobDO.getFireTime().getTime();
    }
    return 0;
  }

  /**
   * @see de.micromata.genome.chronos.FutureJob#call(java.lang.Object)
   */
  @Override
  public abstract Object call(Object argument) throws Exception;

  @Override
  public TriggerJobDO getTriggerJobDO()
  {
    return triggerJobDO;
  }

  @Override
  public void setTriggerJobDO(final TriggerJobDO triggerJobDO)
  {
    this.triggerJobDO = triggerJobDO;
  }

}
