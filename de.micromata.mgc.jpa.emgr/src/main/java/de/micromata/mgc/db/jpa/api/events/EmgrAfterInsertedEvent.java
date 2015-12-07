package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.IEmgr;

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
  public EmgrAfterInsertedEvent(IEmgr<?> emgr, Object entity)
  {
    super(emgr, entity);
  }

}
