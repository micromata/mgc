package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.DbRecordDO;
import de.micromata.genome.jpa.IEmgr;

/**
 * Event after removed (deleted) one entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterDeletedEvent extends EmgrForEntityObjectEvent
{

  /**
   * Instantiates a new emgr after removed event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrAfterDeletedEvent(IEmgr<?> emgr, DbRecord<?> entity)
  {
    super(emgr, entity);
  }

}
