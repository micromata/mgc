package de.micromata.genome.db.jpa.history;

import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.dao.GenomeDaoManager;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.test.DummyHistEntityDO;
import de.micromata.genome.db.jpa.history.test.HistoryTestEmgrFactory;
import de.micromata.genome.user.InternalSysAdminUser;

/**
 * Tests the the history feature.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ReadWriteWithHistTest extends GenomeTestCase
{
  @Before
  public void cleanupTestTable()
  {
    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.deleteFromQuery(DummyHistEntityDO.class, "select e from " + DummyHistEntityDO.class.getName() + " e");
      return null;
    });
    HistoryServiceManager.get().getHistoryService().clearHistoryForEntityClass(DummyHistEntityDO.class);
  }

  @Test
  public void testReadWrite()
  {
    GenomeDaoManager.get().getUserDAO().setCurrentUser(new InternalSysAdminUser("tst_hist_create"));
    DummyHistEntityDO ne = new DummyHistEntityDO();
    ne.setStringValue("The Created");

    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.insert(ne);
      return null;
    });
    DummyHistEntityDO se = HistoryTestEmgrFactory.get().runWoTrans((emgr) -> {
      return emgr.selectByPkDetached(DummyHistEntityDO.class, ne.getPk());
    });
    se.setStringValue("The Mod");
    se.setLongValue(42L);
    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.updateCopy(se);
      return null;
    });

    HistoryService histservice = HistoryServiceManager.get().getHistoryService();
    List<? extends HistoryEntry> entries = histservice.getHistoryEntries(se);
    // created no hist for second update.
    Assert.assertEquals(2, entries.size());
  }

  /**
   * 
   */
  @Test
  public void testReadTransactionalInsert()
  {
    GenomeDaoManager.get().getUserDAO().setCurrentUser(new InternalSysAdminUser("tst_hist_create"));
    DummyHistEntityDO ne = new DummyHistEntityDO();
    ne.setStringValue("The Created");
    ne.setLongValue(111);

    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.insert(ne);
      return null;
    });
    DummyHistEntityDO se = new DummyHistEntityDO();
    se.setStringValue("The Created");
    se.setLongValue(111);
    try {
      HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
        emgr.insert(se);
        return null;
      });
      Assert.fail("Expected ex");
    } catch (PersistenceException ex) {
      // expected ex.printStackTrace();
    }
    List<? extends HistoryEntry> histent = HistoryServiceManager.get().getHistoryService()
        .getHistoryEntriesForEntityClass(DummyHistEntityDO.class);
    Assert.assertEquals(1, histent.size());
  }

  @Test
  public void testReadTransactionalUpdate()
  {
    GenomeDaoManager.get().getUserDAO().setCurrentUser(new InternalSysAdminUser("tst_hist_create"));
    DummyHistEntityDO ne = new DummyHistEntityDO();
    ne.setStringValue("The Created");
    ne.setLongValue(111);

    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.insert(ne);
      return null;
    });
    DummyHistEntityDO se = new DummyHistEntityDO();
    se.setStringValue("The Created");
    se.setLongValue(112);

    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.insert(se);
      return null;
    });
    se.setLongValue(111);
    try {
      HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
        emgr.updateCopy(se);
        return null;
      });
      Assert.fail("Expected ex");
    } catch (PersistenceException ex) {
      ex.printStackTrace();
    }
    List<? extends HistoryEntry> histent = HistoryServiceManager.get().getHistoryService()
        .getHistoryEntriesForEntityClass(DummyHistEntityDO.class);
    Assert.assertEquals(2, histent.size());
  }
}
