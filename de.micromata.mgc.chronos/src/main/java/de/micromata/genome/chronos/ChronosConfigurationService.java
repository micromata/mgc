package de.micromata.genome.chronos;

import de.micromata.genome.chronos.manager.SchedulerManager;

/**
 * Service provides standard/startup Scheduler definitions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ChronosConfigurationService
{

  /**
   * Gets the schedule manager.
   *
   * @return the schedule manager
   */
  SchedulerManager getScheduleManager();
}
