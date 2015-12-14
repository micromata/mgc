package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

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
  protected DbRecord<?> record;

  public DbRecord<?> getRecord()
  {
    return record;
  }

  public void setRecord(DbRecord<?> record)
  {
    this.record = record;
  }

  /**
   * Instantiates a new emgr db record event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrDbRecordEvent(IEmgr<?> emgr, DbRecord<?> record)
  {
    super(emgr);
    this.record = record;
  }

}
