package de.micromata.genome.jpa.events.impl;

import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrInitForInsertEvent;

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
