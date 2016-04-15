package de.micromata.mgc.chronos.test;

import de.micromata.genome.chronos.manager.ChronosSchedulerInitService;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.util.SchedulerFactory;

public class TestChronosSchedulerInitService implements ChronosSchedulerInitService
{

  @Override
  public void initSchedulerManager(SchedulerManager schedManager)
  {
    schedManager.setMinNodeBindTime(10000);
    SchedulerFactory sf = new SchedulerFactory();
    sf.setSchedulerName("testSched");
    schedManager.getScheduleFactories().add(sf);
  }

}
