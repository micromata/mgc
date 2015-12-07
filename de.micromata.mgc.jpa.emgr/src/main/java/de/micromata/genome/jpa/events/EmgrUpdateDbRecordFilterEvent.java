package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Filters de.micromata.genome.jpa.Emgr.update(DbRecord).
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrUpdateDbRecordFilterEvent extends EmgrFilterEvent<Void>
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
   * Instantiates a new emgr update db record filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrUpdateDbRecordFilterEvent(IEmgr<?> emgr, DbRecord entity)
  {
    super(emgr);
    this.entity = entity;
  }

}
