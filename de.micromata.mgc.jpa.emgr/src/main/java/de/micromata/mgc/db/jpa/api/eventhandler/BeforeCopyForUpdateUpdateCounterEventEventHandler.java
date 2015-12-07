package de.micromata.mgc.db.jpa.api.eventhandler;

import de.micromata.mgc.db.jpa.api.StdRecord;
import de.micromata.mgc.db.jpa.api.events.EmgrBeforeCopyForUpdateEvent;
import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;

/**
 * Handler check if fourced update and a StdRecord. In this case copies the updateCounter from entityamanger instance.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class BeforeCopyForUpdateUpdateCounterEventEventHandler implements EmgrEventHandler<EmgrBeforeCopyForUpdateEvent>
{

  @Override
  public void onEvent(EmgrBeforeCopyForUpdateEvent event)
  {
    if (event.isForceUpdate() == false || (event.getSource() instanceof StdRecord) == false
        || (event.getTarget() instanceof StdRecord) == false) {
      return;
    }
    ((StdRecord) event.getSource()).setUpdateCounter(((StdRecord) event.getTarget()).getUpdateCounter());
  }

}
