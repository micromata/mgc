package de.micromata.genome.chronos.spi.lsconfig;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.chronos.BaseSchedulerTestCase;
import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.util.SchedulerFactory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsChronosConfigTest extends BaseSchedulerTestCase
{
  @Test
  public void testLoadFromLs()
  {
    SchedulerDAO cs = ChronosServiceManager.get().getSchedulerDAO();

    SchedulerManager schedm = cs.getSchedulerManager();
    List<SchedulerFactory> facs = schedm.getScheduleFactories();
    Scheduler sched = cs.getCreateScheduler("utest1", false);
    Assert.assertNotNull(sched);
    Assert.assertEquals(7, sched.getThreadPoolSize());
  }
}
