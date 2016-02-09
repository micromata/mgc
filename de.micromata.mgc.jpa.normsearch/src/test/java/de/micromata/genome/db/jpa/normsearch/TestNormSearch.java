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
