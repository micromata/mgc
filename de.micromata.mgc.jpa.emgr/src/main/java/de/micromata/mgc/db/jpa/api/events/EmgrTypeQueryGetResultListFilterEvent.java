package de.micromata.mgc.db.jpa.api.events;

import java.util.List;

import javax.persistence.TypedQuery;

import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Executing getResultList on typed Query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic type
 */
public class EmgrTypeQueryGetResultListFilterEvent<R>extends EmgrFilterEvent<List<R>>
{

  /**
   * The query.
   */
  private TypedQuery<R> query;

  public TypedQuery<R> getQuery()
  {
    return query;
  }

  /**
   * Instantiates a new emgr type query get result list filter event.
   *
   * @param emgr the emgr
   * @param query the query
   */
  public EmgrTypeQueryGetResultListFilterEvent(IEmgr<?> emgr, TypedQuery<R> query)
  {
    super(emgr);
    this.query = query;
  }

}
