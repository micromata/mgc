package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Filters de.micromata.genome.jpa.Emgr.remove(DbRecord).
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrRemoveDbRecordFilterEvent extends EmgrFilterEvent<Void>
{

  /**
   * The entity.
   */
  private Object entity;

  public Object getEntity()
  {
    return entity;
  }

  /**
   * Instantiates a new emgr remove db record filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrRemoveDbRecordFilterEvent(IEmgr<?> emgr, Object entity)
  {
    super(emgr);
    this.entity = entity;
  }

}
