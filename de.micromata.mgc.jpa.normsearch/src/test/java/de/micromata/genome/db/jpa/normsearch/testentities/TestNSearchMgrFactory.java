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

package de.micromata.genome.db.jpa.normsearch.testentities;

import javax.persistence.EntityManager;

import org.junit.Ignore;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchServiceManager;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * emgr factory for tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class TestNSearchMgrFactory extends EmgrFactory<DefaultEmgr>
{

  static TestNSearchMgrFactory INSTANCE;

  public static synchronized TestNSearchMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new TestNSearchMgrFactory();
    return INSTANCE;
  }

  public TestNSearchMgrFactory()
  {
    super("de.micromata.genome.db.jpa.normsearch.testentities");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entitManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entitManager, this, emgrTx);
  }

  @Override
  protected void registerEvents()
  {
    super.registerEvents();
    NormalizedSearchServiceManager.get().getNormalizedSearchService().registerEmgrListener(this);
  }

}
