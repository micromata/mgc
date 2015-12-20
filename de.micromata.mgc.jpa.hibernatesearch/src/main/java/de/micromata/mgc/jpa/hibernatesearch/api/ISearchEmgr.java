package de.micromata.mgc.jpa.hibernatesearch.api;

import java.util.List;

import org.hibernate.search.jpa.FullTextEntityManager;

import de.micromata.genome.jpa.IEmgr;

/**
 * Extension of Emgr, with Hibernate Search.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ISearchEmgr<EMGR extends ISearchEmgr<?>>extends IEmgr<EMGR>
{
  org.hibernate.search.query.dsl.QueryBuilder getFullTexteSearchQueryBuilder();

  <T> List<T> searchDetached(String expression, Class<T> type, String... fields);

  <T> List<T> searchAttached(String expression, Class<T> type, String... fields);

  /**
   * Invokes indexing on given entity.
   * 
   * Events: SearchEmgrReindexEventFilterEvent
   * 
   * @param entity
   */
  void reindex(Object entity);

  /**
   * Gets the full text entity manager.
   *
   * @return the full text entity manager
   */
  FullTextEntityManager getFullTextEntityManager();

}
