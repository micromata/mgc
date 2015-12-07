package de.micromata.mgc.db.jpa.api.eventhandler;

import java.util.Date;

import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.mgc.db.jpa.CriteriaUpdate;
import de.micromata.mgc.db.jpa.api.StdRecord;
import de.micromata.mgc.db.jpa.api.events.EmgrCriteriaUpdateEvent;
import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;

/**
 * Check CriteriaUpdates for StdRecord and set modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class UpdateStdRecordCriteriaUpdateEventHandler implements EmgrEventHandler<EmgrCriteriaUpdateEvent>
{

  @Override
  public void onEvent(EmgrCriteriaUpdateEvent event)
  {
    CriteriaUpdate<?> update = event.getCriteriaUpdate();
    Class<?> clazz = update.getEntityClass();
    if (StdRecord.class.isAssignableFrom(clazz) == false) {
      return;
    }
    update.setExpression("updateCounter", update.getMasterAlias() + ".updateCounter + 1");
    update.set("modifiedAt", new Date());
    update.set("modifiedBy", LoggingServiceManager.get().getLoggingContextService().getCurrentUserName());
  }

}
