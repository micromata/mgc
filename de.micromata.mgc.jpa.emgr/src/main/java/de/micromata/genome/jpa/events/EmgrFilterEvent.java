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
import java.util.List;

import org.apache.log4j.Logger;

import de.micromata.genome.jpa.IEmgr;

/**
 * Event used by filter syntax.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic return type.
 */
public abstract class EmgrFilterEvent<R>extends EmgrEvent
{

  /**
   * The log.
   */
  private static Logger LOG = Logger.getLogger(EmgrFilterEvent.class);
  /**
   * the filter chain.
   */
  @SuppressWarnings("rawtypes")
  private List<EmgrEventHandler> eventHandlerList = new ArrayList<>();
  /**
   * position of the filter chain.
   */
  private int curIndx = 0;

  /**
   * The result.
   */
  private R result;

  /**
   * Instantiates a new emgr filter event.
   *
   * @param emgr the emgr
   */
  public EmgrFilterEvent(IEmgr<?> emgr)
  {
    super(emgr);
  }

  /**
   * Call next filter in chain.
   */
  @SuppressWarnings("unchecked")
  public void nextFilter()
  {
    if (curIndx >= eventHandlerList.size()) {
      return;
    }
    ++curIndx;
    EmgrEventHandler ehandler = eventHandlerList.get(curIndx - 1);
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("EmgrFilter dispatch " + ehandler + "(" + this + ")");
    }
    ehandler.onEvent(this);
  }

  /**
   * Gets the result.
   *
   * @return the result
   */
  public R getResult()
  {
    return result;
  }

  /**
   * Sets the result.
   *
   * @param result the new result
   */
  public void setResult(R result)
  {
    this.result = result;
  }

  /**
   * Internal method to set chain.
   *
   * @param eventHandlerList the new event handler chain
   */
  @SuppressWarnings("rawtypes")
  public void setEventHandlerChain(List<EmgrEventHandler> eventHandlerList)
  {
    curIndx = 0;
    this.eventHandlerList = eventHandlerList;
  }
}
