package de.micromata.genome.db.jpa.normsearch.eventlistener;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchDaoManager;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.Emgr;
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
    if ((event.getEmgr() instanceof Emgr) == false) {
      return;
    }
    if ((event.getEntity() instanceof DbRecord) == false) {
      return;
    }
    NormalizedSearchDaoManager.get().getNormalizedSearchDAO().insert((Emgr) event.getEmgr(),
        (DbRecord) event.getEntity());

  }

}
