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

package de.micromata.genome.logging.events;

import de.micromata.genome.util.event.MgcEventListener;
import de.micromata.genome.util.event.MgcFilterEvent;

/**
 * A service for listen on Logging services.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LoggingEventListenerRegistryService
{
  /**
   * Register a class, which should be called in Logging.
   * 
   * @param listenerClass the class to listen to
   */
  <EVENT extends LoggingEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      Class<LISTENER> listenerClass);

  void submitEvent(LoggingEvent event);

  <R, E extends MgcFilterEvent<R>> R filterEvent(E event, MgcEventListener<E> execute);

}
