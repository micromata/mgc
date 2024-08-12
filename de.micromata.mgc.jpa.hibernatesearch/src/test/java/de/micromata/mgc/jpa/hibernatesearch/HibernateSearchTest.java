//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
    List<MyEntityDO> found = HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .keyword()
          .onFields("name")
          .matching("KOMMER")
          .createQuery();
      jakarta.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List<MyEntityDO> result = jpaQuery.getResultList();
      return result;
    });
    Assert.assertEquals(1, found.size());

    found = HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .all()
          .createQuery();
      jakarta.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List<MyEntityDO> result = jpaQuery.getResultList();
      return result;
    });
    Assert.assertEquals(1, found.size());

    HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.deleteDetached(ent, true);
      return null;
    });

    found = HibernateSearchTestEmgrFactory.get().runInTrans((emgr) -> {
      FullTextEntityManager fullTextEntityManager = FullTextSearch.getFullTextEntityManager(emgr);
      org.hibernate.search.query.dsl.QueryBuilder qb = fullTextEntityManager.getSearchFactory()
          .buildQueryBuilder().forEntity(MyEntityDO.class).get();
      org.apache.lucene.search.Query luceneQuery = qb
          .all()
          .createQuery();
      jakarta.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, MyEntityDO.class);
      List<MyEntityDO> result = jpaQuery.getResultList();
      return result;
    });

    Assert.assertEquals(0, found.size());
  }
}
