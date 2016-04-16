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

package de.micromata.genome.util.event;

/**
 * Holds MgcEventListener instances.
 * 
 * Note: Don't use, if the listener may come from dynamic modules and the registry will be held by non-dynamic module.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface McEventInstanceRegistry extends MgcEventRegistry
{

  /**
   * Register an event listener.
   *
   * @param <EVENT> the generic type
   * @param <LISTENER> the generic type
   * @param listenerInstance the listener instance
   */
  <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      LISTENER listenerInstance);

  /**
   * Use this only, if you are sure, that listener accepts the type event.
   * 
   * May be usefull, in cases where lamda expression and you know the type of event.
   * 
   * public void addOnEvent(MgcEventListener TestFilter1Event handler) { addEventListener(TestFilter1Event.class,
   * handler); }
   *
   * @param event the event
   * @param listener the listener
   */
  void addEventListener(Class<? extends MgcEvent> event, MgcEventListener<?> listener);
}
