package de.micromata.mgc.jpa.hibernatesearch;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;

import de.micromata.genome.jpa.IEmgr;

/**
 * Little wrapper to Hibernate Search Fulltext wrapper.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FullTextSearch
{
  public static FullTextEntityManager getFullTextEntityManager(IEmgr<?> emgr)
  {
    return getFullTextEntityManager(emgr.getEntityManager());
  }

  public static FullTextEntityManager getFullTextEntityManager(EntityManager entityManager)
  {
    FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
        .getFullTextEntityManager(entityManager);

    return fullTextEntityManager;
  }
}
