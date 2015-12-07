package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.IEmgr;

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
  public EmgrForEntityObjectEvent(IEmgr<?> emgr, Object entity)
  {
    super(emgr);
    this.entity = entity;
  }

  public Object getEntity()
  {
    return entity;
  }
}
