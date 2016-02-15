package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.DbRecordDO;
import de.micromata.genome.jpa.IEmgr;

/**
 * Event will invoked, if an entity was detached from entitymanager.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterDetachEvent extends EmgrForEntityObjectEvent
{

  /**
   * Instantiates a new emgr after detach event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrAfterDetachEvent(IEmgr<?> emgr, DbRecord<?>  entity)
  {
    super(emgr, entity);
  }

}
