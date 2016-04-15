//////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: Dispatcher.java,v $
//
// Project   genome
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   02.01.2007
// Copyright Micromata 02.01.2007
//
// $Id: Dispatcher.java,v 1.33 2008-01-05 14:36:11 roger Exp $
// $Revision: 1.33 $
// $Date: 2008-01-05 14:36:11 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import de.micromata.genome.chronos.JobDebugUtils;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.SchedulerConfigurationException;
import de.micromata.genome.chronos.SchedulerException;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.manager.LogJobEventAttribute;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.ScopedLogContextAttribute;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Zentrale Klasse (Singleton) für die Job-Verteilung.
 * <p>
 * Pollt die Datenbank nach neuen {@link Scheduler} und {@link Job} ab und versucht diese zu Starten.
 * <p/>
 * Diese Implementierung geht davon aus, dass es eine implizite minimale Nodebindtimeout gibt, so dass ein lokaler Cache
 * der zu startenden Jobs verwaltet werden kann und damit die Anzahl der selects reduziert werden kann.
 * </p>
 * <p>
 * Hier werden die Runtime-Instanzen von Schedulern und Jobs verwaltet.
 * </p>
 * TODO minRefreshInMillis, minRefreshInMillis, maxRefreshInMillis not used
 */
public class DispatcherImpl2 extends DispatcherImpl
{

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(DispatcherImpl2.class);

  /**
   * The Constant THREAD_SLEEP_AFTER_EX_DEFAULT_MS.
   */
  private static final long THREAD_SLEEP_AFTER_EX_DEFAULT_MS = 2000;

  /**
   * If dispatcher dies because some exception sleep time after relooping.
   */
  private long threadSleepAfterExceptionMs = THREAD_SLEEP_AFTER_EX_DEFAULT_MS;

  /**
   * The reserved jobs.
   */
  private ReservedJobs reservedJobs = new ReservedJobs();

  /**
   * Last time looked for new Jobs in JobStore.
   */
  private long lastJobStoreRefreshTimestamp = 0;

  /**
   * Erzeugt einen neuen Dispatcher.
   *
   * @param virtualHost the virtual host
   * @param jobStore the job store
   */
  public DispatcherImpl2(final String virtualHost, final JobStore jobStore)
  {
    super(virtualHost, jobStore);
  }

  /**
   * check if active jobs has to executed.
   *
   * @return time for next wakeup
   */
  public long checkJobsToRun()
  {

    checkJobStoreSchedulers();
    long now = System.currentTimeMillis();
    boolean oneStarted = false;
    int notstartedJobCount = 0;
    synchronized (reservedJobs) {
      for (Iterator<TriggerJobDO> it = reservedJobs.getJobsByNextFireTimeIterator(); it.hasNext() == true;) {
        TriggerJobDO job = it.next();
        if (job.getNextFireTime() == null) {
          GLog.note(GenomeLogCategory.Scheduler, "Reserved Job nextFireTime was null", new LogJobEventAttribute(job));
          reservedJobs.removeJob(it, job);
          continue;
        }
        Date nfd = job.getNextFireTime();
        long nft = nfd.getTime();
        long dif = now - nft;
        if (nft > now) {
          return nft;
        }
        Scheduler sched = schedulerByPk.get(job.getScheduler());
        int nbt = sched.getNodeBindingTimeout() * 1000;
        boolean foreignJob = getVirtualHost().equals(job.getHostName()) == false;
        if (foreignJob == true && now < nft + nbt) {
          continue;
        }
        boolean execute = sched.executeJob(job, getJobStore());
        oneStarted |= execute;
        if (execute == true) {
          reservedJobs.removeJob(it, job);
          if (foreignJob == true) {
            /**
             * @logging
             * @reason Ein Job von einer anderen Node wurde gestartet
             * @action Keine. Ggf. Node ueberpruefen und erneut starten.
             */
            GLog.note(GenomeLogCategory.Scheduler,
                "started foreign job: " + dif + " ms; " + job.getPk() + "; previous host: " + job.getHostName(), //
                new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null, null, sched)));
          } else {
            if (GLog.isTraceEnabled() == true) {
              GLog.trace(GenomeLogCategory.Scheduler,
                  "started local job: " + dif + " ms; " + job.getPk() + "; previous host: " + job.getHostName(), //
                  new LogJobEventAttribute(new JobEventImpl(job, job.getJobDefinition(), null, null, sched)));
            }
            if (log.isDebugEnabled() == true) {
              log.debug("started local job: " + dif + " ms; " + job.getPk() + "; previous host: " + job.getHostName());
            }
          }
        } else {
          if (GLog.isDebugEnabled() == true) {
            GLog.debug(GenomeLogCategory.Scheduler, "reserved job not executed: " + job.getPk(),
                new LogJobEventAttribute(job));
          }
          ++notstartedJobCount;

        }
      }
    }
    if (log.isDebugEnabled() == true) {
      log.debug("reserved job not executed: " + notstartedJobCount);
    }
    if (oneStarted == true) {
      return System.currentTimeMillis();
    }
    return -1;
  }

  /**
   * Check jobs in db.
   */
  private void checkJobsInDB()
  {
    long now = System.currentTimeMillis();
    if (lastJobStoreRefreshTimestamp + minNodeBindTime > now) {
      return;
    }
    lastJobStoreRefreshTimestamp = now;
    long lookAHead = minNodeBindTime;
    List<TriggerJobDO> nextJobs = getSchedulerDAO().getNextJobs(lookAHead);
    if (nextJobs != null && nextJobs.size() > 0 && GLog.isDebugEnabled() == true) {
      GLog.debug(GenomeLogCategory.Scheduler, "Dispatcher got new jobs from store: " + nextJobs.size());
    }
    if (log.isDebugEnabled() == true) {
      log.debug("Dispatcher got new jobs from store: " + nextJobs.size());
    }
    if (nextJobs != null) {
      reservedJobs.setReservedJobs(nextJobs);
    }
  }

  /**
   * Wait internal.
   *
   * @param timeout in ms
   * @return true if dispatcher thread should be terminated
   */
  private boolean waitInternal(long timeout)
  {
    if (log.isDebugEnabled() == true) {
      log.debug("Dispatcher sleep: " + timeout);
    }
    try {
      // this may wakeup from other scheduler threads
      synchronized (this) {
        this.wait(timeout); // NOSONAR false positive
      }
      if (GLog.isInfoEnabled() == true) {
        GLog.info(GenomeLogCategory.Scheduler, "dispatcher wake up");
      }
      if (log.isDebugEnabled() == true) {
        log.debug("Dispatcher wake up");
      }
      // // give other thread possiblebility to terminate
      // synchronized (this) {
      // this.wait(50);
      // }
      return false;
    } catch (final InterruptedException ex) {
      /**
       * @logging
       * @reason Chronos Dispatcher wurde heruntergefahren, da ein Thread-Interupt empfangen wurde
       * @action Keine
       */
      GLog.note(GenomeLogCategory.Scheduler, "Shutting down dispatcher because interrupted");
      return true;
    }
  }

  /**
   * Main loop of the dispatcher
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run()
  {
    if (LocalSettings.get().getBooleanValue("genome.chronos.nojobs", false) == true) {
      /**
       * @logging
       * @reason a config disables chronos dispatcher.
       * @action normally nothing. if not intended, check command line parameters of tomcat/jetty
       */
      GLog.note(GenomeLogCategory.Scheduler, "genome.chronos.nojobs is true,  stop Chronos Dispatcher");
      return;
    }
    runLoop();
  }

  protected void initOneLoop()
  {

  }

  protected void runLoop()
  {

    String runContext = HostUtils.getRunContext();
    ScopedLogContextAttribute threadContextScope = new ScopedLogContextAttribute(GenomeAttributeType.ThreadContext,
        runContext);
    ScopedLogContextAttribute threadContextScope2 = new ScopedLogContextAttribute(GenomeAttributeType.HttpSessionId,
        runContext);
    try {
      /**
       * @logging
       * @reason Chronos Dispatcher ist gestartet
       * @action Keine
       */
      GLog.note(GenomeLogCategory.Scheduler, "Dispatcher run started");
      int loopCount = 0;

      long nextJobTime = -1;
      while (Thread.interrupted() == false) {
        try {
          ++loopCount;
          initOneLoop();
          if (loopCount < 0) {
            loopCount = 0;
          }
          long now = System.currentTimeMillis();

          try {
            if (GLog.isTraceEnabled() == true) {
              GLog.trace(GenomeLogCategory.Scheduler, "Checking schedulers");
            }
            checkJobsInDB();
            resumeImmediatelly = false;
            nextJobTime = checkJobsToRun();
            if (GLog.isTraceEnabled() == true) {
              GLog.trace(GenomeLogCategory.Scheduler,
                  "Checked schedulers. NextJobTime: " + JobDebugUtils.dateToString(nextJobTime));
            }
          } catch (final Throwable ex) {
            nextJobTime = -1;// Ohne dies, wird timeout == 0 unten und der Thread schläft immer
            // da nextJobTime alt ist und n current isr ergebit sich eine negative Zahl, so dass es kleiner ist als minNodeBindTime
            // Somit wird timout durch timeout = nextJobTime - n; negativ und thread schläft mit timout(0) für immer, bis notify kommt.
            /**
             * @logging
             * @reason Chronos Dispatcher hat einen Fehler entdeckt
             * @action Abhaengig von der Exception Entwickler kontaktieren
             */
            GLog.error(GenomeLogCategory.Scheduler, "Error while dispatching: " + ex, new LogExceptionAttribute(ex));
          }
          if (resumeImmediatelly == true) {
            continue;
          }
          long timeout = minNodeBindTime;
          if (nextJobTime != -1 && nextJobTime - now < minNodeBindTime) {
            timeout = nextJobTime - now;
          }
          if (timeout < 0) {
            timeout = 0;
          }
          if (GLog.isInfoEnabled() == true) {
            GLog.info(GenomeLogCategory.Scheduler, "dispatcher go sleeping: "
                + timeout
                + " ms; nextJobTimeout: "
                + (nextJobTime == -1 ? "none" : Long.toString(nextJobTime - now)));
          }
          if (waitInternal(timeout) == true) {
            break;
          }

        } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
          try {
            /**
             * @logging
             * @reason Error in Dispatcher thread while looping
             * @action Entwickler kontaktieren
             */
            GLog.error(GenomeLogCategory.Scheduler, "Error in Dispatcher thread: " + ex.getMessage(),
                new LogExceptionAttribute(ex));
          } catch (Throwable ex2) { // NOSONAR "Illegal Catch" framework
            log.error("Dispatcher; Failed to log: " + ex2.getMessage(), ex);
          }
          try {
            Thread.sleep(threadSleepAfterExceptionMs);
          } catch (InterruptedException ei) {
            break;
          }
        }
      }
      GLog.note(GenomeLogCategory.Scheduler, "Dispatcher run finished");
    } finally {
      if (threadContextScope != null) {
        threadContextScope.restore();
      }
      if (threadContextScope2 != null) {
        threadContextScope2.restore();
      }
    }
  }

  protected Scheduler createScheduler(SchedulerDO schedulerDO)
  {
    return new SchedulerImpl(schedulerDO, this);
  }

  /**
   * Gibt zu einem {@link SchedulerDO} die entspechende reinitialisierte {@link Scheduler} zurück, oder erzeugt diese
   * neu.
   * <p>
   * Ein neu angelegter Scheduler wird unmittelbar persisitiert und unter dem Namen inklusive Prefix abgespeichert.
   * </p>
   * 
   * @param schedulerDO
   * @return
   * @see #schedulerByPk
   */
  @Override
  public Scheduler createOrGetScheduler(final SchedulerDO schedulerDO)
  {
    Validate.notNull(schedulerDO, "schedulerDB ist null.");

    String schedulerName = schedulerDO.getName();
    Validate.notNull(schedulerDO, "schedulerDB.name ist null.");

    synchronized (this) {
      Scheduler result = schedulerByName.get(schedulerName);

      if (result != null) {
        result.reInit(schedulerDO);
        return result;
      }

      final Scheduler scheduler = createScheduler(schedulerDO);

      // ist der Scheduler schon in der DB?
      final SchedulerDO schedulerDB = getSchedulerDAO().createOrGetPersistScheduler(schedulerName);
      if (schedulerDB.getPk() != SchedulerDO.UNSAVED_SCHEDULER_ID) {
        schedulerDO.setPk(schedulerDB.getPk());
        if (GLog.isTraceEnabled() == true) {
          GLog.trace(GenomeLogCategory.Scheduler,
              "Reuse existing DB-Sheduler entrie. scheduler: " + schedulerName + "#" + schedulerDB.getPk());
        }

      } else {
        schedulerDO.setName(schedulerDB.getName());
        /**
         * @logging
         * @reason Chronos Ein noch nicht existierender Scheduler wird in die DB geschrieben.
         * @action Keine
         */
        GLog.note(GenomeLogCategory.Scheduler, "Create a new DB-Entry for scheduler: " + schedulerName);

        // im Zweifelsfall mit neuen Werten überschreiben, sonst nur neu
        // anlegen
        getSchedulerDAO().persist(schedulerDO);
      }
      // setzen der Id aus der Datenbank !
      scheduler.setSchedulerId(schedulerDO.getPk());
      // Halten mit Prefix
      schedulerByPk.put(schedulerDO.getPk(), scheduler);
      schedulerByName.put(schedulerDO.getName(), scheduler);
      // jobs.put(schedulerName, new ArrayList<TriggerJobDO>());
      return scheduler;
    } // synchronized end
  }

  /**
   * 
   * @param schedulerName
   * @param jobDefinition
   * @param arg
   * @param trigger
   * @return Job reference (pk)
   * @throws SchedulerConfigurationException wenn ein nicht registrierter Scheduler angesprochen wird
   * @throws SchedulerException wenn der Job im JobStore nicht angelegt werden kann.
   */
  @Override
  public long submit(final String schedulerName, String jobName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger, String hostName)
  {

    if (hostName == null) {
      hostName = getVirtualHost();
    }
    synchronized (this) {
      final Scheduler scheduler = getScheduler(schedulerName);
      if (scheduler == null) {
        final String msg = "Es wurde versucht einen nicht registrierten Scheduler zu benutzen: " + schedulerName;
        /**
         * @logging
         * @reason Chronos Dispatcher hat einen Job ueber einen Schedulder bekommen, wobei der Scheduler nicht
         *         eingerichtet ist.
         * @action TechAdmin kontaktieren
         */
        GLog.error(GenomeLogCategory.Scheduler,
            "Es wurde versucht einen nicht registrierten Scheduler zu benutzen: " + schedulerName);
        throw new SchedulerConfigurationException(msg);
      }
      TriggerJobDO job = getSchedulerDAO().buildTriggerJob(scheduler, jobName, jobDefinition, arg, trigger, hostName,
          State.WAIT);

      boolean dispatcherAndSchedulerRunning = isRunning() && scheduler.isRunning();

      boolean isLocalHost = false;
      if (StringUtils.equals(hostName, getVirtualHostName()) == true) {
        isLocalHost = true;
      }
      boolean startJobNow = false;
      boolean addToLocalJobQueue = false;
      if (dispatcherAndSchedulerRunning == true) {
        if (isLocalHost == true) {
          Date now = new Date();
          Date nt = trigger.getNextFireTime(now);

          if (nt.getTime() - now.getTime() < 3) {
            startJobNow = true;
            addToLocalJobQueue = true;
          } else {
            addToLocalJobQueue = true;
          }
        }
      } else {
        GLog.note(GenomeLogCategory.Scheduler, "Submitting Job with no running dispather or scheduler");
      }
      getJobStore().insertJob(job);
      if (startJobNow == true) {
        boolean started = scheduler.executeJob(job, getJobStore());
        if (started == false) {
          reservedJobs.addReservedJob(job);
          wakeup();
        }
      } else if (addToLocalJobQueue == true) {
        reservedJobs.addReservedJob(job);
        wakeup();
      }
      Long jobPk = job.getPk();
      if (jobPk == null) {
        // pk = null sollte nicht auftreten können ist aber abhängig von der JobStore implemenmtierung und theoretisch möglich.
        final String msg = "Beim Anlegen des Jobs ist ein Fehler aufgetreten. Die Referenz (pk) wurde nicht gesetzt : "
            + job.toString();
        /**
         * @logging
         * @reason Im Job Store wurde beim persistieren eines neuen Jobs keine Referenz (pk) vergeben.
         * @action TechAdmin kontaktieren
         */
        GLog.error(GenomeLogCategory.Scheduler,
            "Beim Anlegen des Jobs ist ein Fehler aufgetreten. Die Referenz (pk) wurde nicht gesetzt : "
                + job.toString());
        throw new SchedulerException(msg);
      }
      return jobPk.longValue();
    }
  }

  @Override
  public void addToReservedIfNessary(TriggerJobDO job)
  {
    if (job == null) {
      return;
    }
    if (job.getState() != State.WAIT) {
      return;
    }
    if (StringUtils.equals(job.getHostName(), getVirtualHostName()) == false) {
      return;
    }
    Date now = new Date();
    Date nt = job.getTrigger().getNextFireTime(now);
    if (nt == null) {
      return;
    }
    if (nt.getTime() > now.getTime() + minNodeBindTime) {
      return;
    }
    if (GLog.isTraceEnabled() == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "Adding job to reservedJob", new LogJobEventAttribute(job));
    }
    reservedJobs.addReservedJob(job);
    wakeup();
  }

  @Override
  public void resetLRC()
  {
    super.resetLRC();
    reservedJobs = new ReservedJobs();
    lastJobStoreRefreshTimestamp = 0;
  }

  public long getThreadSleepAfterExceptionMs()
  {
    return threadSleepAfterExceptionMs;
  }

  public void setThreadSleepAfterExceptionMs(long threadSleepAfterExceptionMs)
  {
    this.threadSleepAfterExceptionMs = threadSleepAfterExceptionMs;
  }

}
