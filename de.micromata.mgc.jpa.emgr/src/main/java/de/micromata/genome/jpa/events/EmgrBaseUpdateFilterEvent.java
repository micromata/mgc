package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;

/**
 * Base event class for criterai updates.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <E> the entity.
 */
public class EmgrBaseUpdateFilterEvent<E>extends EmgrFilterEvent<Integer>
{
  /**
   * The update.
   */
  protected CriteriaUpdate<E> update;

  /**
   * Instantiates a new emgr update criteria update filter event.
   *
   * @param emgr the emgr
   * @param update the update
   */
  public EmgrBaseUpdateFilterEvent(IEmgr<?> emgr, CriteriaUpdate<E> update)
  {
    super(emgr);
    this.update = update;
  }

  public CriteriaUpdate<E> getUpdate()
  {
    return update;
  }
}
