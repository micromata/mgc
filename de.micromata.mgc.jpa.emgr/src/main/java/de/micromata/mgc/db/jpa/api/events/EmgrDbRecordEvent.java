package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.DbRecord;
import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Base Event type with an DbRecord as argument.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrDbRecordEvent extends EmgrEvent
{

  /**
   * The record.
   */
  protected DbRecord record;

  public DbRecord getRecord()
  {
    return record;
  }

  public void setRecord(DbRecord record)
  {
    this.record = record;
  }

  /**
   * Instantiates a new emgr db record event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrDbRecordEvent(IEmgr<?> emgr, DbRecord record)
  {
    super(emgr);
    this.record = record;
  }

}
