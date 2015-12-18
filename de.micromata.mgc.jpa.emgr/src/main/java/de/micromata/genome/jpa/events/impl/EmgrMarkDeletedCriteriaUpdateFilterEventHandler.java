package de.micromata.genome.jpa.events.impl;

import java.util.Date;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.MarkDeletableRecord;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrMarkDeletedCriteriaUpdateFilterEvent;

/**
 * If an StdRecord was marked deleted, update modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrMarkDeletedCriteriaUpdateFilterEventHandler
    implements EmgrEventHandler<EmgrMarkDeletedCriteriaUpdateFilterEvent<?>>
{

  /**
   * Sets the modified.
   *
   * @param emgr the emgr
   * @param update the update
   * @param rec the rec
   */
  public static void setModified(IEmgr<?> emgr, CriteriaUpdate<?> update, StdRecord<?> rec)
  {
    String userid = emgr.getEmgrFactory().getCurrentUserId();
    Date now = emgr.getEmgrFactory().getNow();
    rec.setModifiedBy(userid);
    rec.setModifiedAt(now);
    update.set("modifiedBy", userid)
        .set("modifiedAt", now);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void onEvent(EmgrMarkDeletedCriteriaUpdateFilterEvent<?> event)
  {
    MarkDeletableRecord<?> ent = event.getEntity();
    if (ent instanceof StdRecord) {
      setModified(event.getEmgr(), event.getUpdate(), (StdRecord) ent);
    }
    event.nextFilter();
  }

}
