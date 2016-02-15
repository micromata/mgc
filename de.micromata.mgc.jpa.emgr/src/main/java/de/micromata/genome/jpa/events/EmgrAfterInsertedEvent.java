package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Event after inserted one entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterInsertedEvent extends EmgrForEntityObjectEvent
{

  /**
   * Instantiates a new emgr after inserted event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrAfterInsertedEvent(IEmgr<?> emgr,  DbRecord<?>  entity)
  {
    super(emgr, entity);
  }

}
