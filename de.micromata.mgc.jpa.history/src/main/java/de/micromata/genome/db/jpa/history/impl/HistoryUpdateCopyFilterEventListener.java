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

package de.micromata.genome.db.jpa.history.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrUpdateCopyFilterEvent;

/**
 * The listener interface for receiving historyUpdateCopyFilterEvent events. The class that is interested in processing
 * a historyUpdateCopyFilterEvent event implements this interface, and the object created with that class is registered
 * with a component using the component's addHistoryUpdateCopyFilterEventListener  method. When the
 * historyUpdateCopyFilterEvent event occurs, that object's appropriate method is invoked.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryUpdateCopyFilterEventListener implements EmgrEventHandler<EmgrUpdateCopyFilterEvent>
{

  @Override
  public void onEvent(EmgrUpdateCopyFilterEvent event)
  {
    HistoryService historyService = HistoryServiceManager.get().getHistoryService();
    List<WithHistory> whanots = historyService.internalFindWithHistoryEntity(event.getTarget());
    if (whanots.isEmpty() == true) {
      event.nextFilter();
      return;
    }
    Serializable entPk = event.getTarget().getPk();

    Class<?> entClass = event.getTarget().getClass();

    Object prev = event.getTarget();
    Map<String, HistProp> oprops = historyService.internalGetPropertiesForHistory(event.getEmgr(), whanots, prev);

    event.nextFilter();

    Map<String, HistProp> nprops = historyService.internalGetPropertiesForHistory(event.getEmgr(), whanots,
        event.getTarget());
    historyService.internalOnUpdate(event.getEmgr(), entClass.getName(), entPk, oprops, nprops);
  }
}
