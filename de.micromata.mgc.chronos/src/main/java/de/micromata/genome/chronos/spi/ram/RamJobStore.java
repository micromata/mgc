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

package de.micromata.genome.chronos.spi.ram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.AbstractJobStore;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;

/**
 * Abbildung der Jobs im Memory.
 *
 * @author roger@micromata.de
 */
public class RamJobStore extends AbstractJobStore
{

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(RamJobStore.class);

  /**
   * The schedulers by pk.
   */
  private Map<Long, SchedulerDO> schedulersByPk = new HashMap<Long, SchedulerDO>();

  /**
   * The schedulers by name.
   */
  private Map<String, SchedulerDO> schedulersByName = new HashMap<String, SchedulerDO>();
  
  /**
   * Scheduler.pk -> Job.pk -> Job
   */
  private final Map<Long, Map<Long, TriggerJobDO>> allJobs = new HashMap<>();

  /**
   * Job.pk -> JobResults
   */
  private final Map<Long, List<JobResultDO>> jobResults = new HashMap<Long, List<JobResultDO>>();

  /**
   * The next job id.
   */
  private long nextJobId = 0;

  /**
   * The next scheduler id.
   */
  private long nextSchedulerId = 0;

  /**
   * The next result id.
   */
  private long nextResultId = 0;

  /**
   * For Unittests clear all internal data.
   */
  public synchronized void _clearJobStore()
  {
    schedulersByPk.clear();
    allJobs.clear();
    jobResults.clear();
  }

  @Override
  public synchronized SchedulerDO createOrGetScheduler(String schedulerName)
  {
    SchedulerDO ret = schedulersByName.get(schedulerName);
    if (ret != null) {
      return ret;
    }
    ret = new SchedulerDO();
    ret.setName(schedulerName);
    ret.setPk(++nextSchedulerId);
    schedulersByPk.put(ret.getPk(), ret);
    schedulersByName.put(ret.getName(), ret);
    return ret;
  }

  /**
   * Find scheduler pk by name.
   *
   * @param schedulerName the scheduler name
   * @return the long
   */
  private Long findSchedulerPkByName(String schedulerName)
  {
    if (StringUtils.isBlank(schedulerName) == true) {
      return null;
    }
    SchedulerDO sched = schedulersByName.get(schedulerName);
    if (sched != null) {
      return sched.getPk();
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public synchronized List<TriggerJobDO> getJobs(Scheduler scheduler, Date fromDate, Date untilDate, State state)
  {
    final Map<Long, TriggerJobDO> tjobs;
    if (scheduler != null) {
      tjobs = allJobs.get(scheduler.getId());
    } else {
      tjobs = new HashMap<Long, TriggerJobDO>();
      for (Map<Long, TriggerJobDO> scheds : allJobs.values()) {
        synchronized (scheds) {
          tjobs.putAll(scheds);
        }

      }

    }
    if (tjobs == null) {
      return ListUtils.EMPTY_LIST;
    }
    List<TriggerJobDO> ret = new ArrayList<TriggerJobDO>();

    ret.addAll(tjobs.values());

    for (Iterator<TriggerJobDO> it = ret.iterator(); it.hasNext() == true;) {
      TriggerJobDO job = it.next();
      if (state != null) {
        if (job.getState() != state) {
          it.remove();
          continue;
        }
      }
      if (fromDate != null) {
        if (job.getModifiedAt() != null && job.getModifiedAt().after(fromDate) == true) {
          it.remove();
          continue;
        }
      }
      if (untilDate != null) {
        if (job.getModifiedAt() != null && job.getModifiedAt().before(untilDate) == true) {
          it.remove();
          continue;
        }
      }
    }

    return ret; // TODO genome chronos filter

  }

  @Override
  public synchronized List<TriggerJobDO> getNextJobs(Scheduler scheduler, boolean foreignJobs)
  {
    final Map<Long, TriggerJobDO> tjobs;

    tjobs = allJobs.get(scheduler.getId());

    if (tjobs == null || tjobs.isEmpty() == true) {
      return Collections.emptyList();
    }

    final ArrayList<TriggerJobDO> jobsToStart = new ArrayList<TriggerJobDO>();
    Date now = new Date();
    boolean isDebugEnabled = GLog.isDebugEnabled();

    for (final TriggerJobDO job : tjobs.values()) {
      if (job.getState() != State.WAIT) {
        continue;
      }
      final Trigger trigger = job.getTrigger();
      final Date nextFireTime = trigger.getNextFireTime(now);
      if (nextFireTime != null && nextFireTime.before(now)) {
        if (isDebugEnabled == true) {
          GLog.debug(GenomeLogCategory.Scheduler, "Found trigger: " + trigger);
        }
        jobsToStart.add(job);
      }

    }
    return jobsToStart;
  }

  @Override
  public synchronized List<TriggerJobDO> getNextJobs(long lookForward)
  {
    final ArrayList<TriggerJobDO> jobsToStart = new ArrayList<TriggerJobDO>();
    Date vnow = new Date(System.currentTimeMillis() + lookForward);

    for (Map<Long, TriggerJobDO> tjobs : allJobs.values()) {

      for (final TriggerJobDO job : tjobs.values()) {
        if (job.getState() != State.WAIT) {
          continue;
        }
        final Trigger trigger = job.getTrigger();
        final Date nextFireTime = job.getNextFireTime();
        if (nextFireTime != null && nextFireTime.before(vnow)) {
          if (log.isDebugEnabled() == true) {
            log.debug("Found trigger: " + trigger);
          }
          jobsToStart.add(job);
        } else {
          if (log.isDebugEnabled() == true) {
            log.debug("Job in future: " + nextFireTime);
          }
        }

      }
    }
    return jobsToStart;
  }

  public String getPrefix()
  {
    return null;
  }

  @Override
  public synchronized List<JobResultDO> getResults(TriggerJobDO job, int maxResults)
  {

    List<JobResultDO> tl;

    tl = jobResults.get(job.getPk());

    if (tl == null) {
      return Collections.emptyList();
    }

    if (maxResults >= tl.size()) {
      return tl;
    }
    List<JobResultDO> ret = new ArrayList<JobResultDO>(maxResults);

    for (int i = 0; i < maxResults; ++i) {
      ret.add(tl.get(i));
    }

    return ret;
  }

  @Override
  public synchronized List<SchedulerDO> getSchedulers()
  {
    List<SchedulerDO> ret = new ArrayList<SchedulerDO>();

    ret.addAll(schedulersByPk.values());

    return ret;
  }

  @Override
  public void withinTransaction(final Runnable runnable)
  {
    runnable.run(); // NOSONAR false positive
  }

  @Override
  public synchronized void jobRemove(TriggerJobDO job, JobResultDO jobResult, Scheduler scheduler)
  {
    if (GLog.isTraceEnabled() == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "jobRemove: " + job.getPk() + "; " + job.toString());
    }
    Map<Long, TriggerJobDO> schedJobs = allJobs.get(scheduler.getId());

    TriggerJobDO removed = schedJobs.remove(job.getPk());

    if (removed == null) {
      GLog.warn(GenomeLogCategory.Scheduler, "Job with ID cannot be removed: " + job.getPk());
    }

    jobResults.remove(job.getPk());

  }

  @Override
  public synchronized void jobResultRemove(TriggerJobDO job, JobResultDO jobResult, Scheduler scheduler)
  {

    List<JobResultDO> resList;

    resList = jobResults.get(job.getPk());

    if (resList == null) {
      return;
    }

    resList.remove(jobResult);

  }

  @Override
  public synchronized void persist(SchedulerDO scheduler)
  {
    if (scheduler.getPk() == null || scheduler.getPk() == SchedulerDO.UNSAVED_SCHEDULER_ID) {
      scheduler.setPk(nextSchedulerId++);
    }
    schedulersByPk.put(scheduler.getPk(), scheduler);
    schedulersByName.put(scheduler.getName(), scheduler);

  }

  @Override
  public TriggerJobDO reserveJob(TriggerJobDO job)
  {
    job.setState(State.SCHEDULED);
    updateJob(job);
    return job;
  }

  public void setPrefix(String prefix)
  {
  }

  @Override
  public void shutdown() throws InterruptedException
  {

  }

  @Override
  public synchronized long getNextJobId()
  {
    return ++nextJobId;
  }

  @Override
  public synchronized long getNextJobResultId()
  {
    return ++nextResultId;
  }

  @Override
  public synchronized long getNextSchedulerId()
  {
    return ++nextSchedulerId;
  }

  @Override
  public synchronized void insertJob(TriggerJobDO job)
  {
    Validate.notNull(job);
    Validate.notNull(job.getScheduler());
    job.setPk(getNextJobId());
    Map<Long, TriggerJobDO> list;
    list = allJobs.get(job.getScheduler());

    if (list == null) {
      list = new HashMap<Long, TriggerJobDO>();
      allJobs.put(job.getScheduler(), list);
    }
    if (GLog.isTraceEnabled() == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "insertJob: " + job.getPk());
    }

    list.put(job.getPk(), job);

  }

  @Override
  public synchronized void insertResult(JobResultDO result)
  {
    result.setPk(getNextJobResultId());
    List<JobResultDO> l;

    l = jobResults.get(result.getJobPk());
    if (l == null) {
      l = new ArrayList<JobResultDO>();
      jobResults.put(result.getJobPk(), l);
    }
    l.add(result);

  }

  @Override
  public synchronized void updateJob(TriggerJobDO job)
  {
    Validate.notNull(job);
    Validate.notNull(job.getScheduler());
    if (GLog.isTraceEnabled() == true) {
      GLog.trace(GenomeLogCategory.Scheduler, "updateJob: " + job.getPk() + "; " + job.toString());
    }

    Map<Long, TriggerJobDO> c;

    c = allJobs.get(job.getScheduler());

    if (c == null) {
      GLog.warn(GenomeLogCategory.Scheduler,
          "RamJobStore; updateJob; No Scheduler found for: " + job.getScheduler());
      return;
    }

    c.put(job.getPk(), job);

  }

  // TODO genome chronos synchronize
  @Override
  public synchronized TriggerJobDO getAdminJobByPk(long pk)
  {
    for (Map<Long, TriggerJobDO> m : allJobs.values()) {
      if (m.containsKey(pk) == true) {
        return m.get(pk);
      }
    }
    return null;
  }

  @Override
  public synchronized List<TriggerJobDisplayDO> getAdminJobs(String hostName, String name, String state,
      String schedulerName, int resultCount, boolean withLastResult)
  {
    Long schedulerPk = null;
    if (StringUtils.isNotEmpty(schedulerName) == true) {
      SchedulerDO sched = schedulersByName.get(schedulerName);
      if (sched != null) {
        schedulerPk = sched.getPk();
      }
    }
    List<TriggerJobDisplayDO> ret = new ArrayList<TriggerJobDisplayDO>();
    for (Map.Entry<Long, Map<Long, TriggerJobDO>> m : allJobs.entrySet()) {
      if (schedulerPk != null && schedulerPk.equals(m.getKey()) == false) {
        continue;
      }
      for (Map.Entry<Long, TriggerJobDO> e : m.getValue().entrySet()) {
        if (StringUtils.isNotEmpty(hostName) && StringUtils.equals(hostName, e.getValue().getHostName()) == false) {
          continue;
        }
        if (StringUtils.isNotEmpty(state) && StringUtils.equals(state, e.getValue().getState().name()) == false) {
          continue;
        }
        TriggerJobDisplayDO tjd = new TriggerJobDisplayDO(e.getValue());
        ret.add(tjd);
      }

    }
    return ret;
  }

  @Override
  public synchronized List<SchedulerDisplayDO> getAdminSchedulers()
  {
    List<SchedulerDisplayDO> ret = new ArrayList<SchedulerDisplayDO>();
    for (SchedulerDO s : schedulersByPk.values()) {
      ret.add(new SchedulerDisplayDO(s));
    }
    return ret;
  }

  @Override
  public TriggerJobDO getJobByPk(long pk)
  {
    return getAdminJobByPk(pk);
  }

  @Override
  public synchronized List<JobResultDO> getResultsForJob(long jobId)
  {
    return jobResults.get(jobId);
  }

  @Override
  public synchronized int setJobState(long pk, String newState, String oldState)
  {
    for (Map.Entry<Long, Map<Long, TriggerJobDO>> m : allJobs.entrySet()) {
      if (m.getValue().containsKey(pk) == true) {
        TriggerJobDO tj = m.getValue().get(pk);
        if (StringUtils.equals(oldState, tj.getState().name()) == true) {
          tj.setState(State.valueOf(newState));
          if (StringUtils.equals(newState, "WAIT")) {
            tj.setRetryCount(0);
          }
          return 1;
        }
        return 0;
      }
    }
    return 0;
  }

  @Override
  public long getJobCount(State state)
  {
    return getJobCount(null, state);
  }

  /**
   * Gets the job count.
   *
   * @param schedulerName the scheduler name
   * @param state the state
   * @return the job count
   */
  public synchronized long getJobCount(String schedulerName, State state)
  {
    long result = 0;
    Long schedPk = findSchedulerPkByName(schedulerName);

    for (Map<Long, TriggerJobDO> v : allJobs.values()) {
      for (TriggerJobDO tj : v.values()) {
        if (state != null && tj.getState() != state) {
          continue;
        }
        if (schedPk != null && schedPk.equals(tj.getScheduler()) == false) {
          continue;
        }
      }
      ++result;
    }
    return result;
  }

  @Override
  public long getJobResultCount(State state)
  {
    return getJobResultCount(null, state);
  }

  /**
   * Gets the job result count.
   *
   * @param schedulerName the scheduler name
   * @param state the state
   * @return the job result count
   */
  public synchronized long getJobResultCount(String schedulerName, State state)
  {
    Long schedPk = findSchedulerPkByName(schedulerName);
    long result = 0;
    for (List<JobResultDO> lr : jobResults.values()) {
      for (JobResultDO jr : lr) {
        if (state != null && jr.getState() != state) {
          continue;
        }
        if (schedulerName != null) {
          TriggerJobDO jb = getJobByPk(jr.getJobPk());
          if (jb == null) {
            continue;
          }
          if (schedPk != null && schedPk.equals(jb.getScheduler()) == false) {
            continue;
          }
        }
        ++result;
      }
    }
    return result;
  }

  @Override
  public synchronized JobResultDO getResultByPk(long resultId)
  {
    for (List<JobResultDO> lr : jobResults.values()) {
      for (JobResultDO rs : lr) {
        if (rs.getPk() == resultId) {
          return rs;
        }
      }
    }
    // TODO genome throw ex
    return null;
  }

  @Override
  public void deleteScheduler(Long pk)
  {
    // TODO lado implement
  }

  @Override
  public boolean deleteJobWithResults(Long pk)
  {
    if (pk == null) {
      return false;
    }
    for (Map<Long, TriggerJobDO> m : allJobs.values()) {
      if (m.containsKey(pk) == true) {
        return m.remove(pk) != null;
      }
    }
    return false;
  }

  @Override
  public synchronized List<String> getJobNames()
  {
    List<String> jobNames = new ArrayList<String>();
    for (Map<Long, TriggerJobDO> m : allJobs.values()) {
      for (TriggerJobDO j : m.values()) {
        jobNames.add(j.getJobName());
      }
    }

    return jobNames;
  }

}
