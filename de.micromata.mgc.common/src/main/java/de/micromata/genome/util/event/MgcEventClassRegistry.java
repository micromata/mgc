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
 * Registry for event classes.
 * 
 * This are sensible for dynamic modules, like Genome Modules.
 * 
 * If an Event or EventListener classloader disappear (GCed), the event will not be longer dispatched.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface MgcEventClassRegistry extends MgcEventRegistry
{
  /**
   * Register an event listener.
   * 
   * @param listenerClass the listener class
   */
  <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      Class<LISTENER> listenerClass);

  /**
   * Removes a listener class.
   *
   * @param listenerClass the listener class
   */
  public <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void removeListener(
      Class<LISTENER> listenerClass);

}
