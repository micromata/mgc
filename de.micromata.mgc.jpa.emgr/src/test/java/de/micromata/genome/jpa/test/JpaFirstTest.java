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
import de.micromata.genome.util.types.Holder;
import de.micromata.mgc.common.test.MgcTestCase;

public class JpaFirstTest extends MgcTestCase
{

  @Test
  public void testIn3Trans()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    try {
      final Holder<Long> pk = new Holder<Long>();
      mgr.runInTrans((emgr) -> {
        GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
        d.setFirstName("Roger");
        emgr.insert(d);
        pk.set(d.getPk());
        return null;
      });
      mgr.runInTrans((emgr) -> {
        GenomeJpaTestTableDO d = emgr.selectByPkAttached(GenomeJpaTestTableDO.class, pk.get());
        d.setFirstName("Roger Rene");
        emgr.update(d);
        return null;
      });
      mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
      {

        @Override
        public Void call(JpaTestEntMgr mgr)
        {
          GenomeJpaTestTableDO d = mgr.selectByPkAttached(GenomeJpaTestTableDO.class, pk.get());
          mgr.remove(d);
          return null;
        }
      });
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      ex.printStackTrace();
      throw ex;
    }
  }

  @Test
  public void testFirst()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr mgr)
      {
        GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
        d.setFirstName("Roger");
        mgr.insertAttached(d);
        d.setFirstName("Roger Rene");
        mgr.update(d);
        mgr.remove(d);
        return null;
      }
    });

  }

  @Test
  public void testInTrans()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr mgr)
      {
        GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
        d.setFirstName("Roger");
        mgr.insertAttached(d);
        d.setFirstName("Roger Rene");
        mgr.update(d);
        mgr.remove(d);
        return null;
      }
    });

  }

}
