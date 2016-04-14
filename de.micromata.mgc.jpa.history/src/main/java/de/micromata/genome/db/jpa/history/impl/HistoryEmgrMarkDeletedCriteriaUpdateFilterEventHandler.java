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

import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.db.jpa.history.entities.EntityOpType;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.MarkDeletableRecord;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent;
import de.micromata.genome.jpa.events.EmgrMarkDeletedCriteriaUpdateFilterEvent;

/**
 * The Class HistoryEmgrMarkDeletedCriteriaUpdateFilterEventHandler.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryEmgrMarkDeletedCriteriaUpdateFilterEventHandler
    implements EmgrEventHandler<EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent<? extends MarkDeletableRecord<?>>>
{

  @Override
  public void onEvent(EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent<? extends MarkDeletableRecord<?>> event)
  {
    event.nextFilter();
    if (event.getResult() == 1) {
      Object ent = event.getEntity();
      HistoryService historyService = HistoryServiceManager.get().getHistoryService();
      List<WithHistory> whanots = historyService.internalFindWithHistoryEntity(ent);
      if (whanots.isEmpty() == true) {
        return;
      }
      Serializable entPk = ((DbRecord<?>) ent).getPk();
      Class<?> entClass = ent.getClass();
      EntityOpType optype = (event instanceof EmgrMarkDeletedCriteriaUpdateFilterEvent)
          ? EntityOpType.MarkDeleted : EntityOpType.UmarkDeleted;

      historyService.internalOnMarkUnmarkDeleted(event.getEmgr(), optype, whanots, entClass.getName(), entPk, ent);
    }
  }

}
