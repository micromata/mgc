package de.micromata.mgc.db.jpa.api.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrInitForInsertEvent;

/**
 * Initializes std created/modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class InitCreatedStdRecordFieldsEventHandler implements EmgrEventHandler<EmgrInitForInsertEvent>
{

  @Override
  public void onEvent(EmgrInitForInsertEvent event)
  {
    InitUpdateStdRecordFieldsEventHandler.updateRecordRecursive(event.getEmgr(), event.getRecord(), true);

  }

}
