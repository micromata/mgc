package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.MarkDeletableRecord;

/**
 * Event, in case an entity should be marked or unmarked as deleted.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <E>
 */
public class EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent<T extends MarkDeletableRecord<?>>
    extends EmgrBaseUpdateFilterEvent<T>
{
  protected T entity;

  /**
   * Instantiates a new emgr update criteria update filter event.
   *
   * @param emgr the emgr
   * @param update the update
   */
  public EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent(IEmgr<?> emgr, T entity, CriteriaUpdate<T> update)
  {
    super(emgr, update);
    this.entity = entity;
  }

  public T getEntity()
  {
    return entity;
  }

  public void setEntity(T entity)
  {
    this.entity = entity;
  }

}
