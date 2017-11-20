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

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class Emgr2NestedTransTest extends MgcTestCase
{
  JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();

  private GenomeJpaTestTableDO selectAData(final Long pk)
  {
    GenomeJpaTestTableDO table = mgr.runInTrans(new EmgrCallable<GenomeJpaTestTableDO, JpaTestEntMgr>()
    {

      @Override
      public GenomeJpaTestTableDO call(JpaTestEntMgr emgr)
      {
        return emgr.findByPkAttached(GenomeJpaTestTableDO.class, pk);
      }
    });
    return table;
  }

  private Long insertData(final GenomeJpaTestTableDO table)
  {
    final Long pk = mgr.runInTrans(new EmgrCallable<Long, JpaTestEntMgr>()
    {

      @Override
      public Long call(JpaTestEntMgr emgr)
      {
        emgr.insert(table);
        //        emgr.detach(table);
        return table.getPk();
      }
    });
    return pk;
  }

  @Test
  public void testSomeStuff()
  {
    final String name = "EmgrNestedTransTest";

    GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
    d.setFirstName(name);
    final Long pk = insertData(d);

    GenomeJpaTestTableDO table = selectAData(pk);
    Assert.assertEquals(name, table.getFirstName());

    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr emgr) throws RuntimeException
      {

        GenomeJpaTestTableDO table = selectAData(pk);
        String name2 = table.getFirstName() + "2";
        GenomeJpaTestTableDO table2 = new GenomeJpaTestTableDO();
        table2.setFirstName(name2);
        insertData(table2);
        return null;
      }
    });
  }

  @Test
  public void testInsideNoTrans()
  {
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        testSomeStuff();
        return null;
      }
    });
  }

  @Test
  public void testInsideTrans()
  {
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        testSomeStuff();
        return null;
      }
    });
  }

  @Test
  public void testTrans3()
  {
    final GenomeJpaTestTableDO table = new GenomeJpaTestTableDO();
    table.setFirstName("EmgrNestedTransTest_testTrans3");

    final Long pk = insertData(table);
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
    {

      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
        {

          @Override
          public Void call(JpaTestEntMgr emgr)
          {
            table.setFirstName(table.getFirstName() + "_mod");
            emgr.merge(table);
            return null;
          }
        });
        return null;
      }
    });
  }

  @Test
  public void testMerge()
  {
    final GenomeJpaTestTableDO table = new GenomeJpaTestTableDO();
    table.setFirstName("EmgrMergeTest");
    final GenomeJpaTestTableDO result = mgr.runInTrans(new EmgrCallable<GenomeJpaTestTableDO, JpaTestEntMgr>()
    {

      @Override
      public GenomeJpaTestTableDO call(JpaTestEntMgr emgr)
      {
        GenomeJpaTestTableDO merge = emgr.merge(table);
        return merge;
      }
    });

    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getPk());

    GenomeJpaTestTableDO find = mgr.runInTrans(new EmgrCallable<GenomeJpaTestTableDO, JpaTestEntMgr>()
    {

      @Override
      public GenomeJpaTestTableDO call(JpaTestEntMgr emgr)
      {
        GenomeJpaTestTableDO find = emgr.findByPkAttached(GenomeJpaTestTableDO.class, result.getPk());
        return find;
      }
    });

    Assert.assertNotNull(find);

    find.setFirstName("modified");
    final GenomeJpaTestTableDO mergeThis = find;
    mgr.runInTrans(new EmgrCallable<GenomeJpaTestTableDO, JpaTestEntMgr>()
    {

      @Override
      public GenomeJpaTestTableDO call(JpaTestEntMgr emgr)
      {
        GenomeJpaTestTableDO merge = emgr.merge(mergeThis);
        return merge;
      }
    });

    final long pk = find.getPk();
    find = mgr.runInTrans(new EmgrCallable<GenomeJpaTestTableDO, JpaTestEntMgr>()
    {

      @Override
      public GenomeJpaTestTableDO call(JpaTestEntMgr emgr)
      {
        return emgr.findByPkDetached(GenomeJpaTestTableDO.class, pk);
      }
    });

    Assert.assertNotNull(find);
    Assert.assertEquals("modified", find.getFirstName());
    Assert.assertEquals(find.getPk(), mergeThis.getPk());
  }
}
