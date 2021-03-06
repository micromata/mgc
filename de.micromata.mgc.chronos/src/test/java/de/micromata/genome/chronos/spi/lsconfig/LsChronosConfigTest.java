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

package de.micromata.genome.chronos.spi.lsconfig;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.micromata.genome.chronos.BaseSchedulerTestCase;
import de.micromata.genome.chronos.ChronosServiceManager;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.manager.DefaultChronosConfigurationServiceImpl;
import de.micromata.genome.chronos.manager.RAMSchedulerDAOImpl;
import de.micromata.genome.chronos.manager.SchedulerBaseDAO;
import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.util.SchedulerFactory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsChronosConfigTest extends BaseSchedulerTestCase
{
  private static final Logger log = Logger.getLogger(LsChronosConfigTest.class);

  @Before
  public void resetScheduler()
  {
    SchedulerBaseDAO._resetDispatcher();
    ChronosServiceManager.get().setSchedulerDAO(new RAMSchedulerDAOImpl());
    ChronosServiceManager.get().setChronosConfigurationService(new DefaultChronosConfigurationServiceImpl());
    ChronosServiceManager.get().getChronosConfigurationService().resetScheduleManager();
  }

  @Test
  public void testLoadFromLs()
  {
    SchedulerDAO cs = ChronosServiceManager.get().getSchedulerDAO();

    SchedulerManager schedm = cs.getSchedulerManager();
    List<SchedulerFactory> facs = schedm.getScheduleFactories();
    Scheduler sched = cs.getCreateScheduler("utest1", false);

    if (sched == null) {
      log.warn("utest1 not found. SchedulerDAO: " + cs.getClass().getName());
      for (SchedulerDO s : cs.getSchedulers()) {
        log.warn("utest1 not found, Existant Scheduler: " + s.getName());
      }
    }
    Assert.assertNotNull(sched);
    Assert.assertEquals(7, sched.getThreadPoolSize());
  }
}
