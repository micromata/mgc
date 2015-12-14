package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * An event which introduce a modification, like insert/update/delete.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrInitForModEvent extends EmgrDbRecordEvent
{

  /**
   * Instantiates a new emgr init for mod event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrInitForModEvent(IEmgr<?> emgr, DbRecord<?> record)
  {
    super(emgr, record);
  }
}
