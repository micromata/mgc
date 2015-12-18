package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.MarkDeletableRecord;

/**
 * Event, in case an entity should be marked as deleted.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <E>
 */
public class EmgrMarkDeletedCriteriaUpdateFilterEvent<T extends MarkDeletableRecord<?>>
    extends EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent<T>
{
  /**
   * Instantiates a new emgr update criteria update filter event.
   *
   * @param emgr the emgr
   * @param update the update
   */
  public EmgrMarkDeletedCriteriaUpdateFilterEvent(IEmgr<?> emgr, T entity, CriteriaUpdate<T> update)
  {
    super(emgr, entity, update);
  }
}
