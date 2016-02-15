package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.DbRecordDO;
import de.micromata.genome.jpa.IEmgr;

/**
 * Base event for Object entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrForEntityObjectEvent extends EmgrEvent
{

  /**
   * The entity.
   */
  private Object entity;

  /**
   * Instantiates a new emgr for entity object event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrForEntityObjectEvent(IEmgr<?> emgr, DbRecord<?> entity)
  {
    super(emgr);
    this.entity = entity;
  }

  public Object getEntity()
  {
    return entity;
  }
}
