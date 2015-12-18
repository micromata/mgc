package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Invoked before update.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBeforeDeleteEvent extends EmgrInitForModEvent
{

  /**
   * Instantiates a new emgr after before removed event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrBeforeDeleteEvent(IEmgr<?> emgr, DbRecord<?> record)
  {
    super(emgr, record);
  }

}
