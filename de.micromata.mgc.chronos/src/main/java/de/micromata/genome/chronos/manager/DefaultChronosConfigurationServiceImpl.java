package de.micromata.genome.chronos.manager;

import de.micromata.genome.chronos.ChronosConfigurationService;

/**
 * Provides empty jobs/scheduler.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DefaultChronosConfigurationServiceImpl implements ChronosConfigurationService
{
  SchedulerManager schedulerManager = new SchedulerManager();

  @Override
  public SchedulerManager getScheduleManager()
  {

    return schedulerManager;
  }
  // TODO MGC
  //  @Override
  //  public SchedulerManager getSchedulerManager()
  //  {
  //    DynBeanConfig dbc = getChronosBeanConfig();
  //    if (dbc == null) {
  //      return null;
  //    }
  //    SchedulerManager manager = (SchedulerManager) dbc.getBean("schedulerManager");
  //    manager.init();
  //    return manager;
  //  }
  //
  //  private static DynBeanConfig getChronosBeanConfig()
  //  {
  //    GenomeDaoManager gdm = GenomeDaoManager.get();
  //    return (DynBeanConfig) gdm.getTimependingDAO().getVersionResource(null, "CONTEXT_CHRONOS", false);
  //  }
}
