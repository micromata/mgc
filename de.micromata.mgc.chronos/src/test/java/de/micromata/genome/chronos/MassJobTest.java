package de.micromata.genome.chronos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.RuntimeIOException;

/**
 * Creates a lot of jobs and execute it.
 * 
 * check latency and performance of executing many jobs.
 * 
 * This Test should not be executed on CI.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class MassJobTest extends BaseSchedulerTestCase
{
  String schedName = "massTest";

  private static int executedJobs = 0;

  public static class MassJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      //      sleep(250);
      ++executedJobs;
      return null;
    }
  }

  private void updateSchedThreads(String name, int threadCount)
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    Scheduler sched = scheddao.getScheduler(name);
    SchedulerDO scheddo = sched.getDO();
    scheddo.setThreadPoolSize(threadCount);
    scheddao.persist(scheddo);
  }

  @Test
  public void createMassJobs()
  {
    LoggingServiceManager.get().getLogConfigurationDAO().setThreshold(LogLevel.Debug);
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    scheddao.getDispatcher().setMinNodeBindTime(10000);
    Scheduler sched = scheddao.getScheduler(schedName);
    updateSchedThreads(schedName, 0);
    sleep(1000);

    int count = 15000;
    for (int i = 0; i < count; ++i) {
      scheddao.submit(schedName, new ClassJobDefinition(MassJob.class), null, createTriggerDefinition("+1"));
    }
    updateSchedThreads(schedName, 10);

    waitForEnd();
  }

  private void waitForEnd()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      do {
        String rl = in.readLine();
        if (StringUtils.equalsIgnoreCase(rl, "stop") == true) {
          break;
        }
        JobStore jobstore = scheddao.getJobStore();
        List<? extends TriggerJobDO> jc = jobstore.findJobs(null, null, null, schedName, 100000);
        System.out.println("Job called: " + executedJobs + "; in sched: " + jc.size());
      } while (true);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }
}
