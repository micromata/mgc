package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.DbRecord;
import de.micromata.mgc.db.jpa.api.IEmgr;

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
  private DbRecord entity;

  public DbRecord getEntity()
  {
    return entity;
  }

  /**
   * Instantiates a new emgr remove db record filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrRemoveDbRecordFilterEvent(IEmgr<?> emgr, DbRecord entity)
  {
    super(emgr);
    this.entity = entity;
  }

}
