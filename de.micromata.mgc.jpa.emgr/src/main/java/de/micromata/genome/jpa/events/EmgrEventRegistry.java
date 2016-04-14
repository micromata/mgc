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

package de.micromata.genome.jpa.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.runtime.ClassUtils;

/**
 * Registry of Events used inside JPA.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrEventRegistry
{

  /**
   * All handlers registerd to events.
   */
  private Map<Class<? extends EmgrEvent>, List<EmgrEventHandler>> registeredEvents = new HashMap<>();
  /**
   * For fast access all super arguments classes of an event.
   */
  private Map<Class<? extends EmgrEvent>, Set<Class<? extends EmgrEvent>>> allSuperClasses = new HashMap<>();

  /**
   * Register event.
   *
   * @param eventHandler the event handler
   */
  public void registerEvent(EmgrEventHandler<?> eventHandler)
  {
    Class<? extends EmgrEvent> argType = ClassUtils.getGenericTypeArgument(eventHandler.getClass(),
        EmgrEvent.class);

    if (argType == null) {
      /**
       * @logging
       * @reason Cannot determine argument type of listener
       * @action Check Backend Listener configuration
       */
      GLog.error(GenomeLogCategory.Configuration, "Cannot determine argument type of Event Lister: " + eventHandler);
      return;
    }

    registeredEvents.putIfAbsent(argType, new ArrayList<>());
    registeredEvents.get(argType).add(eventHandler);
    collectSuperEvents(argType);
  }

  /**
   * Collect super events.
   *
   * @param event the event
   */
  private void collectSuperEvents(Class<? extends EmgrEvent> event)
  {
    if (allSuperClasses.containsKey(event) == true) {
      return;
    }
    allSuperClasses.put(event, ClassUtils.getAllSuperImplementing(event, EmgrEvent.class));
  }

  /**
   * Invoke events.
   *
   * @param event the event
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void invokeEvents(EmgrEvent event)
  {
    Set<Class<? extends EmgrEvent>> ecls = allSuperClasses.get(event.getClass());
    if (ecls == null) {
      return;
    }

    for (Class<? extends EmgrEvent> ec : ecls) {
      List<EmgrEventHandler> list = registeredEvents.get(ec);
      if (list == null) {
        continue;
      }
      for (EmgrEventHandler eh : list) {
        eh.onEvent(event);
      }
    }
  }

  /**
   * Collect event handler.
   *
   * @param event the event
   * @return the list
   */
  private List<EmgrEventHandler> collectEventHandler(EmgrFilterEvent event)
  {
    List<EmgrEventHandler> ret = new ArrayList<>();
    Set<Class<? extends EmgrEvent>> ecls = allSuperClasses.get(event.getClass());
    if (ecls == null) {
      return ret;
    }

    for (Class<? extends EmgrEvent> ec : ecls) {
      List<EmgrEventHandler> list = registeredEvents.get(ec);
      if (list == null) {
        continue;
      }
      for (EmgrEventHandler eh : list) {
        ret.add(eh);
      }
    }
    return ret;
  }

  /**
   * Invoke events.
   *
   * @param <E> the element type
   * @param <R> the generic type
   * @param event the event
   * @param nested the nested
   * @return the r
   */
  public <E extends EmgrFilterEvent, R> R invokeEvents(EmgrFilterEvent<R> event, EmgrEventHandler<E> nested)
  {
    List<EmgrEventHandler> handlerList = collectEventHandler(event);
    handlerList.add(nested);
    event.setEventHandlerChain(handlerList);
    event.nextFilter();
    return event.getResult();
  }
}
