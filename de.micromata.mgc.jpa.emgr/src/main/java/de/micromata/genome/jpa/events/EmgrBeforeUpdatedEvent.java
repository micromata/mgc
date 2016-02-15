package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.DbRecordDO;
import de.micromata.genome.jpa.IEmgr;

/**
 * Event after updated one entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBeforeUpdatedEvent extends EmgrForEntityObjectEvent
{

  /**
   * Instantiates a new emgr before updated event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrBeforeUpdatedEvent(IEmgr<?> emgr, DbRecord<?>  entity)
  {
    super(emgr, entity);
  }

}
