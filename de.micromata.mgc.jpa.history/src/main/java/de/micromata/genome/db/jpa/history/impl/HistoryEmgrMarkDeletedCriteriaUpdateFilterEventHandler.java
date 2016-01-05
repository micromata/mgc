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
