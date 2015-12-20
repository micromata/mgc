package de.micromata.mgc.jpa.hibernatesearch.events;

import de.micromata.genome.jpa.events.EmgrFilterEvent;
import de.micromata.mgc.jpa.hibernatesearch.api.ISearchEmgr;

/**
 * Filters an explicite reindex call to SearchEmgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SearchEmgrReindexEventFilterEvent extends EmgrFilterEvent<Void>
{

  /**
   * The entity.
   */
  private Object entity;

  /**
   * The search emgr.
   */
  private ISearchEmgr<?> searchEmgr;

  /**
   * Instantiates a new search emgr reindex event filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public SearchEmgrReindexEventFilterEvent(ISearchEmgr<?> emgr, Object entity)
  {
    super(emgr);
    this.entity = entity;
    this.searchEmgr = emgr;
  }

  /**
   * Gets the entity.
   *
   * @return the entity
   */
  public Object getEntity()
  {
    return entity;
  }

  /**
   * Sets the entity.
   *
   * @param entity the new entity
   */
  public void setEntity(Object entity)
  {
    this.entity = entity;
  }

  /**
   * Gets the search emgr.
   *
   * @return the search emgr
   */
  public ISearchEmgr<?> getSearchEmgr()
  {
    return searchEmgr;
  }

}
