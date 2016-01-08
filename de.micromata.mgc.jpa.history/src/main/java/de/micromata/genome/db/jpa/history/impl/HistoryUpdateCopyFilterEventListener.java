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
 * with a component using the component's <code>addHistoryUpdateCopyFilterEventListener<code> method. When the
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
