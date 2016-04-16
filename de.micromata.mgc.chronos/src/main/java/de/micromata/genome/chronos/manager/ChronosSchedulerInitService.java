package de.micromata.genome.chronos.manager;

/**
 * Provides jobs and scheduler definitions for intialize.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ChronosSchedulerInitService
{
  void initSchedulerManager(SchedulerManager schedManager);

  default void copyAddSchedulerManager(SchedulerManager source, SchedulerManager target)
  {
    target.getScheduleFactories().addAll(source.getScheduleFactories());
    target.getJobs().putAll(source.getJobs());
    target.getGlobalFilter().addAll(source.getGlobalFilter());
    target.getStandardJobs().addAll(source.getStandardJobs());
    target.getStartupJobs().addAll(source.getStartupJobs());
    target.setVirtualHostName(source.getVirtualHostName());
    target.setMinNodeBindTime(source.getMinNodeBindTime());
    target.setMinRefreshInMillis(source.getMinRefreshInMillis());
    target.setStartRefreshInMillis(source.getStartRefreshInMillis());
    target.setMaxRefreshInMillis(source.getMaxRefreshInMillis());
    target.setRestartOwnJobsOnBooting(source.isRestartOwnJobsOnBooting());
    target.setRestartOwnJobTimeoutInMillis(source.getRestartOwnJobTimeoutInMillis());
  }
}
