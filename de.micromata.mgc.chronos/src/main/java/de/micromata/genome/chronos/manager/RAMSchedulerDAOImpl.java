/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   24.01.2008
// Copyright Micromata 24.01.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.manager;

import de.micromata.genome.chronos.spi.DispatcherImpl;
import de.micromata.genome.chronos.spi.DispatcherImpl2;
import de.micromata.genome.chronos.spi.ram.RamJobStore;

/**
 * Implements a Scheduler Service with a RAM Jobstore.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class RAMSchedulerDAOImpl extends SchedulerBaseDAO
{

  @Override
  public DispatcherImpl createDispatcher(String virtualHostName) throws Exception
  {
    RamJobStore store = new RamJobStore();

    DispatcherImpl dispatcher = new DispatcherImpl2(virtualHostName, store);
    return dispatcher;
  }

}
