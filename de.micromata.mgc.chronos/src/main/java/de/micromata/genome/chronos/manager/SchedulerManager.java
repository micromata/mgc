/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   09.01.2008
// Copyright Micromata 09.01.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.spi.DispatcherImpl;
import de.micromata.genome.chronos.util.SchedulerFactory;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.types.TimeInMillis;

/**
 * Configuration Klasse zum vereinfachten Handling mit Schedulern und Jobs.
 * <p>
 * Im Augenblick wird hier nur ein {@link DispatcherImpl} unterstützt.
 * </p>
 * 
 * 
 * 
 * @author roger
 */
public class SchedulerManager
{

  /**
   * predefined Jobs.
   */
  private Map<String, JobBeanDefinition> jobs = new HashMap<String, JobBeanDefinition>(0);

  /**
   * Jobs, which should be exists. These jobs are identified by jobname.
   */
  private List<JobBeanDefinition> standardJobs = new ArrayList<JobBeanDefinition>();

  /**
   * Jobs, which should be submittet at every startup.
   */
  private List<JobBeanDefinition> startupJobs = new ArrayList<JobBeanDefinition>();

  /**
   * The virtual host name.
   */
  private String virtualHostName = null;

  /**
   * The min refresh in millis.
   */
  private long minRefreshInMillis = 10L;

  /**
   * The start refresh in millis.
   */
  private long startRefreshInMillis = 250L;

  /**
   * The max refresh in millis.
   */
  private long maxRefreshInMillis = 4000L;

  /**
   * Minimale Nodebindtimeout (Dispatcher2).
   */
  private long minNodeBindTime = TimeInMillis.MINUTE;

  /**
   * The schedule factories.
   */
  private List<SchedulerFactory> scheduleFactories = new ArrayList<>();

  /**
   * The scheduler filter.
   */
  private Map<String, List<JobRunnerFilter>> schedulerFilter = new HashMap<String, List<JobRunnerFilter>>();

  /**
   * The global filter.
   */
  private List<JobRunnerFilter> globalFilter = new ArrayList<JobRunnerFilter>();

  /**
   * The merged filter.
   */
  private Map<String, List<JobRunnerFilter>> mergedFilter = null;

  /**
   * Startet stehen gebliebene Jobs mit der eigenen Node, die auf RUN oder SCHEDULED stehen.
   */
  private boolean restartOwnJobsOnBooting = true;

  /**
   * Startet nur stehengebliebene Jobs, deren modification time aelter ist als der angegebene Wert.
   */
  private long restartOwnJobTimeoutInMillis = TimeInMillis.MINUTE * 5;

  /**
   * Sucht (falls das zugehoerige Flag {@link #restartOwnJobsOnBooting} gesetzt ist) nach Jobs im Status RUN oder
   * SCHEDULED. Da der Dispatcher im Moment noch gar nicht aktiv ist, koennen solche Jobs eigentlich noch nicht
   * existieren. Falls es zu diesem Zeitpunkt ebensolche gibt, bedeutet das, dass diese unerwartet beendet worden sind,
   * z.B. durch einen System-Absturz. Nun werden sie erneut gestartet, indem ihr Zustand auf WAIT gesetzt wird.
   */

  public void init()
  {
    ChronosServiceManager.get().getSchedulerDAO().init(this);

  }

  /**
   * Instance des SchedulerManager.
   *
   * @return Kann null zurückgeben, wenn der Scheduler in ContextPop nicht definiert ist!
   */
  public static SchedulerManager get()
  {
    return ChronosServiceManager.get().getSchedulerDAO().getSchedulerManager();
  }

  public void afterPropertiesSet() throws Exception
  {
    Logging log = LoggingServiceManager.get().getLogging();
    if (scheduleFactories == null) {
      /**
       * @logging
       * @reason In der ContextPop sind keine Scheduler definiert.
       * @action Techadmin
       */
      log.error(GenomeLogCategory.Scheduler, "No SchedulerFactories configured in ContextDaoDomain");
      return;
    }
    if (jobs == null) {
      /**
       * @logging
       * @reason In der ContextPop sind keine Jobs verwendet.
       * @action Techadmin
       */
      log.error(GenomeLogCategory.Scheduler, "No Jobs configured in ContextChronos");
      return;
    }
    for (Map.Entry<String, JobBeanDefinition> me : jobs.entrySet()) {
      me.getValue().setBeanName(me.getKey());
    }
    init();

  }

  /**
   * Gets the filters.
   *
   * @param schedulerName the scheduler name
   * @return the filters
   */
  public synchronized List<JobRunnerFilter> getFilters(String schedulerName)
  {
    if (mergedFilter != null && mergedFilter.get(schedulerName) != null) {
      return mergedFilter.get(schedulerName);
    }
    if (mergedFilter == null) {
      mergedFilter = new HashMap<String, List<JobRunnerFilter>>();
    }
    List<JobRunnerFilter> res = new ArrayList<JobRunnerFilter>();
    res.addAll(globalFilter);
    List<JobRunnerFilter> l = schedulerFilter.get(schedulerName);
    if (l != null) {
      res.addAll(l);
    }
    Collections.sort(res, new Comparator<JobRunnerFilter>()
    {

      @Override
      public int compare(JobRunnerFilter o1, JobRunnerFilter o2)
      {
        return o1.getPriority() - o2.getPriority();
      }
    });
    mergedFilter.put(schedulerName, res);
    return res;
  }

  /**
   * Gets the job definition.
   *
   * @param name Standard Job name
   * @return den Job. Falls Job nicht gefunden werden kann, wird eine LoggedRuntimeException geworfen
   */
  public JobBeanDefinition getJobDefinition(String name)
  {
    if (jobs == null) {
      return null;
    }
    JobBeanDefinition jdef = jobs.get(name);
    if (jdef == null) {
      /**
       * @logging
       * @reason Der angegebene Job kann nicht gefunden werden
       * @action Techadmin kontaktieren, Konfiguration ueberpruefen
       */
      throw new LoggedRuntimeException(LogLevel.Fatal, GenomeLogCategory.Scheduler,
          "Standard Scheduler Job cannot be found: " + name);
    }
    return jdef;
  }

  private SchedulerDAO getSchedulerDAO()
  {
    return ChronosServiceManager.get().getSchedulerDAO();
  }

  public List<SchedulerFactory> getScheduleFactories()
  {
    return scheduleFactories;
  }

  public void setScheduleFactories(List<SchedulerFactory> scheduleFactories)
  {
    this.scheduleFactories = scheduleFactories;
  }

  public Map<String, JobBeanDefinition> getJobs()
  {
    return jobs;
  }

  public long getMaxRefreshInMillis()
  {
    return maxRefreshInMillis;
  }

  public void setMaxRefreshInMillis(long maxRefreshInMillis)
  {
    this.maxRefreshInMillis = maxRefreshInMillis;
  }

  public Map<String, List<JobRunnerFilter>> getSchedulerFilter()
  {
    return schedulerFilter;
  }

  public List<JobRunnerFilter> getGlobalFilter()
  {
    return globalFilter;
  }

  public Map<String, List<JobRunnerFilter>> getMergedFilter()
  {
    return mergedFilter;
  }

  public void setJobs(Map<String, JobBeanDefinition> jobs)
  {
    this.jobs = jobs;
  }

  public String getVirtualHostName()
  {
    return virtualHostName;
  }

  public void setVirtualHostName(String virtualHostName)
  {
    this.virtualHostName = virtualHostName;
  }

  public long getMinNodeBindTime()
  {
    return minNodeBindTime;
  }

  public void setMinNodeBindTime(long minNodeBindTime)
  {
    this.minNodeBindTime = minNodeBindTime;
  }

  public long getMinRefreshInMillis()
  {
    return minRefreshInMillis;
  }

  public void setMinRefreshInMillis(long minRefreshInMillis)
  {
    this.minRefreshInMillis = minRefreshInMillis;
  }

  public long getStartRefreshInMillis()
  {
    return startRefreshInMillis;
  }

  public void setStartRefreshInMillis(long startRefreshInMillis)
  {
    this.startRefreshInMillis = startRefreshInMillis;
  }

  public boolean isRestartOwnJobsOnBooting()
  {
    return restartOwnJobsOnBooting;
  }

  public void setRestartOwnJobsOnBooting(boolean restartOwnJobsOnBooting)
  {
    this.restartOwnJobsOnBooting = restartOwnJobsOnBooting;
  }

  public long getRestartOwnJobTimeoutInMillis()
  {
    return restartOwnJobTimeoutInMillis;
  }

  public void setRestartOwnJobTimeoutInMillis(long restartOwnJobTimeoutInMillis)
  {
    this.restartOwnJobTimeoutInMillis = restartOwnJobTimeoutInMillis;
  }

  public List<JobBeanDefinition> getStandardJobs()
  {
    return standardJobs;
  }

  public void setStandardJobs(List<JobBeanDefinition> standardJobs)
  {
    this.standardJobs = standardJobs;
  }

  public List<JobBeanDefinition> getStartupJobs()
  {
    return startupJobs;
  }

  public void setStartupJobs(List<JobBeanDefinition> startupJobs)
  {
    this.startupJobs = startupJobs;
  }
}
