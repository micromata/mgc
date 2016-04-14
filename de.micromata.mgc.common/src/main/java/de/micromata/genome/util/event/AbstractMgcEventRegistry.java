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

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Base class for Event registry.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractMgcEventRegistry implements MgcEventRegistry
{
  protected String registryName;

  public AbstractMgcEventRegistry()
  {

  }

  public AbstractMgcEventRegistry(String registryName)
  {
    this.registryName = registryName;
  }

  /**
   * Wrapp the invocation of the event handler.
   * 
   * @param listener the listener
   * @param event the event
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void invokeListener(MgcEventListener listener, final MgcEvent event)
  {
    listener.onEvent(event);
  }

  protected abstract List<MgcEventListener> collectEventFilter(MgcEvent event);

  @Override
  public <R, E extends MgcFilterEvent<R>> R filterEvent(E event, MgcEventListener<E> execute)
  {
    List<MgcEventListener> handlerList = collectEventFilter(event);
    handlerList.add(execute);
    event.setEventHandlerChain(handlerList);
    event.nextFilter();
    return event.getResult();
  }

  @Override
  public String getRegistryName()
  {
    if (StringUtils.isNotBlank(registryName) == true) {
      return registryName;
    }
    return MgcEventRegistry.super.getRegistryName();
  }

  public void setRegistryName(String registryName)
  {
    this.registryName = registryName;
  }

}
