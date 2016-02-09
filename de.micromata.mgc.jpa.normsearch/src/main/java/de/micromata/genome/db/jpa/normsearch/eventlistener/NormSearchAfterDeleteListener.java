package de.micromata.genome.db.jpa.normsearch.eventlistener;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchServiceManager;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.Emgr;
import de.micromata.genome.jpa.events.EmgrAfterDeletedEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Updates history after Delete.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NormSearchAfterDeleteListener implements EmgrEventHandler<EmgrAfterDeletedEvent>
{

  @Override
  public void onEvent(EmgrAfterDeletedEvent event)
  {
    if ((event.getEmgr() instanceof Emgr) == false) {
      return;
    }
    if ((event.getEntity() instanceof DbRecord) == false) {
      return;
    }
    NormalizedSearchServiceManager.get().getNormalizedSearchDAO().delete((Emgr) event.getEmgr(),
        (DbRecord) event.getEntity());

  }

}
