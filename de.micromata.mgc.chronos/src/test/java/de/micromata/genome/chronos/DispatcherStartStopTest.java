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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.util.ClassJobDefinition;

public class DispatcherStartStopTest extends BaseSchedulerTestCase
{
  public static final String schedulerName = "DispatcherStartStopTest";

  @Before
  public void beforeMethod()
  {
    //    org.junit.Assume.assumeTrue(inContinuousRun == false);
  }

  @Test
  public void testRestart()
  {

    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();

    scheddao.getScheduler(schedulerName);
    jobCalled = 0;
    scheddao.submit(schedulerName, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("+1"));
    sleep(1000);
    Assert.assertEquals(1, jobCalled);

    scheddao.shutdown();

    jobCalled = 0;
    scheddao.submit(schedulerName, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("+1"));
    sleep(1000);
    Assert.assertEquals(0, jobCalled);
    jobCalled = 0;
    scheddao.restart();
    SchedulerManager.get();
    sleep(1000);
    Assert.assertEquals(1, jobCalled);

  }
}
