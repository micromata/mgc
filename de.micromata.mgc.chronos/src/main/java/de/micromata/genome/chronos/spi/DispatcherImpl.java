//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.chronos.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;

/**
 * Zentrale Klasse (Singleton) für die Job-Verteilung.
 * <p>
 * Pollt die Datenbank nach neuen {@link Scheduler} und {@link Job} ab und versucht diese zu Starten.
 * </p>
 * <p>
 * Hier werden die Runtime-Instanzen von Schedulern und Jobs verwaltet.
 * </p>
 * 
 */
public abstract class DispatcherImpl implements Runnable, Dispatcher
{

  /**
   * The Constant log.
   */
  protected static final Logger log = Logger.getLogger(DispatcherImpl.class);

  /**
   * The job store.
   */
  private final JobStore jobStore;

  /**
   * Name of the application.
   */
  private String appName = "";

  /**
   * The virtual host name.
   */
  private String virtualHost;

  /**
   * The min refresh in millis.
   */
  protected long minRefreshInMillis = 10L;

  /**
   * The start refresh in millis.
   */
  protected long startRefreshInMillis = 250L;

  /**
   * The max refresh in millis.
   */
  protected long maxRefreshInMillis = 4000L;

  /**
   * time in milliseconds whereas minimal node bind (hostname) will be asumed.
   */
  protected long minNodeBindTime = 1000L;

  /**
   * The last scheduler update.
   */
  private long lastSchedulerUpdate = 0;

  /**
   * The scheduler lease time.
   */
  private long schedulerLeaseTime = 10000; // 10 seconds

  /**
   * Zuordnung der {@link Scheduler} zu ihrem Namen mit <i>und</i> ohne Prefix.
   */
  protected final Map<Long, Scheduler> schedulerByPk = new HashMap<>();

  /**
   * The scheduler by name.
   */
  protected final Map<String, Scheduler> schedulerByName = new HashMap<>();

  /**
   * Zuordnung der {@link TriggerJobDO} zum Namen des entsprechenden {@link Scheduler}.
   */
  // private final Map<String, List<TriggerJobDO>> jobs = new HashMap<String,
  // List<TriggerJobDO>>();
  /**
   * Zuordnung der {@link TriggerJobDO} zum Namen des entsprechenden {@link SchedulerImpl}.
   */
  private final Map<String, List<TriggerJobDO>> jobs = new HashMap<String, List<TriggerJobDO>>();

  /**
   * The dispatcher thread group.
   */
  private ThreadGroup dispatcherThreadGroup;

  /**
   * The dispatcher thread.
   */
  private Thread dispatcherThread;

  /**
   * The resume immediatelly.
   */
  protected volatile boolean resumeImmediatelly;

  /**
   * Erzeugt einen neuen Dispatcher.
   * <p>
   * Der Cluster-Diskriminator ist Empty!
   * </p>
   *
   * @param jobStore the job store
   */
  protected DispatcherImpl(final JobStore jobStore)
  {
    this.jobStore = jobStore;
    jobStore.setDispatcher(this);
    virtualHost = HostUtils.getThisHostName();
  }

  /**
   * Erzeugt einen neuen Dispatcher und gibt den virtual host name für den Cluster-Betrieb mit.
   *
   * @param virtualHost the virtual host
   * @param jobStore the job store
   */
  public DispatcherImpl(final String virtualHost, final JobStore jobStore)
  {
    this(jobStore);
    if (StringUtils.isNotEmpty(virtualHost) == true) {
      this.virtualHost = virtualHost;
    }
  }

  protected String getShortApplicationName()
  {
    return ChronosServiceManager.get().getSchedulerDAO().getShortApplicationName();
  }

  @Override
  public ThreadGroup getCreateDispatcherThreadGroup()
  {
    if (dispatcherThreadGroup != null) {
      return dispatcherThreadGroup;
    }
    dispatcherThreadGroup = new ThreadGroup(
        "JCDTG[" + getShortApplicationName() + "]: " + getDispatcherName());
    return dispatcherThreadGroup;
  }

  /**
   * Creates the thread.
   *
   * @param jobStore the job store
   * @return the thread
   */
  private Thread createThread(final JobStore jobStore)
  {
    final Thread t = new Thread(getCreateDispatcherThreadGroup(), this, "JCDT["
        + getShortApplicationName() + "]: "
        + getDispatcherName());
    t.setDaemon(true);
    return t;
  }

  @Override
  public String getDispatcherName()
  {
    return jobStore + " at " + HostUtils.getRunContext(dispatcherThread);
  }

  @Override
  public JobStore getJobStore()
  {
    return jobStore;
  }

  /**
   * Startet den Dispatcher und hält seinen Thread.
   */
  @Override
  public synchronized void startup()
  {
    GLog.info(GenomeLogCategory.Scheduler, "Starting Dispatcher");
    if (dispatcherThread == null) {
      dispatcherThread = createThread(jobStore);
    }
    if (dispatcherThread.isAlive() == false) {
      dispatcherThread.start();
    }
  }

  /**
   * Reset lrc.
   */
  public void resetLRC()
  {
    lastSchedulerUpdate = 0;
  }

  @Override
  public void shutdown() throws InterruptedException
  {
    shutdown(-1L);
    resetLRC();
  }

  /**
   * If dispatcher is waiting for starting a new job, wake dispatcher
   */
  @Override
  public void wakeup()
  {
    if (GLog.isDebugEnabled() == true) {
      GLog.debug(GenomeLogCategory.Scheduler, "DispatcherImpl.wakeup()");
    }
    if (log.isDebugEnabled() == true) {
      log.debug("DispatcherImpl.wakeup()");
    }
    resumeImmediatelly = true;
    synchronized (this) { // NOSONAR false positive
      this.notify(); // NOSONAR false positive
    }

  }

  @Override
  public boolean isRunning()
  {
    return dispatcherThread != null;
  }

  /**
   * Hält den Dispatcher-Thread an mit {@link Thread#join()}.
   * 
   * @param waitForShutdown how long to wait until the shutdown
   * @throws InterruptedException is thrown when an error happened 
   */
  @Override
  public void shutdown(final long waitForShutdown) throws InterruptedException
  {
    /**
     * @logging
     * @reason Chronos Dispatcher wird heruntergefahren
     * @action Keine
     */
    GLog.note(GenomeLogCategory.Scheduler, "Shutdown Dispatcher");
    if (dispatcherThread == null) {
      /**
       * @logging
       * @reason Chronos Dispatcher wird heruntergefahren, war aber bereits gestoppt
       * @action Keine
       */
      GLog.note(GenomeLogCategory.Scheduler, "Shutdown Dispatcher, was already stopped");
      return;
    }
    if (dispatcherThread.isAlive() == false) {
      /**
       * @logging
       * @reason Chronos Dispatcher wird heruntergefahren, obwohl er bereits am herunterfahren ist.
       * @action Entwickler kontaktieren
       */
      GLog.error(GenomeLogCategory.Scheduler, "Shutdown with stopped dispatcher");
      throw new IllegalStateException(this + " already stopped");
    }
    dispatcherThread.interrupt();
    if (waitForShutdown < 0L) {
      dispatcherThread.join();
    } else {
      dispatcherThread.join(waitForShutdown);
    }
    for (Scheduler sched : this.schedulerByPk.values()) {
      sched.shutdown(waitForShutdown);
    }
    dispatcherThread = null;
    this.schedulerByPk.clear();
    this.schedulerByName.clear();

    /**
     * @logging
     * @reason Chronos Dispatcher wurde heruntergefahren
     * @action Keine
     */
    GLog.note(GenomeLogCategory.Scheduler, "Shutdown Dispatcher finished");
  }

  /**
   * Check job store schedulers.
   */
  protected void checkJobStoreSchedulers()
  {
    long now = System.currentTimeMillis();
    if (now - lastSchedulerUpdate < schedulerLeaseTime) {
      return;
    }
    forceCheckJobStoreSchedulers();
  }

  /**
   * Force check job store schedulers.
   */
  protected void forceCheckJobStoreSchedulers()
  {
    List<SchedulerDO> schedulerDOs = getJobStore().getSchedulers();
    for (SchedulerDO s : schedulerDOs) {
      /* Scheduler sched = */createOrGetScheduler(s);
    }
  }

  public List<Scheduler> getSchedulers()
  {
    synchronized (this) {
      return new ArrayList<Scheduler>(schedulerByPk.values());
    }
  }

  /**
   * Gibt den Scheduler mit dem angegebenen Namen zurück oder <code>null</code>.
   * <p>
   * Hier wird <u>nicht</u> auf die Datenbank zugegriffen. Dafür ist {@link #createOrGetScheduler(SchedulerDO)} zu
   * benutzen.
   * </p>
   * 
   * @param name the name of the scheduler
   * @return the scheduler
   */
  @Override
  public Scheduler getScheduler(final String name)
  {
    synchronized (this) {
      if (GLog.isDebugEnabled() == true) {
        GLog.debug(GenomeLogCategory.Scheduler, "Get scheduler for: " + name + " " + name);
      }
      Scheduler scheduler = schedulerByName.get(name);
      if (scheduler != null) {
        return scheduler;
      }
      forceCheckJobStoreSchedulers();
      scheduler = schedulerByName.get(name);

      return scheduler;
    }
  }

  @Override
  public Scheduler getSchedulerByPk(Long pk)
  {
    synchronized (this) {
      Scheduler scheduler = schedulerByPk.get(pk);
      if (scheduler != null) {
        return scheduler;
      }
      forceCheckJobStoreSchedulers();
      scheduler = schedulerByPk.get(pk);
      return scheduler;
    }
  }

  public void setRefreshInterval(final long refreshInMillis)
  {
    this.minRefreshInMillis = refreshInMillis;
  }

  @Override
  public String toString()
  {
    return "Dispatcher for " + jobStore + " at " + getVirtualHostName() + "@" + HostUtils.getVm();
  }

  public String getAppName()
  {
    return appName;
  }

  public void setAppName(String appName)
  {
    this.appName = appName;
  }

  public Thread getDispatcherThread()
  {
    return dispatcherThread;
  }

  public void setDispatcherThread(Thread dispatcherThread)
  {
    this.dispatcherThread = dispatcherThread;
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

  public long getMaxRefreshInMillis()
  {
    return maxRefreshInMillis;
  }

  public void setMaxRefreshInMillis(long maxRefreshInMillis)
  {
    this.maxRefreshInMillis = maxRefreshInMillis;
  }

  public Map<String, List<TriggerJobDO>> getJobs()
  {
    return jobs;
  }

  public ThreadGroup getDispatcherThreadGroup()
  {
    return dispatcherThreadGroup;
  }

  public void setDispatcherThreadGroup(ThreadGroup dispatcherThreadGroup)
  {
    this.dispatcherThreadGroup = dispatcherThreadGroup;
  }

  @Override
  public String getVirtualHostName()
  {
    return virtualHost;
  }

  public long getMinNodeBindTime()
  {
    return minNodeBindTime;
  }

  @Override
  public void setMinNodeBindTime(long minNodeBindTime)
  {
    this.minNodeBindTime = minNodeBindTime;
  }

  public String getVirtualHost()
  {
    return virtualHost;
  }

  public void setVirtualHost(String virtualHost)
  {
    this.virtualHost = virtualHost;
  }

  @Override
  public long submit(String schedulerName, JobDefinition jobDefinition, Object arg, Trigger trigger, String hostName)
  {
    return submit(schedulerName, null, jobDefinition, arg, trigger, hostName);
  }

  @Override
  public void addToReservedIfNessary(TriggerJobDO job)
  {

  }

  protected SchedulerDAO getSchedulerDAO()
  {
    return ChronosServiceManager.get().getSchedulerDAO();
  }
}
