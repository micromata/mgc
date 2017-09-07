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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.db.jpa.history.test.DummyHistEntityDO;
import de.micromata.genome.db.jpa.history.test.HistoryTestEmgrFactory;

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
    HistoryTestEmgrFactory.get().runInTrans((emgr) -> {
      histservice.internalOnInsert(emgr, anotl, DummyHistEntityDO.class.getName(), 1L, dummy1);

      List<? extends HistoryEntry<?>> entries = histservice.getHistoryEntries(emgr, dummy1);
      ReadWriteWithHistTest.printHistoryEntries(entries);
      return entries;
    });

  }
}
