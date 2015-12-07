package de.micromata.mgc.db.jpa.api.eventhandler;

import de.micromata.mgc.db.jpa.api.StdRecord;
import de.micromata.mgc.db.jpa.api.events.EmgrAfterCopyForUpdateEvent;
import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;

/**
 * Manage update Counter after update
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterCopyForUpdateEventEventHandler implements EmgrEventHandler<EmgrAfterCopyForUpdateEvent>
{

  @Override
  public void onEvent(EmgrAfterCopyForUpdateEvent event)
  {
    if ((event.getTarget() instanceof StdRecord) == false || (event.getSource() instanceof StdRecord) == false) {
      return;
    }
    ((StdRecord) event.getSource()).setUpdateCounter(((StdRecord) event.getTarget()).getUpdateCounter());
  }

}
