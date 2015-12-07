package de.micromata.mgc.jpa.hibernatesearch;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * Basic functional test of full text searches.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateSearchTest extends MgcTestCase
{
  @Test
  public void testFullSearch()
  {

    HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      Session sess = emgr.getEntityManager().unwrap(Session.class);
      MyEntityDO ent = new MyEntityDO();
      ent.setName("Roger Kommer");
      emgr.insert(ent);
      return ent;
    });
    List found = HibernateSearchTestEmgrFactory.get().runWoTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .keyword()
          .onFields("name")
          .matching("KOMMER")
          .createQuery();
      javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List result = jpaQuery.getResultList();
      return result;
    });
    Assert.assertEquals(1, found.size());
  }
}
