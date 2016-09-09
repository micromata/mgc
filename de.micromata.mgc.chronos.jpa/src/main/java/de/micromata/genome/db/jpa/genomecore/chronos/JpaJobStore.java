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

package de.micromata.genome.db.jpa.genomecore.chronos;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.AbstractJobStore;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;
import de.micromata.genome.jpa.Clauses;
import de.micromata.genome.jpa.Clauses.LogicClause;
import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.CommonMatchers;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class JpaJobStore.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaJobStore extends AbstractJobStore
{

  /**
   * The log.
   */
  private static Logger LOG = Logger.getLogger(JpaJobStore.class);
  JpaChronosEmgrFactory emfac = JpaChronosEmgrFactory.get();

  @Override
  public List<SchedulerDO> getSchedulers()
  {
    return emfac.runInTrans(new EmgrCallable<List<SchedulerDO>, DefaultEmgr>()
    {

      @Override
      public List<SchedulerDO> call(DefaultEmgr emgr)
      {
        List<JpaSchedulerDO> res = emgr.selectDetached(JpaSchedulerDO.class,
            "select  m from " + JpaSchedulerDO.class.getSimpleName() + " m");
        List<SchedulerDO> ret = new ArrayList<>();
        for (JpaSchedulerDO shd : res) {
          ret.add(SchedJpaTypeConverter.fromDb(shd));
        }
        return ret;
      }
    });
  }

  @Override
  public void insertJob(final TriggerJobDO job)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("insertJob: " + job);
    }
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        final JpaTriggerJobDO jdo = SchedJpaTypeConverter.toDb(job);
        emgr.insert(jdo);
        job.setPk(jdo.getPk());
        return null;
      }
    });
  }

  @Override
  public void updateJob(final TriggerJobDO job)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("updateJob: " + job);
    }
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        JpaTriggerJobDO jjob = emgr.selectByPkAttached(JpaTriggerJobDO.class, job.getPk());
        JpaTriggerJobDO toupdate = SchedJpaTypeConverter.toDb(job);
        PrivateBeanUtils.copyInstanceProperties(JpaTriggerJobDO.class, toupdate, jjob,
            CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
                FieldMatchers.fieldName("+*,-createdAt,-createdBy,-updateCounter,-pk,-attributes")));
        emgr.update(jjob);
        return null;
      }
    });
  }

  @Override
  public void insertResult(final JobResultDO result)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("insertResult: " + result);
    }
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        final JpaJobResultDO jdo = SchedJpaTypeConverter.toDb(result);
        emgr.insert(jdo);
        result.setPk(jdo.getPk());
        return null;
      }
    });

  }

  @Override
  public List<TriggerJobDO> getNextJobs(final Scheduler scheduler, final boolean foreignJobs)
  {

    return emfac.runInTrans(new EmgrCallable<List<TriggerJobDO>, DefaultEmgr>()
    {
      @Override
      public List<TriggerJobDO> call(DefaultEmgr emgr)
      {

        Date now = emfac.getNow();
        List<JpaTriggerJobDO> resl = new ArrayList<>();
        String thisHostName = getDispatcher().getVirtualHostName();
        String selectsql = "select j from " + JpaTriggerJobDO.class.getName() + " j";
        if (scheduler.getNodeBindingTimeout() == 0) {

          resl = emgr.createQueryDetached(JpaTriggerJobDO.class,
              selectsql + " where j.scheduler = :scheduler and j.nextFireTime < :now and j.state = :state",
              "scheduler", scheduler.getId(),
              "now", now,
              "state", State.WAIT)
              .setMaxResults(scheduler.getFreeJobSlotsCount()).getResultList();
        } else if (foreignJobs == true) {
          resl = emgr.createQueryDetached(JpaTriggerJobDO.class, selectsql
              + " where j.scheduler = :scheduler and j.nextFireTime < :now and j.state = :state and m.hostName != :hostName",
              "scheduler", scheduler.getId(),
              "now", now,
              "state", State.WAIT,
              "hostName", thisHostName)
              .setMaxResults(scheduler.getFreeJobSlotsCount()).getResultList();
        } else {
          emgr.createQueryDetached(JpaTriggerJobDO.class, selectsql
              + " where j.scheduler = :scheduler and j.nextFireTime < :now and j.state = :state and m.hostName == :hostName",
              "scheduler", scheduler.getId(),
              "now", now,
              "state", State.WAIT,
              "hostName", thisHostName)
              .setMaxResults(scheduler.getFreeJobSlotsCount()).getResultList();
        }
        List<TriggerJobDO> ret = new ArrayList<>(resl.size());
        for (JpaTriggerJobDO td : resl) {
          ret.add(SchedJpaTypeConverter.fromDb(td));
        }
        return ret;
      }

    });

  }

  @Override
  public List<TriggerJobDO> getNextJobs(final long lookForward)
  {
    return emfac.runInTrans(new EmgrCallable<List<TriggerJobDO>, DefaultEmgr>()
    {
      @Override
      public List<TriggerJobDO> call(DefaultEmgr emgr)
      {
        Date now = emfac.getNow();
        Date untilOwn = new Date(now.getTime() + lookForward);
        Date untilForeign = new Date(now.getTime() - lookForward);
        String thisHostName = getDispatcher().getVirtualHostName();
        List<JpaTriggerJobDO> resl = emgr
            .createQueryDetached(JpaTriggerJobDO.class,
                "select j from " + JpaTriggerJobDO.class.getName() + " j"
                    + " where j.state = :state and "
                    + "((j.hostName = :hostName and j.nextFireTime < :untilOwn) or (j.hostName != :hostName and j.nextFireTime < :untilForeign))"
                    + " order by j.nextFireTime asc",
                "state", State.WAIT,
                "hostName", thisHostName,
                "untilOwn", untilOwn,
                "untilForeign", untilForeign)
            .getResultList();

        List<TriggerJobDO> ret = new ArrayList<>(resl.size());
        for (JpaTriggerJobDO td : resl) {
          ret.add(SchedJpaTypeConverter.fromDb(td));
        }
        return ret;
      }

    });

  }

  @Override
  public List<TriggerJobDO> getJobs(final Scheduler scheduler, final Date fromDate, final Date untilDate,
      final State state)
  {

    return emfac.runInTrans(new EmgrCallable<List<TriggerJobDO>, DefaultEmgr>()
    {
      @Override
      public List<TriggerJobDO> call(DefaultEmgr emgr)
      {
        LogicClause clause = new LogicClause("and");
        if (scheduler != null) {
          clause.getClauses().add(Clauses.equal("j.scheduler", scheduler.getId()));
        }
        if (state != null) {
          clause.getClauses().add(Clauses.moreOrEqual("j.state", state));
        }
        if (fromDate != null) {
          clause.getClauses().add(Clauses.moreOrEqual("j.modifiedAt", fromDate));
        }
        if (untilDate != null) {
          clause.getClauses().add(Clauses.lessOrEqual("j.modifiedAt", untilDate));
        }
        Map<String, Object> args = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        clause.renderClause(sb, "", args);
        String sql = "select j from  " + JpaTriggerJobDO.class.getName() + " j "
            + " where " + sb.toString();
        List<JpaTriggerJobDO> resl = emgr.createQueryDetached(JpaTriggerJobDO.class, sql, args)
            .getResultList();

        List<TriggerJobDO> ret = new ArrayList<>(resl.size());
        for (JpaTriggerJobDO td : resl) {
          ret.add(SchedJpaTypeConverter.fromDb(td));
        }
        return ret;
      }

    });
  }

  @Override
  public SchedulerDO createOrGetScheduler(final String schedulerName)
  {
    return emfac.runInTrans(new EmgrCallable<SchedulerDO, DefaultEmgr>()
    {
      @Override
      public SchedulerDO call(DefaultEmgr emgr)
      {
        List<JpaSchedulerDO> res = emgr.selectDetached(JpaSchedulerDO.class,
            "select  m from " + JpaSchedulerDO.class.getSimpleName() + " m where m.name = :name", "name",
            schedulerName);
        if (res.isEmpty() == true) {
          SchedulerDO shed = new SchedulerDO();
          shed.setName(schedulerName);
          return shed;
        }
        return SchedJpaTypeConverter.fromDb(res.get(0));
      }
    });
  }

  @Override
  public void jobRemove(final TriggerJobDO job, final JobResultDO jobResult, final Scheduler scheduler)
  {
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        long count = emgr.selectSingleAttached(Long.class,
            "select count(*) from " + JpaJobResultDO.class.getSimpleName() + " r where r.jobPk = :jobPk",
            "jobPk", job.getPk());
        if (count > 0) {
          // TODO RK log
          return null;
        }
        emgr.createUntypedQuery("delete from " + JpaTriggerJobDO.class.getSimpleName() + " j where j.pk = :pk",
            "pk", job.getPk())
            .executeUpdate();
        return null;
      }
    });

  }

  @Override
  public void jobResultRemove(final TriggerJobDO job, final JobResultDO jobResult, Scheduler scheduler)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("jobResultRemove: " + job + "; result: " + jobResult);
    }
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        emgr.createUntypedQuery(
            "update " + JpaTriggerJobDO.class.getSimpleName() + " j set j.currentResultPk = null where j.pk = :pk",
            "pk", job.getPk())
            .executeUpdate();
        emgr.createUntypedQuery("delete from " + JpaJobResultDO.class.getSimpleName() + " r where r.jobPk = :pk",
            "pk", job.getPk())
            .executeUpdate();
        return null;
      }
    });

  }

  @Override
  public List<JobResultDO> getResults(final TriggerJobDO impl, final int maxResults)
  {

    return emfac.runInTrans(new EmgrCallable<List<JobResultDO>, DefaultEmgr>()
    {
      @Override
      public List<JobResultDO> call(DefaultEmgr emgr)
      {
        List<JpaJobResultDO> rl = emgr.createQuery(JpaJobResultDO.class,
            "select r from " + JpaJobResultDO.class.getSimpleName() + " r where r.jobPk = :pk order by r.pk desc",
            "pk", impl.getPk())
            .setMaxResults(maxResults)
            .getResultList();
        List<JobResultDO> ret = new ArrayList<>(rl.size());
        for (JpaJobResultDO jr : rl) {
          ret.add(SchedJpaTypeConverter.fromDb(jr));
        }
        return ret;
      }

    });
  }

  @Override
  public void shutdown() throws InterruptedException
  {
    synchronized (this) {
      // Verhindern aller anderen Calls

    }
  }

  @Override
  public TriggerJobDO reserveJob(final TriggerJobDO job)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("reserveJob: " + job);
    }
    return emfac.runInTrans(emgr -> {
      CriteriaUpdate<JpaTriggerJobDO> cu = CriteriaUpdate.createUpdate(JpaTriggerJobDO.class);
      cu.set("state", State.SCHEDULED)
          .addWhere(Clauses.and(
              Clauses.equal("pk", job.getPk()),
              Clauses.equal("state", State.WAIT)));
      int count = emgr.update(cu);
      if (count > 0) {
        job.setState(State.SCHEDULED);
        return job;
      }
      return null;
    });

  }

  @Override
  public void persist(final SchedulerDO scheduler)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("persist Scheduler: " + scheduler);
    }
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {

        if (scheduler.getPk() == SchedulerDO.UNSAVED_SCHEDULER_ID) {
          JpaSchedulerDO jsched = SchedJpaTypeConverter.toDb(scheduler);
          jsched.setPk(null);
          emgr.insert(jsched);
          scheduler.setPk(jsched.getPk());
          return null;
        }
        JpaSchedulerDO jched = emgr.selectByPkAttached(JpaSchedulerDO.class, scheduler.getPk());
        PrivateBeanUtils.copyInstanceProperties(JpaSchedulerDO.class, scheduler, jched, FieldMatchers
            .fieldName("name,threadPoolSize,serviceRetryTime,jobRetryTime,jobMaxRetryCount,nodeBindingTimeout"));
        emgr.update(jched);
        return null;
      }

    });

  }

  @Override
  public void withinTransaction(final Runnable runnable)
  {
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        runnable.run(); // NOSONAR false positive
        return null;
      }
    });

  }

  @Override
  public TriggerJobDO getAdminJobByPk(final long pk)
  {

    return emfac.runInTrans(new EmgrCallable<TriggerJobDO, DefaultEmgr>()
    {
      @Override
      public TriggerJobDO call(DefaultEmgr emgr)
      {
        JpaTriggerJobDO tj = emgr.selectByPkDetached(JpaTriggerJobDO.class, pk);
        //        Scheduler scheduler = ChronosServiceManager.get().getSchedulerDAO().getSchedulerByPk(tj.getScheduler());
        TriggerJobDO ret = SchedJpaTypeConverter.fromDb(tj);

        return ret;
      }
    });

  }

  @Override
  public TriggerJobDO getJobByPk(long pk)
  {
    return getAdminJobByPk(pk);
  }

  @Override
  public int setJobState(final long pk, final String newState, final String oldState)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("setJobState: " + pk + "; new " + newState + "; old " + oldState);
    }
    return emfac.runInTrans(new EmgrCallable<Integer, DefaultEmgr>()
    {
      @Override
      public Integer call(DefaultEmgr emgr)
      {
        Map<String, Object> params = new HashMap<String, Object>();
        final State ns = State.fromString(newState);
        final State os = State.fromString(oldState);
        params.put("jobPk", pk);
        params.put("state", ns);
        params.put("oldState", os);
        params.put("modifiedBy", LoggingServiceManager.get().getLoggingContextService().getCurrentUserName());
        if (ns == State.WAIT) {
          params.put("retryCount", 0);
        }

        CriteriaUpdate<JpaTriggerJobDO> cu = CriteriaUpdate.createUpdate(JpaTriggerJobDO.class)
            .addWhere(Clauses.and(
                Clauses.equal("pk", pk),
                Clauses.equal("state", os)))
            .set("state", ns);
        if (ns == State.WAIT) {
          cu.set("retryCount", 0);
        }
        int count = emgr.update(cu);
        return count;
      }
    });
  }

  @Override
  public List<TriggerJobDisplayDO> getAdminJobs(final String hostName, final String jobName, final String state,
      final String schedulerName,
      final int resultCount)
  {
    return getAdminJobs(hostName, jobName, state, schedulerName, resultCount, true);
  }

  /**
   * The Interface Createor.
   *
   * @param <T> the generic type
   */
  private static interface Createor<T>
  {

    /**
     * Creates the.
     *
     * @return the t
     */
    T create();
  }

  @Override
  public List<TriggerJobDisplayDO> getAdminJobs(String hostName, String jobName, String state, String schedulerName,
      int resultCount,
      boolean withLastResult)
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    List<TriggerJobDisplayDO> ret = new ArrayList<>();
    emfac.runInTrans((emgr) -> {
      selectJobsIntern(emgr, hostName, jobName, state, schedulerName, resultCount, ret, () -> {
        return new TriggerJobDisplayDO();
      });
      for (TriggerJobDisplayDO dtj : ret) {
        if (withLastResult == true && dtj.getCurrentResultPk() != null) {
          JpaJobResultDO result = emgr.findByPkDetached(JpaJobResultDO.class, dtj.getCurrentResultPk());
          dtj.setResult(SchedJpaTypeConverter.fromDb(result));
        }
        dtj.setSchedulerName(scheddao.getSchedulerByPk(dtj.getScheduler()).getName());
      }
      return ret;
    });
    return ret;
  }

  @Override
  public List<? extends TriggerJobDO> findJobs(String hostName, String jobName, String state, String schedulerName,
      int resultCount)
  {
    List<TriggerJobDO> ret = new ArrayList<>();
    return emfac.runInTrans((emgr) -> {
      return selectJobsIntern(emgr, hostName, jobName, state, schedulerName, resultCount, ret, () -> {
        return new TriggerJobDO();
      });
    });
  }

  /**
   * Select jobs intern.
   *
   * @param <T> the generic type
   * @param emgr the emgr
   * @param hostName the host name
   * @param jobName the job name
   * @param state the state
   * @param schedulerName the scheduler name
   * @param resultCount the result count
   * @param ret the ret
   * @param creator the creator
   * @return the list
   */
  private <T extends TriggerJobDO> List<T> selectJobsIntern(DefaultEmgr emgr, String hostName, String jobName,
      String state,
      String schedulerName,
      int resultCount, List<T> ret, Createor<T> creator)
  {
    Long schedPk = null;
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    if (StringUtils.isNotBlank(schedulerName) == true) {
      schedPk = scheddao.getScheduler(schedulerName).getId();
    }
    String sql = "select  j from " + JpaTriggerJobDO.class.getSimpleName() + " j ";

    LogicClause clause = new LogicClause("and");
    if (state != null) {
      clause.getClauses().add(Clauses.equal("j.state", State.fromString(state)));
    } else {
      clause.getClauses().add(Clauses.notEqual("j.state", State.CLOSED));
    }
    if (schedPk != null) {
      clause.getClauses().add(Clauses.equal("j.scheduler", schedPk));
    }
    if (StringUtils.isNotBlank(hostName) == true) {
      clause.getClauses().add(Clauses.equal("j.hostName", hostName));
    }
    if (StringUtils.isNotBlank(jobName) == true) {
      clause.getClauses().add(Clauses.equal("j.jobName", jobName));
    }
    Map<String, Object> args = new HashMap<>();
    if (clause.getClauses().isEmpty() == false) {
      StringBuilder sb = new StringBuilder();
      clause.renderClause(sb, "", args);
      sql += " where " + sb.toString();
    }
    sql += " order by j.modifiedAt desc";

    TypedQuery<JpaTriggerJobDO> query = emgr.createQuery(JpaTriggerJobDO.class, sql, args);
    if(resultCount >= 1) {
      query.setMaxResults(resultCount);
    }
    List<JpaTriggerJobDO> resl = query.getResultList();


    for (JpaTriggerJobDO jt : resl) {
      T nr = creator.create();
      SchedJpaTypeConverter.fromDb(nr, jt);
      ret.add(nr);
    }
    return ret;
  }

  @Override
  public List<JobResultDO> getResultsForJob(final long jobId)
  {

    return emfac.runInTrans(new EmgrCallable<List<JobResultDO>, DefaultEmgr>()
    {
      @Override
      public List<JobResultDO> call(DefaultEmgr emgr)
      {
        List<JpaJobResultDO> resl = emgr
            .createQuery(JpaJobResultDO.class,
                "select r from " + JpaJobResultDO.class.getSimpleName() + " r where r.jobPk = :pk", "pk", jobId)
            .getResultList();
        List<JobResultDO> ret = new ArrayList<>(resl.size());
        for (JpaJobResultDO jr : resl) {
          ret.add(SchedJpaTypeConverter.fromDb(jr));
        }
        return ret;
      }
    });
  }

  @Override
  public JobResultDO getResultByPk(final long resultId)
  {
    return emfac.runInTrans(new EmgrCallable<JobResultDO, DefaultEmgr>()
    {
      @Override
      public JobResultDO call(DefaultEmgr emgr)
      {
        List<JpaJobResultDO> resl = emgr
            .createQuery(JpaJobResultDO.class,
                "select r from " + JpaJobResultDO.class.getSimpleName() + " r where r.pk = :pk", "pk", resultId)
            .getResultList();
        if (resl.isEmpty() == true) {
          // TODO RK unittest want null here?!
          return null;// throw new RuntimeException("Failure in getResultById. message: Cannot find job result");
        }
        return SchedJpaTypeConverter.fromDb(resl.get(0));
      }
    });
  }

  @Override
  public List<SchedulerDisplayDO> getAdminSchedulers()
  {
    return emfac.runInTrans(new EmgrCallable<List<SchedulerDisplayDO>, DefaultEmgr>()
    {
      @Override
      public List<SchedulerDisplayDO> call(DefaultEmgr emgr)
      {
        List<JpaSchedulerDO> resl = emgr
            .createQuery(JpaSchedulerDO.class, "select s from " + JpaSchedulerDO.class.getSimpleName() + " s")
            .getResultList();
        List<SchedulerDisplayDO> ret = new ArrayList<>(resl.size());
        for (JpaSchedulerDO js : resl) {
          ret.add(SchedJpaTypeConverter.displayFromDb(js));
        }
        return ret;
      }
    });
  }

  @Override
  public long getJobCount(final State state)
  {
    return emfac.runInTrans(new EmgrCallable<Long, DefaultEmgr>()
    {
      @Override
      public Long call(DefaultEmgr emgr)
      {
        if (state != null) {
          return emgr.selectSingleAttached(Long.class,
              "select count(j) from " + JpaTriggerJobDO.class.getSimpleName() + " j where j.state = :state",
              "state", state);
        } else {
          return emgr.selectSingleAttached(Long.class,
              "select count(j) from " + JpaTriggerJobDO.class.getSimpleName() + " j");
        }
      }
    });
  }

  @Override
  public long getJobResultCount(final State state)
  {
    return emfac.runInTrans(new EmgrCallable<Long, DefaultEmgr>()
    {
      @Override
      public Long call(DefaultEmgr emgr)
      {
        if (state != null) {
          return emgr.selectSingleAttached(Long.class,
              "select count(j) from " + JpaJobResultDO.class.getSimpleName() + " j where j.state = :state",
              "state", state);
        } else {
          return emgr.selectSingleAttached(Long.class,
              "select count(j) from " + JpaJobResultDO.class.getSimpleName() + " j");
        }
      }
    });
  }

  @Override
  public void deleteScheduler(final Long pk)
  {
    emfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr emgr)
      {
        int deleted = emgr
            .createUntypedQuery("delete from " + JpaSchedulerDO.class.getSimpleName() + " s where s.pk = :pk", "pk",
                pk)
            .executeUpdate();
        return null;
      }
    });

  }

  @Override
  public boolean deleteJobWithResults(final Long pk)
  {
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("deleteJobWithResults: " + pk);
    }
    return emfac.runInTrans(new EmgrCallable<Boolean, DefaultEmgr>()
    {
      @Override
      public Boolean call(DefaultEmgr emgr)
      {
        int deleted = emgr
            .createUntypedQuery("delete from " + JpaJobResultDO.class.getSimpleName() + " r where r.jobPk = :pk",
                "pk",
                pk)
            .executeUpdate();

        deleted = emgr
            .createUntypedQuery("delete from " + JpaTriggerJobDO.class.getSimpleName() + " j where s.job = :pk",
                "pk",
                pk)
            .executeUpdate();
        return deleted > 0;
      }
    });

  }

  @Override
  public List<String> getJobNames()
  {
    return emfac.runInTrans(new EmgrCallable<List<String>, DefaultEmgr>()
    {
      @Override
      public List<String> call(DefaultEmgr emgr)
      {
        return emgr.createQuery(String.class,
            "select distinct j.jobName from " + JpaTriggerJobDO.class.getSimpleName() + " j").getResultList();
      }
    });
  }

  @Override
  public long getNextJobId()
  {
    throw new UnsupportedOperationException(
        "de.micromata.genome.db.jpa.genomecore.chronos.JpaJobStore.getNextJobId()");
  }

  @Override
  public long getNextSchedulerId()
  {
    throw new UnsupportedOperationException(
        "de.micromata.genome.db.jpa.genomecore.chronos.JpaJobStore.getNextSchedulerId()");
  }

  @Override
  public long getNextJobResultId()
  {
    throw new UnsupportedOperationException(
        "de.micromata.genome.db.jpa.genomecore.chronos.JpaJobStore.getNextJobResultId()");
  }
}
