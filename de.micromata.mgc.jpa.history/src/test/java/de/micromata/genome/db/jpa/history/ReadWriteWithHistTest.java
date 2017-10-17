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

package de.micromata.genome.db.jpa.history;

import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.history.api.DiffEntry;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.test.DummyHistEntityDO;
import de.micromata.genome.db.jpa.history.test.HistoryTestEmgrFactory;

/**
 * Tests the the history feature.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ReadWriteWithHistTest extends GenomeTestCase
{
  private static final Logger LOG = Logger.getLogger(ReadWriteWithHistTest.class);

  static void printHistoryEntries(List<? extends HistoryEntry<?>> entries)
  {
    StringBuilder sb = new StringBuilder();
    String divider = ";";

    sb.append("id").append(divider).append("timestamp").append(divider).append("entity").append(divider).append("entid")
        .append(divider).append("user").append(divider)
        .append("property").append(divider).append("old").append(divider).append("new\n");
    for (HistoryEntry<?> he : entries) {
      for (DiffEntry de : he.getDiffEntries()) {
        sb.append(he.getPk()).append(divider)
            .append(he.getModifiedAt()).append(divider)
            .append(he.getEntityName()).append(divider)
            .append(he.getEntityId()).append(divider)
            .append(he.getEntityOpType().name()).append(divider)
            .append(he.getUserName()).append(divider)
            .append(de.getPropertyName()).append(divider)
            .append(de.getOldValue()).append(divider)
            .append(de.getNewValue()).append(divider)
            .append("\n");

      }
      System.out.println("HistoryEntries: \n" + sb);
    }
  }

  public void cleanupTestTable()
  {
    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.deleteFromQuery(DummyHistEntityDO.class, "select e from " + DummyHistEntityDO.class.getName() + " e");
      return null;
    });
    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      HistoryServiceManager.get().getHistoryService().clearHistoryForEntityClass(emgr, DummyHistEntityDO.class);
      return null;
    });
  }

  @Test
  public void testReadWrite()
  {
    cleanupTestTable();
    // just standard insert operation
    DummyHistEntityDO ne = new DummyHistEntityDO();
    ne.setStringValue("The Created");

    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.insert(ne);
      return null;
    });
    DummyHistEntityDO se = HistoryTestEmgrFactory.get().runWoTrans((emgr) -> {
      return emgr.selectByPkDetached(DummyHistEntityDO.class, ne.getPk());
    });
    // just standard update operations
    se.setStringValue("The Mod");
    se.setLongValue(42L);
    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      emgr.updateCopy(se);
      return null;
    });

    HistoryService histservice = HistoryServiceManager.get().getHistoryService();
    List<? extends HistoryEntry<?>> entries = HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      return histservice.getHistoryEntries(emgr, se);
    });
    // created no hist for second update.
    Assert.assertEquals(2, entries.size());
    printHistoryEntries(entries);
  }

  /**
   * 
   */
  @Test
  public void testReadTransactionalInsert()
  {
    cleanupTestTable();
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
    List<? extends HistoryEntry<?>> histent = HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      return HistoryServiceManager.get().getHistoryService()
          .getHistoryEntriesForEntityClass(emgr, DummyHistEntityDO.class);
    });

    Assert.assertEquals(1, histent.size());
    printHistoryEntries(histent);
  }

  @Test
  public void testReadTransactionalUpdate()
  {
    cleanupTestTable();
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
      LOG.info("Expected exception: " + ex.getMessage(), ex);
    }
    List<? extends HistoryEntry<?>> histent = HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      return HistoryServiceManager.get().getHistoryService()
          .getHistoryEntriesForEntityClass(emgr, DummyHistEntityDO.class);
    });

    Assert.assertEquals(2, histent.size());
    ReadWriteWithHistTest.printHistoryEntries(histent);
  }
}
