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

package de.micromata.genome.db.jpa.tabattr;

import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.tabattr.testentities.TJpaMasterAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TJpaMasterDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgrFactory;

public class JpaCoreTest extends GenomeTestCase
{
  TestTabAttrEntityMgrFactory emgfac = TestTabAttrEntityMgrFactory.get();

  @Test
  public void testCore()
  {
    try {
      String key = "key";
      TJpaMasterDO nm = emgfac.runInTrans((emgr) -> {
        TJpaMasterDO master = new TJpaMasterDO();
        master.putAttribute(key, "Hello");
        emgr.getEntityManager().persist(master);
        return master;
      });

      //    nm.putAttribute(key, "newVal");
      //    emgfac.runInTrans((emgr) -> {
      //      emgr.getEntityManager().merge(nm);
      //      return null;
      //    });

      nm.putNewAttribute(key, "asdfasdfsd");
      emgfac.runInTrans((emgr) -> {
        emgr.getEntityManager().merge(nm);
        return null;
      });
      TJpaMasterAttrDO attr = nm.getAttributes().get(key);
      attr.getPk();
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
