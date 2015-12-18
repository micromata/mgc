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

  <T> List<T> searchAttached(String expression, Class<T> type, String... fields);

  FullTextEntityManager getFullTextEntityManager();

}
