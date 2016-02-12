package de.micromata.genome.db.jpa.normsearch.eventlistener;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchServiceManager;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.events.EmgrAfterInsertedEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Updates history after Update.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NormSearchAfterInsertEventListener implements EmgrEventHandler<EmgrAfterInsertedEvent>
{

  @Override
  public void onEvent(EmgrAfterInsertedEvent event)
  {
    if ((event.getEntity() instanceof DbRecord) == false) {
      return;
    }
    NormalizedSearchServiceManager.get().getNormalizedSearchService().onInsert(event.getEmgr(),
        (DbRecord<?>) event.getEntity());

  }

}
