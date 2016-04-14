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

package de.micromata.mgc.jpa.spring;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrEventRegistry;

/**
 * The Class SpringEmgrFilterBean.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
@Component()
public class SpringEmgrFilterBean
{

  /**
   * The application context.
   */
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * Register emgr filter.
   *
   * @param emgrFactory the emgr factory
   */
  public void registerEmgrFilter(EmgrFactory<?> emgrFactory)
  {
    Map<String, EmgrEventHandler> beans = applicationContext.getBeansOfType(EmgrEventHandler.class);
    EmgrEventRegistry eventFactory = emgrFactory.getEventFactory();
    for (EmgrEventHandler eh : beans.values()) {
      eventFactory.registerEvent(eh);
    }
  }
}
