/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   13.02.2008
// Copyright Micromata 13.02.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import org.junit.Test;

import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.chronos.util.CronTrigger;
import de.micromata.genome.chronos.util.DelayTrigger;

public class ComplexJobTest extends BaseSchedulerTestCase
{
  @Override
  public void createTestSched(String testname)
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    Scheduler scheduler = scheddao.getScheduler(testname);
    SchedulerDO sched = scheduler.getDO();
    sched.setServiceRetryTime(5);
    sched.setJobRetryTime(5);
    sched.setThreadPoolSize(1);
    scheddao.persist(sched);
  }

  public static class SimpleJob extends AbstractFutureJob
  {

    @Override
    public Object call(Object argument) throws Exception
    {
      Integer sleepTime = (Integer) argument;
      Thread.sleep(sleepTime);
      return null;
    }
  }

  public static class ForceRetryJob extends AbstractFutureJob
  {
    public static int maxRetires;

    public static int retries;

    @Override
    public Object call(Object argument) throws Exception
    {
      retries = retries + 1;
      if (retries < maxRetires) {
        throw new ForceRetryException("Try it later");
      }
      return null;
    }
  }

  public static class ServiceUnavilableJob extends AbstractFutureJob
  {
    public static boolean doServiceFail = false;

    public static boolean semaphore = false;

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

  public static Trigger createTriggerDefinition(final String definition)
  {
    if (definition.startsWith("+")) {
      return new DelayTrigger(definition);
    } else {
      return new CronTrigger(definition);
    }
  }

  @Test
  public void testComplexTest()
  {
    try {
      SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
      String sched1 = "testSched1";
      String sched2 = "testSched2";
      createTestSched(sched1);
      createTestSched(sched2);
      int maxLoops = 10;
      ForceRetryJob.maxRetires = 5;
      ForceRetryJob.retries = 0;
      scheddao.submit(sched1, new ClassJobDefinition(ForceRetryJob.class), null, createTriggerDefinition("+1"));

      for (int i = 0; i < maxLoops; ++i) {
        ServiceUnavilableJob.doServiceFail = true;
        for (int j = 0; j < 2; ++j) {
          scheddao.submit(sched2, new ClassJobDefinition(ServiceUnavilableJob.class), j * 100,
              createTriggerDefinition("+1"));
        }
        for (int j = 0; j < 5; ++j) {
          scheddao.submit(sched1, new ClassJobDefinition(SimpleJob.class), j * 100, createTriggerDefinition("+1"));
        }

        Thread.sleep(1000);
        ServiceUnavilableJob.doServiceFail = false;

      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
