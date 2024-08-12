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

import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.RuntimeCallable;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.common.test.ThreadedRunner;

/**
 * Tests the select for update mechanism.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class JpaSelectForUpdateTest extends MgcTestCase
{
  @Test
  public void testBasicUpdate()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    final GenomeJpaTestTableDO ttable = new GenomeJpaTestTableDO();
    ttable.setFirstName("JpaSelectForUpdate");
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {

      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        emgr.insert(ttable);
        return null;
      }
    });

    final Long pk = ttable.getPk();
    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {
      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        TypedQuery<GenomeJpaTestTableDO> query = emgr.createQuery(GenomeJpaTestTableDO.class,
            "select m from GenomeJpaTestTableDO m where m.pk = :pk", "pk", pk);
        int timems = 2000;
        emgr.setSelectForUpdate(query, timems);
        GenomeJpaTestTableDO seltable = query.getSingleResult();
        Assert.assertEquals(seltable.getFirstName(), ttable.getFirstName());
        seltable.setFirstName("othername");
        emgr.update(seltable);
        return null;
      }
    });
    // GenomeJpaTestTableDO d = ent.find(GenomeJpaTestTableDO.class, pk);
  }

  /**
   * Mehrere threads read/update auf einer Zeile.
   * 
   * Durch select for update muss das ergenis die summe sein.
   * 
   */
  @Test
  public void testIncrement()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    final GenomeJpaTestTableDO ttable = new GenomeJpaTestTableDO();
    ttable.setFirstName("0");

    mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {
      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        emgr.insert(ttable);
        return null;
      }
    });

    final Long pk = ttable.getPk();
    // threads incrementing it.
    RuntimeCallable run = new RuntimeCallable() {

      @Override
      public void call()
      {
        mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {
          @Override
          public Void call(JpaTestEntMgr emgr)
          {
            TypedQuery<GenomeJpaTestTableDO> query = emgr.createQuery(GenomeJpaTestTableDO.class,
                "select m from GenomeJpaTestTableDO m where m.pk = :pk", "pk", pk);
            int timems = -1;
            emgr.setSelectForUpdate(query, timems);
            GenomeJpaTestTableDO seltable = query.getSingleResult();
            int cc = Integer.parseInt(seltable.getFirstName());
            ++cc;
            seltable.setFirstName(Integer.toString(cc));
            emgr.update(seltable);
            return null;
          }
        });

      }
    };

    final int loops = 1000;
    final int threads = 5;
    ThreadedRunner trunner = new ThreadedRunner(loops, threads);
    trunner.run(run);
    mgr.runInTrans(new EmgrCallable<Object, JpaTestEntMgr>() {
      @Override
      public Object call(JpaTestEntMgr emgrr)
      {

        String firstname = emgrr.createQuery(GenomeJpaTestTableDO.class, "select m from GenomeJpaTestTableDO m where m.pk = :pk", "pk", pk)
            .getSingleResult().getFirstName();
        int executed = Integer.parseInt(firstname);
        Assert.assertEquals(loops * threads, executed);
        return null;
      }
    });

  }

  /**
   * Dieser Test scheint fuer Derby nicht zu funktionieren, da Derby kein Timeout kann.
   */
  @Test
  public void testTimeOut()
  {
    final JpaTestEntMgrFactory mgrfac = JpaTestEntMgrFactory.get();
    final GenomeJpaTestTableDO ttable = new GenomeJpaTestTableDO();
    ttable.setFirstName("0");

    mgrfac.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {
      @Override
      public Void call(JpaTestEntMgr emgr)
      {
        emgr.insert(ttable);
        return null;
      }
    });

    final Long pk = ttable.getPk();
    // threads incrementing it.
    RuntimeCallable run = new RuntimeCallable() {

      @Override
      public void call()
      {
        mgrfac.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {
          @Override
          public Void call(JpaTestEntMgr emgr)
          {
            System.out.println("1; start locking thread");
            int timeoutms = 5000;
            TypedQuery<GenomeJpaTestTableDO> query = emgr.createQuery(GenomeJpaTestTableDO.class,
                "select m from GenomeJpaTestTableDO m where m.pk = :pk", "pk", pk);

            emgr.setSelectForUpdate(query, timeoutms);
            GenomeJpaTestTableDO seltable = query.getSingleResult();
            int cc = Integer.parseInt(seltable.getFirstName());
            ++cc;
            seltable.setFirstName(Integer.toString(cc));
            emgr.update(seltable);
            // schlafe 5 sekunden
            sleep(timeoutms);
            System.out.println("1; finished locking thread");
            return null;
          }
        });

      }
    };

    ThreadedRunner trunner = new ThreadedRunner(1, 1);
    trunner.start(run);
    sleep(300);
    long startt = System.currentTimeMillis();
    try {
      mgrfac.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>() {
        @Override
        public Void call(JpaTestEntMgr emgr)
        {
          TypedQuery<GenomeJpaTestTableDO> query = emgr.createQuery(GenomeJpaTestTableDO.class,
              "select m from GenomeJpaTestTableDO m where m.pk = :pk", "pk", pk);
          int timems = 1000;
          System.out.println("2; Start try query");
          emgr.setSelectForUpdate(query, timems);
          GenomeJpaTestTableDO seltable = query.getSingleResult();
          int cc = Integer.parseInt(seltable.getFirstName());
          ++cc;
          seltable.setFirstName(Integer.toString(cc));
          emgr.update(seltable);
          System.out.println("2; finished tried query");
          return null;
        }
      });
      // should get QueryTimeoutException
      // or LockTimeoutException
      if (LocalSettings.get().get("genomeds").equals("localderby") == false) {
        Assert.fail("Should get LockTimeoutException");
      }
    } catch (LockTimeoutException ex) {
      // logging ist [ORA-00054: Ressource belegt und Anforderung mit NOWAIT angegeben oder Timeout abgelaufen
      System.out.println("Expected ex after " + (System.currentTimeMillis() - startt) + " ms: " + ex.getMessage());
    }
    trunner.join();
  }
}
