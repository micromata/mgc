package de.micromata.genome.jpa.events.impl;

import java.util.Date;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrBeforeCriteriaUpdateEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Standard listener to handle modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBeforeCriteriaUpdateEventHandler implements EmgrEventHandler<EmgrBeforeCriteriaUpdateEvent>
{

  @Override
  public void onEvent(EmgrBeforeCriteriaUpdateEvent event)
  {
    CriteriaUpdate<?> update = event.getCriteriaUpdate();
    IEmgr<?> emgr = event.getEmgr();
    Class<?> entclass = update.getEntityClass();
    if (StdRecord.class.isAssignableFrom(entclass) == false) {
      return;
    }
    String userid = emgr.getEmgrFactory().getCurrentUserId();
    Date now = emgr.getEmgrFactory().getNow();

    update.setIfAbsend("modifiedBy", userid)
        .setIfAbsend("modifiedAt", now);
  }

}
