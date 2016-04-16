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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The Class MgcFilterEventImpl.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic type
 */
public class MgcFilterEventImpl<R> implements MgcFilterEvent<R>
{
  /**
   * The log.
   */
  private static Logger LOG = Logger.getLogger(MgcFilterEventImpl.class);
  /**
   * the filter chain.
   */
  @SuppressWarnings("rawtypes")
  private List<MgcEventListener> eventHandlerList = new ArrayList<>();
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
   * Result is initally null.
   */
  public MgcFilterEventImpl()
  {
  }

  /**
   * Instantiates a new emgr filter event.
   *
   * @param result the result
   */
  public MgcFilterEventImpl(R result)
  {
    this.result = result;
  }

  /**
   * Call next filter in chain.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void nextFilter()
  {
    if (curIndx >= eventHandlerList.size()) {
      return;
    }
    ++curIndx;
    MgcEventListener ehandler = eventHandlerList.get(curIndx - 1);
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
  @Override
  public R getResult()
  {
    return result;
  }

  /**
   * Sets the result.
   *
   * @param result the new result
   */
  @Override
  public void setResult(R result)
  {
    this.result = result;
  }

  /**
   * Internal method to set chain.
   *
   * @param eventHandlerList the new event handler chain
   */
  @Override
  @SuppressWarnings("rawtypes")
  public void setEventHandlerChain(List<MgcEventListener> eventHandlerList)
  {
    curIndx = 0;
    this.eventHandlerList = eventHandlerList;
  }
}
