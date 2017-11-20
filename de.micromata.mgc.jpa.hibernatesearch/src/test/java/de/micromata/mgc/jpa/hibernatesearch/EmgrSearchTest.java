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

package de.micromata.mgc.jpa.hibernatesearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * Basic search test.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrSearchTest extends HibernateSearchTestBase
{
  @Test
  public void testSearch()
  {
    // your factory
    HibernateSearchTestEmgrFactory emf = HibernateSearchTestEmgrFactory.get();

    // insert an entity 
    MyEntityDO net = new MyEntityDO();
    net.setName("Bla");
    net.setLoginName("Blub");
    net.setNotSearchable("NOTFOUND");
    emf.runInTrans((emgr) -> emgr.insert(net));

    // now search 
    List<MyEntityDO> found = emf.tx().go(emgr -> {
      List<MyEntityDO> lret = emgr.searchAttached("bla", MyEntityDO.class, "name");
      return lret;
    });

    Assert.assertEquals(1, found.size());

    found = emf.runInTrans(emgr -> {
      List<MyEntityDO> lret = emgr.searchAttached("blub", MyEntityDO.class, "name");
      return lret;
    });

    Assert.assertEquals(0, found.size());

    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("blub", MyEntityDO.class);
      return lret;
    });
    Assert.assertEquals(1, found.size());

    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("bla", MyEntityDO.class);
      return lret;
    });
    Assert.assertEquals(1, found.size());

    found = emf.runInTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("NOTFOUND", MyEntityDO.class);
      return lret;
    });
    Assert.assertEquals(0, found.size());
  }
}
