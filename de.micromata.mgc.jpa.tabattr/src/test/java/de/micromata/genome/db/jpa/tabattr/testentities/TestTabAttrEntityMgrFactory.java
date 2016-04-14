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

package de.micromata.genome.db.jpa.tabattr.testentities;

import javax.persistence.EntityManager;

import org.junit.Ignore;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * emgr factory for tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class TestTabAttrEntityMgrFactory extends EmgrFactory<TestTabAttrEntityMgr>
{

  static TestTabAttrEntityMgrFactory INSTANCE;

  public static synchronized TestTabAttrEntityMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new TestTabAttrEntityMgrFactory();
    return INSTANCE;
  }

  public TestTabAttrEntityMgrFactory()
  {
    super("de.micromata.genome.jpa.tabattr.testentities");
  }

  @Override
  protected TestTabAttrEntityMgr createEmgr(EntityManager entitManager, EmgrTx<TestTabAttrEntityMgr> emgrTx)
  {
    return new TestTabAttrEntityMgr(entitManager, this, emgrTx);
  }

}
