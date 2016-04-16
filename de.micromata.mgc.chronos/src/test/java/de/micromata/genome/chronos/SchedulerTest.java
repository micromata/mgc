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
