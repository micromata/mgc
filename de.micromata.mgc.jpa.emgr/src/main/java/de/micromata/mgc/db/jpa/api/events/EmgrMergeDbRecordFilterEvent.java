package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.DbRecord;
import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Filters de.micromata.genome.jpa.Emgr.merge(DbRecord).
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public class EmgrMergeDbRecordFilterEvent<E extends DbRecord>extends EmgrFilterEvent<E>
{

  /**
   * The entity.
   */
  private E entity;

  public E getEntity()
  {
    return entity;
  }

  /**
   * Instantiates a new emgr merge db record filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrMergeDbRecordFilterEvent(IEmgr<?> emgr, E entity)
  {
    super(emgr);
    this.entity = entity;
  }

}
