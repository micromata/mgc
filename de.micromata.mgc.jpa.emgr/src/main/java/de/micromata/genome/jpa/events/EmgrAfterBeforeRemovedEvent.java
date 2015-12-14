package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Invoked before update.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterBeforeRemovedEvent extends EmgrInitForModEvent
{

  /**
   * Instantiates a new emgr after before removed event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrAfterBeforeRemovedEvent(IEmgr<?> emgr, DbRecord<?> record)
  {
    super(emgr, record);

  }

}
