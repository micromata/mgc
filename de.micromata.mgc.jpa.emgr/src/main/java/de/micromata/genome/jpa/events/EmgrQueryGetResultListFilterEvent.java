package de.micromata.genome.jpa.events;

import java.util.List;

import javax.persistence.Query;

import de.micromata.genome.jpa.IEmgr;

/**
 * Executing getResultList on Query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic type
 */
public class EmgrQueryGetResultListFilterEvent extends EmgrFilterEvent<List<?>>
{

  /**
   * The query.
   */
  private Query query;

  /**
   * Instantiates a new emgr type query get result list filter event.
   *
   * @param emgr the emgr
   * @param query the query
   */
  public EmgrQueryGetResultListFilterEvent(IEmgr<?> emgr, Query query)
  {
    super(emgr);
    this.query = query;
  }

  public Query getQuery()
  {
    return query;
  }
}
