package de.micromata.genome.jpa.events.impl;

import java.util.Date;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrCriteriaUpdateEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.logging.LoggingServiceManager;

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
