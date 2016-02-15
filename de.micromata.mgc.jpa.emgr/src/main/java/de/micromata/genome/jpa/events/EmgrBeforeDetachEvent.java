package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.DbRecordDO;
import de.micromata.genome.jpa.IEmgr;

/**
 * Event will be invoked before entity will be detached.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBeforeDetachEvent extends EmgrForEntityObjectEvent
{

  /**
   * Instantiates a new emgr before detach event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrBeforeDetachEvent(IEmgr<?> emgr, DbRecord<?>  entity)
  {
    super(emgr, entity);
  }

}
