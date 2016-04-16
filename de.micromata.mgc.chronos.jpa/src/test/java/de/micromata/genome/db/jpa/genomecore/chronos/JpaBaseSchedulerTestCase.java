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

import org.junit.After;
import org.junit.Before;

import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.util.TriggerJobUtils;
import de.micromata.mgc.common.test.MgcTestCase;

public abstract class JpaBaseSchedulerTestCase extends MgcTestCase
{
  public static int jobCalled = 0;

  public synchronized static void incJobCalled()
  {
    ++jobCalled;
  }

  public synchronized static int getJobCalled()
  {
    return jobCalled;
  }

  public static class SimpleJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      sleep(100);
      // System.out.println("SJ");
      incJobCalled();
      return null;
    }
  }

  @Before
  public void setUp()
  {
    //    DynDaoManager.threadInitDynDaoManager();
    JpaSchedulerImpl schedImpl = new JpaSchedulerImpl();
    ChronosServiceManager.get().setSchedulerDAO(schedImpl);
  }

  @After
  public void tearDown()
  {
    //    DynDaoManager.threadDeInitDynDaoManager();
  }

  public void createTestSched(String testname)
  {
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();
    Scheduler scheduler = scheddao.getScheduler(testname);
    SchedulerDO sched = scheduler.getDO();
    sched.setServiceRetryTime(5);
    sched.setThreadPoolSize(1);
    scheddao.persist(sched);
  }

  public static Trigger createTriggerDefinition(final String definition)
  {
    return TriggerJobUtils.createTriggerDefinition(definition);
  }

}
