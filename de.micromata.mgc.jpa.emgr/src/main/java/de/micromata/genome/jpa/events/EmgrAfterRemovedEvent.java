package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.IEmgr;

/**
 * Event after removed (deleted) one entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterRemovedEvent extends EmgrForEntityObjectEvent
{

  /**
   * Instantiates a new emgr after removed event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrAfterRemovedEvent(IEmgr<?> emgr, Object entity)
  {
    super(emgr, entity);
  }

}
