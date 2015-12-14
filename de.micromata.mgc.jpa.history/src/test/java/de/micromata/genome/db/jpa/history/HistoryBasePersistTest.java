package de.micromata.genome.db.jpa.history;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.api.JpaHistoryEntityManagerFactory;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.db.jpa.history.test.DummyHistEntityDO;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HistoryBasePersistTest extends GenomeTestCase
{
  @Test
  public void testPersist()
  {
    DummyHistEntityDO dummy1 = new DummyHistEntityDO();
    dummy1.setPk(1L);
    dummy1.setStringValue("A Value");
    DummyHistEntityDO dummy2 = new DummyHistEntityDO();
    dummy2.setPk(1L);
    dummy2.setStringValue("B Value");
    WithHistory anot = DummyHistEntityDO.class.getAnnotation(WithHistory.class);
    List<WithHistory> anotl = new ArrayList<>();
    anotl.add(anot);
    HistoryService histservice = HistoryServiceManager.get().getHistoryService();
    JpaHistoryEntityManagerFactory.get().runInTrans((emgr) -> {
      histservice.internalOnInsert(emgr, anotl, DummyHistEntityDO.class.getName(), 1L, dummy1);

      List<? extends HistoryEntry> entries = histservice.getHistoryEntries(emgr, dummy1);
      return entries;
    });

  }
}
