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

package de.micromata.genome.jpa.test;

import org.junit.Test;

import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * Basic master detail tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class MasterDetailTest extends MgcTestCase
{
  @Test
  public void testMasterDetail()
  {
    final JpaTestEntMgrFactory mgrfac = JpaTestEntMgrFactory.get();
    final GenomeJpaMasterTableDO m = new GenomeJpaMasterTableDO();
    m.setFirstName("Roger");
    mgrfac.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr mgr)
      {
        mgr.insertAttached(m);
        mgr.remove(m);
        return null;
      }
    });

  }

  @Test
  public void testMasterDetail1()
  {
    final JpaTestEntMgrFactory mgrfac = JpaTestEntMgrFactory.get();
    final GenomeJpaMasterTableDO m = new GenomeJpaMasterTableDO();
    m.createAddNewDetail().setLocation("Kassel");
    m.setFirstName("Roger");
    mgrfac.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr mgr)
      {
        mgr.insertAttached(m);
        m.createAddNewDetail().setLocation("Zurich");
        mgr.update(m);
        mgr.remove(m);
        return null;
      }
    });

  }
}
