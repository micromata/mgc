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

package de.micromata.mgc.jpa.xmldump.entities;

import org.junit.Ignore;

import de.micromata.genome.db.jpa.tabattr.api.TabAttributeEntry;
import de.micromata.genome.jpa.EmgrCallable;

/**
 * Map a map: http://java.dzone.com/articles/jpa-20-map-collections-%E2%80%93-basic
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class TestTabAttrJpaDAOImpl // extends TabAttrJpaDAOImpl
{
  public static final String[] searchColums = { "abrn", "senderName1", "senderName2", "recvName1", "senderName2",
      "recvStreet" };

  private boolean withNormSarch = false;

  public TabAttributeEntry create()
  {
    return new TestTabAttrDO();
  }

  public TestMasterAttrDO loadByPk(final Long pk)
  {
    final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
    return mgrfac.runInTrans(new EmgrCallable<TestMasterAttrDO, TestTabAttrEntityMgr>()
    {

      @Override
      public TestMasterAttrDO call(TestTabAttrEntityMgr mgr)
      {
        return mgr.findByPkAttached(TestMasterAttrDO.class, pk);
      }
    });
  }

  public void insert(final TestMasterAttrDO m)
  {
    final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
    mgrfac.runInTrans(new EmgrCallable<Void, TestTabAttrEntityMgr>()
    {

      @Override
      public Void call(TestTabAttrEntityMgr mgr)
      {
        mgr.insert(m);
        return null;
      }
    });
  }

  public void update(final TestMasterAttrDO m)
  {
    final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
    mgrfac.runInTrans(new EmgrCallable<Void, TestTabAttrEntityMgr>()
    {

      @Override
      public Void call(TestTabAttrEntityMgr mgr)
      {
        TestMasterAttrDO nm = mgr.selectByPkAttached(TestMasterAttrDO.class, m.getPk());
        nm.setRecvName1(m.getRecvName1());
        mgr.update(nm);
        return null;
      }
    });
  }

  public void delete(final TestMasterAttrDO m)
  {
    final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
    mgrfac.runInTrans(new EmgrCallable<Void, TestTabAttrEntityMgr>()
    {

      @Override
      public Void call(TestTabAttrEntityMgr mgr)
      {
        mgr.update(m);
        return null;
      }
    });
  }
}
