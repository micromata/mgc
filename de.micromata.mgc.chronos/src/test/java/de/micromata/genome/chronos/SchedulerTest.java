/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   26.01.2007
// Copyright Micromata 26.01.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import org.junit.Test;

import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.chronos.util.DelayTrigger;
import de.micromata.mgc.common.test.MgcTestCase;

public class SchedulerTest extends MgcTestCase
{
  public static class MyJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      System.out.println("Hello from Job: " + argument);
      return null;
    }

  }

  @Test
  public void testFirst()
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    Scheduler sched = scheddao.getScheduler("testSched");
    scheddao.submit("testSched", new ClassJobDefinition(MyJob.class), "from onInit delayed 3000",
        new DelayTrigger(3000));
    try {
      Thread.sleep(5000);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}
