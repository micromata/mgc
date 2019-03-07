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

package de.micromata.genome.chronos;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.Stringifiable;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.ram.RamJobStore;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.types.TimeInMillis;

/**
 * Some basic tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NewJobsTest extends BaseSchedulerTestCase
{
  static final Logger LOG = Logger.getLogger(NewJobsTest.class);

  @Before
  public void initJob()
  {

    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    SchedulerManager schedmanager = SchedulerManager.get();
    RamJobStore rjs = (RamJobStore) scheddao.getJobStore();
    rjs._clearJobStore();
    scheddao.getDispatcher().setMinNodeBindTime(TimeInMillis.SECOND * 5);
  }

  @Before
  public void beforeMethod()
  {
    //    org.junit.Assume.assumeTrue(inContinuousRun == false);
  }

  public static class SimpleJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      // log.warn("SimpleJob.call");
      return null;
    }
  }

  public static long getJobResultCount()
  {
    return ChronosServiceManager.get().getSchedulerDAO().getJobStore().getJobResultCount(null);
  }

  public static long getJobResultCount(String schedname, State state)
  {
    JobStore jobstore = ChronosServiceManager.get().getSchedulerDAO().getJobStore();
    if (schedname == null) {
      return jobstore.getJobResultCount(state);
    }
    int result = 0;
    for (TriggerJobDO j : jobstore.findJobs(null, null, null, schedname, 10000)) {
      List<JobResultDO> results = jobstore.getResults(j, 10000);
      if (state == null) {
        result += results.size();
      } else {
        for (JobResultDO jr : results) {
          if (jr.getState() != state) {
            continue;
          }
          ++result;
        }
      }
    }
    return result;
  }

  /**
   * Zählt die Jobs in der Tabelle.
   * 
   * @return the count of the jobs
   */
  public static long getJobCount()
  {
    return ChronosServiceManager.get().getSchedulerDAO().getJobStore().getJobCount(null);
  }

  public static long getJobCount(String schedulerName, State state)
  {
    return ChronosServiceManager.get().getSchedulerDAO().getJobStore()
        .findJobs(null, null, state == null ? null : state.name(), schedulerName, 10000).size();
  }

  public static void sleep(long ms)
  {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {

      LOG.fatal("Exception encountered " + ex, ex);
    }
  }

  @Test
  public void testRun()
  {
    final String testSched = "testRun";
    long oldJobResultCount = getJobResultCount(testSched, null);

    long oldJobCount = getJobCount(testSched, null);

    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();

    scheddao.getScheduler(testSched);
    scheddao.submit(testSched, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("+1"));
    sleep(3000);
    Assert.assertEquals(getJobResultCount(testSched, null) - oldJobResultCount, 0);
    Assert.assertEquals(getJobCount(testSched, null) - oldJobCount, 0);
  }

  public static class FailJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      throw new RuntimeException("Failure");
    }
  }

  @Test
  public void testFailedJob()
  {
    long oldJobResultCount = getJobResultCount();
    long oldJobCount = getJobCount();
    final String testname = "testFailed";

    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    scheddao.getScheduler(testname);
    scheddao.submit(testname, new ClassJobDefinition(FailJob.class), null, createTriggerDefinition("+1"));
    sleep(10000);
    Assert.assertEquals(1, getJobResultCount() - oldJobResultCount);
    Assert.assertEquals(1, getJobCount() - oldJobCount);
  }

  static boolean doServiceFail = false;

  public static class ServiceUnavilableJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      if (doServiceFail == true) {
        throw new ServiceUnavailableException("Failure");
      }
      semaphore = true;
      return null;
    }
  }

  /**
   * Dieser Job schlägt 4 mal fehl.
   * 
   * @author worker@micromata.de
   * 
   */
  public static class RetryJob extends AbstractFutureJob
  {
    private static int retries = 0;

    public static int getRetries()
    {
      return retries;
    }

    public static void setRetries(int retries)
    {
      RetryJob.retries = retries;
    }

    @Override
    public Object call(Object argument) throws Exception
    {
      retries = retries + 1;
      if (retries < 5) {
        LOG.warn("RetryJob do retry");
        throw new JobRetryException("Try it later");
      }
      LOG.warn("RetryJob do NOT retry");
      return null;
    }
  }

  public static class RetryNextJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      throw new RetryNextRunException("Test nexRun");
    }
  }

  static boolean semaphore = false;

  @Test
  public void testRetryNextRun()
  {
    LoggingServiceManager.get().getLogConfigurationDAO().setThreshold(LogLevel.Trace);
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    try {

      long oldJobCount = getJobCount();
      final String testname = "testRetryNextRun";

      Scheduler scheduler = scheddao.getScheduler(testname);
      long oldJobResultCount = getJobResultCount(testname, null);
      SchedulerDO sched = scheduler.getDO();
      sched.setServiceRetryTime(2);
      sched.setThreadPoolSize(1);
      sched.setJobRetryTime(100);
      sched.setNodeBindingTimeout(100);
      scheddao.persist(sched);

      scheddao.submit(testname, new ClassJobDefinition(RetryNextJob.class), null, createTriggerDefinition("* * * * *"));
      sleep(1000);
      System.out.println("please wait a minute");
      long startT = System.currentTimeMillis();
      long maxTime = TimeInMillis.MINUTE * 2;
      while (getJobResultCount(testname, null) - oldJobResultCount == 0) {
        sleep(1000);
        if (System.currentTimeMillis() > startT + maxTime) {
          throw new RuntimeException("Hmm job did't start within 2 minute");
        }
      }
    } finally {
      LoggingServiceManager.get().getLogConfigurationDAO().setThreshold(LogLevel.Note);
    }
  }

  @Test
  public void testServiceUnabailbleJob()
  {
    long oldJobResultCount = getJobResultCount();
    long oldJobCount = getJobCount();
    final String testname = "testServiceUnabailble";

    doServiceFail = true;
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    Scheduler scheduler = scheddao.getScheduler(testname);
    SchedulerDO sched = scheduler.getDO();
    sched.setServiceRetryTime(2);
    sched.setThreadPoolSize(1);
    scheddao.persist(sched);

    scheddao.submit(testname, new ClassJobDefinition(ServiceUnavilableJob.class), null, createTriggerDefinition("+1"));
    sleep(3000);
    assert (getJobResultCount() - oldJobResultCount > 0);
    Assert.assertEquals(1, getJobCount() - oldJobCount);
    // Schalter für den Job, dass er jetzt laufen kann

    doServiceFail = false;
    MaxRunChecker rc = new MaxRunChecker(TimeInMillis.MINUTE * 2, "testServiceUnabailbleJob");
    while (!semaphore) {
      sleep(1000);
      rc.check();
    }

    assert (getJobResultCount(testname, State.RETRY) > 0);
    // LOG.debug(getJobCount(State.FINISHED));
    LOG.debug(getJobCount());
    Assert.assertEquals(getJobCount(testname, State.FINISHED), 1);
  }

  // @Test
  public void testSchedulerRestart()
  {

    final String testname = "testSchedulerRestart";
    long oldJobCount = getJobCount();
    long oldJobResultCount = getJobResultCount();
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    final Scheduler sched = scheddao.getScheduler(testname);
    // int prevSize = sched.getThreadPoolSize();
    // JdbcTemplate jdbc = DBAccess.getJdbcTemplate(DBRole.PopScheduler);
    // jdbc.update("update VW_TA_JCHRONOS_SCHEDULER set POOL_SIZE = 0 where NAME = '" + testSched + "'");

    // keine Threads
    scheddao.denyNewJobs(testname);

    for (int i = 0; i < 10; ++i) {
      scheddao.submit(testname, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("+1"));
    }
    sleep(500);
    Assert.assertEquals(10, getJobCount() - oldJobCount);
    scheddao.setJobCount(10, testname);

    sleep(2500);
    Assert.assertEquals(0, getJobCount() - oldJobCount);
  }

  public static int jobResultCount = 0;

  public static class JobWithResultJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      ++jobResultCount;
      return new Pair<String, Integer>("Dies ist ein Ergebnis", jobResultCount);
    }
  }

  @Test
  public void testJobsWithResult()
  {

    final String testname = "testJobsWithResult";
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    scheddao.getScheduler(testname);
    long oldJobCount = getJobCount();
    long oldJobResultCount = getJobResultCount();
    int jc = 1;
    for (int i = 0; i < jc; ++i) {
      scheddao.submit(testname, new ClassJobDefinition(JobWithResultJob.class), null, createTriggerDefinition("+1"));
      // sleep(1);
    }
    sleep(6000);
    Assert.assertEquals(getJobCount() - oldJobCount, jc);
    Assert.assertEquals(getJobResultCount() - oldJobResultCount, jc);
  }

  public static class SimpleSingletonJob extends AbstractFutureJob implements JobDefinition, FutureJob, Stringifiable
  {
    public static SimpleSingletonJob job = new SimpleSingletonJob();

    public static String stringified = SimpleSingletonJob.class.getName() + ":bla";

    @Override
    public Object call(Object argument) throws Exception
    {
      return null;
    }

    @Override
    public String asString()
    {
      return stringified;
    }

    public static Object createFromString(String arg)
    {
      return job;
    }

    @Override
    public FutureJob getInstance()
    {
      return job;
    }
  }

  /**
   * Performance test: Vor Umbau: 55 ms Nach Umbau: 30 ms SingeltonJob: 11 ms
   */
  public void _testMassSchedulerRestart()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    long oldJobCount = getJobCount();
    long oldJobResultCount = getJobResultCount();
    final String testSched = "testMassSchedulerRestart";
    int schedThreadCount = 2;
    int jc = 1000;

    Scheduler sched = scheddao.getScheduler(testSched);
    int prevSchedThreadCount = sched.getThreadPoolSize();
    scheddao.setJobCount(schedThreadCount, testSched);

    // int prevSize = sched.getThreadPoolSize();
    // JdbcTemplate jdbc = DBAccess.getJdbcTemplate(DBRole.PopScheduler);
    StopWatch sw = new StopWatch();
    sw.start();

    for (int i = 0; i < jc; ++i) {
      scheddao.submit(testSched, SimpleSingletonJob.job/* new ClassJobDefinition(SimpleJob.class) */, null,
          createTriggerDefinition("+1"));
      // sleep(1);
    }
    MaxRunChecker rc = new MaxRunChecker(TimeInMillis.MINUTE * 2, "testServiceUnabailbleJob");
    while (getJobCount() > 0) {
      sleep(1000);
      rc.check();
    }
    sw.stop();
    long time = sw.getTime();
    LOG.info("" + jc + " Jobs needs: " + time / 1000 + " secs. " + time / jc + " ms per job");
    scheddao.setJobCount(prevSchedThreadCount, testSched);
    Assert.assertEquals(0, getJobCount() - oldJobCount);
  }

  @Test
  public void testJobRetries()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    LoggingServiceManager.get().getLogConfigurationDAO().setThreshold(LogLevel.Trace);
    try {
      final String schedulerName = "testJobRetries";
      long oldJobCount = getJobCount(schedulerName, State.FINISHED);

      long oldJobResultCount = getJobResultCount(schedulerName, State.RETRY);

      final Scheduler scheduler = scheddao.getScheduler(schedulerName);
      SchedulerDO sched = scheduler.getDO();
      sched.setJobMaxRetryCount(10);
      sched.setJobRetryTime(2); // 2 seconds
      scheddao.persist(sched);
      RetryJob.setRetries(0);
      scheddao.submit(schedulerName, new ClassJobDefinition(RetryJob.class), null, createTriggerDefinition("+1"));
      long startT = System.currentTimeMillis();
      while (RetryJob.getRetries() < 5) {
        sleep(1000);
        if ((System.currentTimeMillis() - startT) > TimeInMillis.MINUTE) {
          break;
        }
        if (getJobResultCount(schedulerName, State.RETRY) - oldJobResultCount == 4
            && getJobCount(schedulerName, State.FINISHED) - oldJobCount == 1) {
          break;
        }

      }

      Assert.assertEquals(4, getJobResultCount(schedulerName, null) - oldJobResultCount);
      sleep(5000);
      Assert.assertEquals(1, getJobCount(schedulerName, null) - oldJobCount);
      Assert.assertEquals(1, getJobCount(schedulerName, State.FINISHED) - oldJobCount);
    } finally {
      LoggingServiceManager.get().getLogConfigurationDAO().setThreshold(LogLevel.Note);
    }
  }

  @Test
  @Ignore
  public void testRestartDispatcher() throws InterruptedException
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    final String schedulerName = "testRestart";
    final SchedulerManager manager = SchedulerManager.get();
    scheddao.getScheduler(schedulerName);

    for (int i = 0; i < 30; ++i) {
      scheddao.submit(schedulerName, SimpleSingletonJob.job, null, createTriggerDefinition("+1"));
      // sleep(1);
    }

    scheddao.shutdown();
    scheddao.restart();
    // assert (getJobResultCount(State.RETRY) == 4);
    // assertEquals(getJobCount(State.FINISHED), 1);
  }

  @Test
  public void testJobRemove()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    final String schedulerName = "testJobRemove";
    long oldJobCount = getJobCount(schedulerName, State.FINISHED);
    final Date now = new Date();
    final Date untilDate = new Date(24 * 60 * 10000 + now.getTime());

    final SchedulerManager manager = SchedulerManager.get();
    final Scheduler sched = scheddao.getScheduler(schedulerName);
    scheddao.submit(schedulerName, new ClassJobDefinition(JobWithResultJob.class), null, createTriggerDefinition("+1"));
    MaxRunChecker rc = new MaxRunChecker(TimeInMillis.MINUTE * 2, "testJobRemove");
    while (true) {
      long jbrc = getJobCount(schedulerName, State.FINISHED);
      if (jbrc - oldJobCount == 1) {
        break;
      }
      sleep(300);
      rc.check();
    }
    JobStore jobStore = scheddao.getJobStore();
    final List<TriggerJobDO> jobs = jobStore.getJobs(sched, now, untilDate, State.FINISHED);
    Assert.assertTrue(jobs.size() > 0);
    // Assert.assertEquals(1, jobs.size() - oldJobCount);

    TriggerJobDO job = jobs.get(jobs.size() - 1);
    Assert.assertTrue(job.getCurrentResultPk() != null);
    JobResultDO result = jobStore.getResultByPk(job.getCurrentResultPk());
    jobStore.jobResultRemove(job, result, sched);
    jobStore.jobRemove(job, null, sched);
    Assert.assertEquals(0, getJobCount(schedulerName, State.FINISHED) - oldJobCount);
  }

  @Test
  public void testJobRemoveByRef()
  {
    final String schedulerName = "testJobRemove";
    long oldJobCount = getJobCount(schedulerName, State.FINISHED);
    final Date now = new Date();
    final Date untilDate = new Date(24 * 60 * 10000 + now.getTime());

    final SchedulerManager manager = SchedulerManager.get();
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    final Scheduler sched = scheddao.getScheduler(schedulerName);
    final long ref = scheddao.submit(schedulerName, new ClassJobDefinition(JobWithResultJob.class), null,
        createTriggerDefinition("+1"));
    MaxRunChecker rc = new MaxRunChecker(TimeInMillis.MINUTE * 2, "testJobRemoveByRef");
    while (true) {
      long jbrc = getJobCount(schedulerName, State.FINISHED);
      if (jbrc - oldJobCount == 1) {
        break;
      }
      sleep(300);
      rc.check();

    }

    // final List<TriggerJobDO> jobs = sched.getJobs(now, untilDate, State.FINISHED);
    // Assert.assertTrue(jobs.size() > 0);
    // Assert.assertEquals(1, jobs.size() - oldJobCount);
    JobStore jobStore = scheddao.getJobStore();
    TriggerJobDO job = jobStore.getJobByPk(ref);
    Assert.assertTrue(job != null);
    Assert.assertTrue(job.getCurrentResultPk() != null);
    JobResultDO result = jobStore.getResultByPk(job.getCurrentResultPk());
    jobStore.jobResultRemove(job, result, sched);
    jobStore.jobRemove(job, null, sched);
    Assert.assertEquals(0, getJobCount(schedulerName, State.FINISHED) - oldJobCount);
  }
}
