package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.IEmgr;

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
  public EmgrBeforeUpdatedEvent(IEmgr<?> emgr, Object entity)
  {
    super(emgr, entity);
  }

}
