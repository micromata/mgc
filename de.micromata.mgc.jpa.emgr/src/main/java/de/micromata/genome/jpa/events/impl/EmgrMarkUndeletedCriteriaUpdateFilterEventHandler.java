package de.micromata.genome.jpa.events.impl;

import de.micromata.genome.jpa.MarkDeletableRecord;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrMarkDeletedCriteriaUpdateFilterEvent;

/**
 * If an StdRecord was unmarked, update modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrMarkUndeletedCriteriaUpdateFilterEventHandler
    implements EmgrEventHandler<EmgrMarkDeletedCriteriaUpdateFilterEvent<?>>
{

  @Override
  public void onEvent(EmgrMarkDeletedCriteriaUpdateFilterEvent<?> event)
  {
    MarkDeletableRecord<?> ent = event.getEntity();
    if (ent instanceof StdRecord) {
      EmgrMarkDeletedCriteriaUpdateFilterEventHandler.setModified(event.getEmgr(), event.getUpdate(), (StdRecord) ent);
    }
    event.nextFilter();
  }

}
