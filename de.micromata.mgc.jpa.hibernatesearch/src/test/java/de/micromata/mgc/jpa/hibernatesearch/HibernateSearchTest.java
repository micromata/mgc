package de.micromata.mgc.jpa.hibernatesearch;

import java.util.List;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * Basic functional test of full text searches.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateSearchTest extends HibernateSearchTestBase
{
  @SuppressWarnings("unchecked")
  @Test
  public void testFullSearch()
  {

    MyEntityDO ent = HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      MyEntityDO ment = new MyEntityDO();
      ment.setName("Roger Kommer");
      emgr.insert(ment);
      return ment;
    });
    List<MyEntityDO> found = HibernateSearchTestEmgrFactory.get().runWoTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .keyword()
          .onFields("name")
          .matching("KOMMER")
          .createQuery();
      javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List<MyEntityDO> result = jpaQuery.getResultList();
      return result;
    });
    Assert.assertEquals(1, found.size());

    found = HibernateSearchTestEmgrFactory.get().runWoTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .all()
          .createQuery();
      javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List<MyEntityDO> result = jpaQuery.getResultList();
      return result;
    });
    Assert.assertEquals(1, found.size());

    HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      //TODO: RK Add boolean to make it compilable
      emgr.deleteDetached(ent, false);
      return null;
    });

    found = HibernateSearchTestEmgrFactory.get().runWoTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .all()
          .createQuery();
      javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List<MyEntityDO> result = jpaQuery.getResultList();
      return result;
    });

    Assert.assertEquals(0, found.size());
  }
}
