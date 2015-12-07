package de.micromata.genome.db.jpa.history.impl;

import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.events.EmgrAfterInsertedEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Handles Insert events for inserting History.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HistoryEmgrAfterInsertedEventHandler implements EmgrEventHandler<EmgrAfterInsertedEvent>
{

  @Override
  public void onEvent(EmgrAfterInsertedEvent event)
  {
    Object ent = event.getEntity();
    HistoryService historyService = HistoryServiceManager.get().getHistoryService();
    List<WithHistory> whanots = historyService.internalFindWithHistoryEntity(ent);
    if (whanots.isEmpty() == true) {
      return;
    }
    Long entPk = ((DbRecord) ent).getPk();
    Class<?> entClass = ent.getClass();
    historyService.internalOnInsert(event.getEmgr(), whanots, entClass.getName(), entPk, ent);
  }

}
