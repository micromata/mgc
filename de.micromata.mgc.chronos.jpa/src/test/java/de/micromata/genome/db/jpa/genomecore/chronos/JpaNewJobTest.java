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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobRetryException;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.RetryNextRunException;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.ServiceUnavailableException;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.Stringifiable;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.types.TimeInMillis;

/**
 * The Class JpaNewJobTest.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaNewJobTest extends JpaBaseSchedulerTestCase
{

  /**
   * The Constant LOG.
   */
  static final Logger LOG = Logger.getLogger(JpaNewJobTest.class);

  @Before
  public void setNodeBindTimeOut()
  {
    ChronosServiceManager.get().getSchedulerDAO().getDispatcher().setMinNodeBindTime(TimeInMillis.SECOND * 5);
  }

  /**
   * Before method.
   */
  @Before
  public void beforeMethod()
  {
    //org.junit.Assume.assumeTrue(inContinuousRun == false);
  }

  // private static ApplicationContext factory = null;

  // protected static Object getBean(final String beanname)
  // {
  // if (factory == null) {
  // factory = new ClassPathXmlApplicationContext("testApplicationContext.xml");
  // factory.getBean("daoManager");
  // }
  // return factory.getBean(beanname);
  // }
  //
  // @Override
  // protected void setUp()
  // {
  // //Object dm = getBean("daoManager");
  // //assertNotNull("DaoManager instance not found", dm);
  // DaoManager.get().setTestModus("DEV");
  // SchedulerManager.setDiscriminator("testhost_");
  // // PropertyConfigurator logcfg = new PropertyConfigurator();
  // PropertyConfigurator.configure("dev/extrc/config/log4j.properties");
  // // DynDaoManager.initForTestCase();
  //
  // }

  /**
   * The Class SimpleJob.
   */
  public static class SimpleJob extends AbstractFutureJob
  {

    @Override
    public Object call(Object argument) throws Exception
    {
      // log.warn("SimpleJob.call");
      return null;
    }
  }

  /**
   * Gets the job result count.
   *
   * @return the job result count
   */
  public static long getJobResultCount()
  {
    return ChronosServiceManager.get().getSchedulerDAO().getJobStore().getJobResultCount(null);
  }

  /**
   * Gets the job result count.
   *
   * @param schedname the schedname
   * @param state the state
   * @return the job result count
   */
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
   * @return the job count
   */
  public static long getJobCount()
  {
    return ChronosServiceManager.get().getSchedulerDAO().getJobStore().getJobCount(null);
  }

  /**
   * Gets the job count.
   *
   * @param schedulerName the scheduler name
   * @param state the state
   * @return the job count
   */
  public static long getJobCount(String schedulerName, State state)
  {
    return ChronosServiceManager.get().getSchedulerDAO().getJobStore()
        .findJobs(null, null, state == null ? null : state.name(), schedulerName, 10000).size();
  }

  /**
   * Sleep.
   *
   * @param ms the ms
   */
  public static void sleep(long ms)
  {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {

      LOG.fatal("Exception encountered " + ex, ex);
    }
  }

  /**
   * Test run.
   */
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

  /**
   * The Class FailJob.
   */
  public static class FailJob extends AbstractFutureJob
  {

    @Override
    public Object call(Object argument) throws Exception
    {
      throw new RuntimeException("Failure");
    }
  }

  /**
   * Test failed job.
   */
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

  /**
   * The do service fail.
   */
  static boolean doServiceFail = false;

  /**
   * The Class ServiceUnavilableJob.
   */
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

    /**
     * The retries.
     */
    private static int retries = 0;

    /**
     * Gets the retries.
     *
     * @return the retries
     */
    public static int getRetries()
    {
      return retries;
    }

    /**
     * Sets the retries.
     *
     * @param retries the new retries
     */
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

  /**
   * The Class RetryNextJob.
   */
  public static class RetryNextJob extends AbstractFutureJob
  {

    @Override
    public Object call(Object argument) throws Exception
    {
      throw new RetryNextRunException("Test nexRun");
    }
  }

  /**
   * The semaphore.
   */
  static volatile boolean semaphore = false;

  /**
   * Test retry next run.
   */
  @Test
  public void testRetryNextRun()
  {
    LoggingServiceManager.get().getLogConfigurationDAO().setThreshold(LogLevel.Trace);
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    try {

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

  /**
   * Test service unabailble job.
   */
  @Test
  public void testServiceUnabailbleJob()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    long oldJobResultCount = getJobResultCount();
    long oldJobCount = getJobCount();
    final String testname = "testServiceUnabailble";

    doServiceFail = true;

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

  /**
   * Test scheduler restart.
   */
  // @Test
  public void testSchedulerRestart()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    final String testname = "testSchedulerRestart";
    long oldJobCount = getJobCount();

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

  /**
   * The job result count.
   */
  public static int jobResultCount = 0;

  /**
   * The Class JobWithResultJob.
   */
  public static class JobWithResultJob extends AbstractFutureJob
  {

    @Override
    public Object call(Object argument) throws Exception
    {
      ++jobResultCount;
      return new Pair<String, Integer>("Dies ist ein Ergebnis", jobResultCount);
    }
  }

  /**
   * Test jobs with result.
   */
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

  /**
   * The Class SimpleSingletonJob.
   */
  public static class SimpleSingletonJob extends AbstractFutureJob implements JobDefinition, FutureJob, Stringifiable
  {

    /**
     * The job.
     */
    public static SimpleSingletonJob job = new SimpleSingletonJob();

    /**
     * The stringified.
     */
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

    /**
     * Creates the from string.
     *
     * @param arg the arg
     * @return the object
     */
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
   * Performance test: Vor Umbau: 55 ms Nach Umbau: 30 ms SingeltonJob: 11 ms.
   */
  public void _testMassSchedulerRestart()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    long oldJobCount = getJobCount();
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

  /**
   * Test job retries.
   */
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

  /**
   * Test restart dispatcher.
   *
   * @throws InterruptedException the interrupted exception
   */
  @Test
  @Ignore
  public void testRestartDispatcher() throws InterruptedException
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    final String schedulerName = "testRestart";

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

  /**
   * Test job remove.
   */
  @Test
  public void testJobRemove()
  {
    final String schedulerName = "testJobRemove";
    long oldJobCount = getJobCount(schedulerName, State.FINISHED);
    final Date now = new Date();
    final Date untilDate = new Date(24 * 60 * 10000 + now.getTime());

    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
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

  /**
   * Test job remove by ref.
   */
  @Test
  public void testJobRemoveByRef()
  {
    final String schedulerName = "testJobRemove";
    long oldJobCount = getJobCount(schedulerName, State.FINISHED);

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
