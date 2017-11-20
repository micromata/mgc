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

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.chronos.util.DelayTrigger;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.mgc.common.test.MgcTestCase;

public class JobStoreTest extends MgcTestCase
{

  private static final String TMP_SCHEDNAME = "tmp_sched_451900";

  public void findClassPathInJars()
  {
    try {
      Iterator it = FileUtils.iterateFiles(new File("."), new String[] { "jar" }, true);
      for (; it.hasNext();) {
        File f = (File) it.next();
        ZipInputStream zf = new ZipInputStream(new FileInputStream(f));
        ZipEntry ze;
        while ((ze = zf.getNextEntry()) != null) {
          String name = ze.getName();
          if (name.startsWith("org/objectweb/asm") == true) {
            System.out.println("Found: " + f.getCanonicalPath());
            zf.closeEntry();
            break;
          }
          zf.closeEntry();
        }
        zf.close();
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  // @Test
  public void testSequences()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    JobStore jobStore = scheddao.getJobStore();
    jobStore.getResultsForJob(1L);
    jobStore.createOrGetScheduler("asdfasdf");
    jobStore.getAdminJobByPk(1L);
    jobStore.getAdminJobs(null, null, null, 10);
    jobStore.getNextJobs(scheddao.getScheduler("asdfasdf"), false);
    jobStore.getResultByPk(1L);

  }

  private JobStore getJobStore()
  {

    return ChronosServiceManager.get().getSchedulerDAO().getJobStore();
  }

  @Before
  public void setUp() throws Exception
  {
    ChronosServiceManager.get().setSchedulerDAO(new TestJpaSchedulerImpl());
    JobStore jobStore = getJobStore();
    SchedulerDO scheduler = jobStore.createOrGetScheduler(TMP_SCHEDNAME);
    if (scheduler.getPk() == -1) {
      jobStore.persist(scheduler);
    }
  }

  @After
  public void tearDown() throws Exception
  {
    //    super.tearDown();
    JobStore jobStore = getJobStore();
    SchedulerDO sc = jobStore.createOrGetScheduler(TMP_SCHEDNAME);
    jobStore.deleteScheduler(sc.getPk());
  }

  private Scheduler getScheduler()
  {
    JobStore jobStore = getJobStore();
    SchedulerDO schedulerDO = jobStore.createOrGetScheduler(TMP_SCHEDNAME);

    Dispatcher disp = null;
    try {
      disp = ChronosServiceManager.get().getSchedulerDAO().createDispatcher("");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return disp.createOrGetScheduler(schedulerDO);
  }

  @Test
  public void schedulerCRUD()
  {
    JobStore jobStore = getJobStore();
    List<SchedulerDisplayDO> schedulers = jobStore.getAdminSchedulers();
    Assert.assertFalse(schedulers.isEmpty());

    List<SchedulerDO> scheduler1 = jobStore.getSchedulers();
    Assert.assertFalse(scheduler1.isEmpty());

  }

  private TriggerJobDO createStdTestJob(String jobName)
  {
    JobStore jobStore = getJobStore();

    Scheduler scheduler = getScheduler();
    TriggerJobDO job = new TriggerJobDO();
    Trigger trigger = new DelayTrigger(1);
    //    job.setTrigger(trigger);
    job.setTriggerDefinition("+1");
    job.setFireTime(trigger.getNextFireTime(new Date()));
    job.setJobStore(jobStore);
    //    job.setPk(jobId);
    job.setJobName(jobName);
    job.setJobArgumentObject("x=a");
    job.setJobDefinitionObject(new ClassJobDefinition(DummyJob.class));
    job.setHostName(jobStore.getDispatcher().getVirtualHostName());
    job.setState(State.STOP);
    job.setScheduler(scheduler.getId());
    job.setCreatedBy(LoggingServiceManager.get().getLoggingContextService().getCurrentUserName());
    job.setModifiedBy(LoggingServiceManager.get().getLoggingContextService().getCurrentUserName());
    job.setUpdateCounter(0);
    return job;
  }

  @Test
  public void jobCRUD()
  {

    JobStore jobStore = getJobStore();

    Scheduler scheduler = getScheduler();

    // create job
    //    long jobId = jobStore.getNextJobId();
    TriggerJobDO job = createStdTestJob("JobStoreTest");
    // C
    jobStore.insertJob(job);

    // R
    TriggerJobDO fetched = jobStore.getJobByPk(job.getPk());
    Assert.assertTrue(StringUtils.equals(fetched.getJobName(), "JobStoreTest"));
    Assert.assertEquals(fetched.getPk(), job.getPk());
    Assert.assertEquals("x=a", fetched.getJobArguments());
    Assert.assertEquals("+1", fetched.getTriggerDefinition());
    // R
    long count = jobStore.getJobCount(State.STOP);
    Assert.assertTrue(count > 0);

    count = jobStore.getJobResultCount(State.STOP);
    Long jobId = job.getPk();
    // R
    TriggerJobDO j = jobStore.getAdminJobByPk(jobId);
    Assert.assertNotNull(j);
    Assert.assertTrue(StringUtils.equals(fetched.getJobName(), "JobStoreTest"));

    count = jobStore.setJobState(jobId, State.CLOSED.name(), State.STOP.name());
    Assert.assertTrue(count > 0);

    // R
    List<TriggerJobDisplayDO> jobs = jobStore.getAdminJobs(jobStore.getDispatcher().getVirtualHostName(),
        State.CLOSED.name(),
        scheduler.getName(), Integer.MAX_VALUE);
    Assert.assertFalse(jobs.isEmpty());
    boolean atLeastOneJobName = false;
    ;
    for (TriggerJobDisplayDO jj : jobs) {
      if (StringUtils.isNotBlank(jj.getJobName())) {
        atLeastOneJobName = true;
        break;
      }
    }
    Assert.assertTrue(atLeastOneJobName);

    List<TriggerJobDO> tjobs = jobStore.getJobs(scheduler, new Date(0L), new Date(System.currentTimeMillis() + 20000),
        State.CLOSED);
    Assert.assertFalse(tjobs.isEmpty());
    atLeastOneJobName = false;
    ;
    for (TriggerJobDO jj : tjobs) {
      if (StringUtils.isNotBlank(jj.getJobName())) {
        atLeastOneJobName = true;
        break;
      }
    }
    // C
    JobResultDO jr = new JobResultDO();
    jr.setJobPk(jobId);
    //    jr.setPk(resultId);
    jobStore.insertResult(jr);
    Long resultId = jr.getPk();

    // R
    JobResultDO rj1 = jobStore.getResultByPk(resultId);
    Assert.assertEquals(rj1.getPk(), jr.getPk());

    // U
    jobStore.updateJobWithResult(job, rj1);

    // D
    jobStore.jobResultRemove(job, jr, scheduler);

    // R
    JobResultDO jr1 = jobStore.getResultByPk(resultId);
    Assert.assertNull(jr1);

    // U
    job.setRetryCount(444);
    jobStore.updateJob(job);
    job = jobStore.getJobByPk(jobId);

    Assert.assertEquals(job.getRetryCount(), 444);

    // D
    jobStore.jobRemove(job, null, scheduler);
  }

  // TODO RK transfer to jdbc
  public void jobNameTests(JobStore jobStore, Scheduler scheduler)
  {
    //    JobStore jdbcJobStore = (JobStore) jobStore;
    //
    //    final Map<String, Object> map = new HashMap<String, Object>();
    //    map.put("maxRecords", 10);
    //    map.put("until", new Timestamp(System.currentTimeMillis()));
    //    map.put("scheduler", scheduler);
    //    map.put("hostName", "genlap");
    //    // jobListUntil
    //    List<TriggerJobDO> list = jdbcJobStore.getSqlMapClientTemplate().queryForList("jobListUntil", map);
    //
    //    boolean atLeastOneJobName = false;
    //    for (TriggerJobDO jj : list) {
    //      if (StringUtils.isNotBlank(jj.getJobName())) {
    //        atLeastOneJobName = true;
    //        break;
    //      }
    //    }
    //    Assert.assertTrue(atLeastOneJobName);
    //
    //    // jobListCurrent
    //    list = jdbcJobStore.getSqlMapClientTemplate().queryForList("jobListCurrent", map);
    //    atLeastOneJobName = false;
    //    for (TriggerJobDO jj : list) {
    //      if (StringUtils.isNotBlank(jj.getJobName())) {
    //        atLeastOneJobName = true;
    //        break;
    //      }
    //    }
    //    Assert.assertTrue(atLeastOneJobName);
    //
    //    // jobListFromThisNode
    //    list = jdbcJobStore.getSqlMapClientTemplate().queryForList("jobListFromThisNode", map);
    //    atLeastOneJobName = false;
    //    for (TriggerJobDO jj : list) {
    //      if (StringUtils.isNotBlank(jj.getJobName())) {
    //        atLeastOneJobName = true;
    //        break;
    //      }
    //    }
    //    Assert.assertTrue(atLeastOneJobName);
    //
    //    // jobListCurrent
    //    list = jdbcJobStore.getSqlMapClientTemplate().queryForList("jobListFromOtherNode", map);
    //    atLeastOneJobName = false;
    //    for (TriggerJobDO jj : list) {
    //      if (StringUtils.isNotBlank(jj.getJobName())) {
    //        atLeastOneJobName = true;
    //        break;
    //      }
    //    }
    //    // Assert.assertTrue(atLeastOneJobName);
    //
    //    list = jdbcJobStore.getSqlMapClientTemplate().queryForList("nextThisNodeJobs2", map);
    //    atLeastOneJobName = false;
    //    for (TriggerJobDO jj : list) {
    //      if (StringUtils.isNotBlank(jj.getJobName())) {
    //        atLeastOneJobName = true;
    //        break;
    //      }
    //    }
    //    Assert.assertTrue(atLeastOneJobName);
    //
    //    map.put("untilOwn", new Date(System.currentTimeMillis() + TimeInMillis.DAY * 10));
    //    map.put("untilForeign", new Date(System.currentTimeMillis() + TimeInMillis.DAY * 10));
    //
    //    list = jdbcJobStore.getSqlMapClientTemplate().queryForList("nextJobs2", map);
    //    atLeastOneJobName = false;
    //    for (TriggerJobDO jj : list) {
    //      if (StringUtils.isNotBlank(jj.getJobName())) {
    //        atLeastOneJobName = true;
    //        break;
    //      }
    //    }
    //    Assert.assertTrue(atLeastOneJobName);
  }

}