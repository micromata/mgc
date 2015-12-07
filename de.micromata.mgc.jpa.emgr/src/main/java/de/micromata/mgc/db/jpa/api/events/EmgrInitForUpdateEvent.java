package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.DbRecord;
import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Event will be called to initialize DbRecord for update.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrInitForUpdateEvent extends EmgrDbRecordEvent
{

  /**
   * Instantiates a new emgr init for update event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrInitForUpdateEvent(IEmgr<?> emgr, DbRecord record)
  {
    super(emgr, record);
  }

}
