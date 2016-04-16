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

package de.micromata.mgc.chronos.test;

import de.micromata.genome.chronos.manager.ChronosSchedulerInitService;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.util.SchedulerFactory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TestChronosSchedulerInitService implements ChronosSchedulerInitService
{

  @Override
  public void initSchedulerManager(SchedulerManager schedManager)
  {
    schedManager.setMinNodeBindTime(10000);
    SchedulerFactory sf = new SchedulerFactory();
    sf.setSchedulerName("testSched");
    schedManager.getScheduleFactories().add(sf);
  }

}
