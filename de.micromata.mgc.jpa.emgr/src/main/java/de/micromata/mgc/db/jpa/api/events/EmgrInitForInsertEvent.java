package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.DbRecord;
import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * An Entity will be prepared for insertion.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrInitForInsertEvent extends EmgrDbRecordEvent
{

  /**
   * Instantiates a new emgr init for insert event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrInitForInsertEvent(IEmgr<?> emgr, DbRecord record)
  {
    super(emgr, record);
  }
}
