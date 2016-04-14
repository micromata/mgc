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

package de.micromata.genome.db.jpa.normsearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.micromata.genome.db.jpa.normsearch.testentities.DummyHistEntityDO;
import de.micromata.genome.db.jpa.normsearch.testentities.TestNSearchMgrFactory;
import de.micromata.genome.db.jpa.normsearch.testentities.TestTableMasterSearchDO;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * The Class TestNormSearch.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TestNormSearch extends MgcTestCase
{

  @Before
  public void cleanNormSearch()
  {
    TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      emgr.createUntypedQuery("delete from " + DummyHistEntityDO.class.getName()).executeUpdate();
      emgr.createUntypedQuery("delete from " + TestTableMasterSearchDO.class.getName()).executeUpdate();
      return null;
    });
  }

  @Test
  public void testDefault()
  {
    long nowtime = System.currentTimeMillis();

    // insert an entity
    TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      DummyHistEntityDO ne = new DummyHistEntityDO();
      ne.setComment("Mein Kommentar");
      ne.setLongValue(nowtime);
      ne.setStringValue("AnotherStringValue");
      emgr.insert(ne);
      return null;
    });

    // search entity
    NormalizedSearchService searchdao = NormalizedSearchServiceManager.get().getNormalizedSearchService();
    List<Long> res = TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      return searchdao.search(emgr, TestTableMasterSearchDO.class, "KOMM");
    });
    Assert.assertEquals(1, res.size());

    // another search entity

    res = TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      return searchdao.search(emgr, TestTableMasterSearchDO.class, "AnotherStringValue");
    });
    Assert.assertEquals(1, res.size());

    // Update entity
    TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      DummyHistEntityDO ent = emgr
          .createQuery(DummyHistEntityDO.class,
              "select e from " + DummyHistEntityDO.class.getName() + " e where e.longValue = :longValue",
              "longValue", nowtime)
          .getSingleResult();
      ent.setStringValue("ChangeStringValue");
      emgr.update(ent);
      return null;
    });

    // don't find original value
    res = TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      return searchdao.search(emgr, TestTableMasterSearchDO.class, "AnotherStringValue");
    });
    Assert.assertEquals(0, res.size());

    // find changed value
    res = TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      return searchdao.search(emgr, TestTableMasterSearchDO.class, "ChangeStringValue");
    });
    Assert.assertEquals(1, res.size());

    // delete the entity
    TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      emgr.deleteFromQuery(DummyHistEntityDO.class,
          "select e from " + DummyHistEntityDO.class.getName() + " e where e.longValue = :longValue",
          "longValue", nowtime);
      return null;
    });

    // don't find it.9
    res = TestNSearchMgrFactory.get().runInTrans((emgr) -> {
      return searchdao.search(emgr, TestTableMasterSearchDO.class, "KOMM");
    });
    Assert.assertEquals(0, res.size());
  }
}
