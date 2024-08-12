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

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.Clauses;
import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * What happens, if entity was updated via criteria update.
 * 
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrSearchCriteriaUpdateTest extends MgcTestCase
{
  private static final Logger LOG = Logger.getLogger(EmgrSearchCriteriaUpdateTest.class);

  @Test
  public void testSearch()
  {
    HibernateSearchTestEmgrFactory emf = HibernateSearchTestEmgrFactory.get();
    MyEntityDO net = new MyEntityDO();
    net.setName("XThisIsAName");
    net.setLoginName("Blub");
    net.setNotSearchable("NOTFOUND");
    emf.runInTrans((emgr) -> emgr.insert(net));

    List<MyEntityDO> found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("XThisIsAName", MyEntityDO.class, "name");
      return lret;
    });

    Assert.assertEquals(1, found.size());
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("YAnotherName", MyEntityDO.class, "name");
      return lret;
    });
    Assert.assertEquals(0, found.size());
    emf.runInTrans((emgr) -> {
      emgr.update(CriteriaUpdate.createUpdate(MyEntityDO.class)
          .set("name", "YAnotherName")
          .addWhere(Clauses.equal("name", "XThisIsAName")));
      return null;
    });

    // criteria update does not update index.
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("XThisIsAName", MyEntityDO.class, "name");
      return lret;
    });
    Assert.assertEquals(1, found.size());
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("YAnotherName", MyEntityDO.class, "name");
      return lret;
    });
    Assert.assertEquals(0, found.size());

    // reindex the entity
    emf.runInTrans((emgr) -> {
      emgr.reindex(emgr.selectByPkAttached(MyEntityDO.class, net.getPk()));
      return null;
    });
    // now find the entities.
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("XThisIsAName", MyEntityDO.class, "name");
      return lret;
    });

    Assert.assertEquals(0, found.size());
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("YAnotherName", MyEntityDO.class, "name");
      return lret;
    });
    Assert.assertEquals(1, found.size());

    // try with new JPA CriteriaUpdate
    int aff = emf.runInTrans((emgr) -> {
      EntityManager em = emgr.getEntityManager();
      CriteriaBuilder cb = em.getCriteriaBuilder();
      jakarta.persistence.criteria.CriteriaUpdate<MyEntityDO> cu = cb.createCriteriaUpdate(MyEntityDO.class);
      Root<MyEntityDO> root = cu.from(MyEntityDO.class);
      cu.set("name", "ThirdName").where(cb.equal(root.get("name"), "YAnotherName"));
      int affected = em.createQuery(cu).executeUpdate();
      return affected;

    });
    Assert.assertEquals(1, aff);
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("ThirdName", MyEntityDO.class, "name");
      return lret;
    });
    // no difference to JPA2.1 criteriaupdate, this also doesn't update ftindex.
    Assert.assertEquals(0, found.size());
    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("YAnotherName", MyEntityDO.class, "name");
      return lret;
    });
    Assert.assertEquals(1, found.size());
  }
}
