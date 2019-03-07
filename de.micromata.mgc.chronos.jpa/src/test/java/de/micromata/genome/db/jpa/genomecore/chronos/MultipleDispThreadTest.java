package de.micromata.genome.db.jpa.genomecore.chronos;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.util.ClassJobDefinition;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MultipleDispThreadTest extends JpaBaseSchedulerTestCase
{

  TestJpaSchedulerImpl secondScheduler;
  SchedulerDAO firstScheduler;
  Dispatcher secDispatcher;

  @Before
  public void setUp2()
  {
    long minNodeBindTimeout = 100;
    firstScheduler = ChronosServiceManager.get().getSchedulerDAO();
    firstScheduler.getDispatcher().setMinNodeBindTime(minNodeBindTimeout);
    secondScheduler = new TestJpaSchedulerImpl();
    SchedulerManager manager = new SchedulerManager();
    manager.setMinNodeBindTime(minNodeBindTimeout);
    secondScheduler.init(manager);
    try {
      secDispatcher = secondScheduler.createDispatcher(manager.getVirtualHostName());
      secDispatcher.setMinNodeBindTime(manager.getMinNodeBindTime());
      secDispatcher.startup();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Manual Test only. Test with 2 Scheduler which are runing on same node name and fight for the jobs to execute. With
   * this test GENOME-1768 was to be reproduced.
   * 
   * @throws IOException when an error happened
   */
  //@Test
  @Ignore
  public void doMultipleJob() throws IOException
  {
    final String testSched = "testRunMultiple";
    firstScheduler.getScheduler(testSched);
    firstScheduler.submit(testSched, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("p1"));
    secondScheduler.submit(testSched, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("p1"));
    // run endless
    System.in.read();
  }
}
