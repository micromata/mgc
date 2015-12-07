package de.micromata.genome.jpa.test;

import javax.persistence.EntityManager;

import org.junit.Ignore;
import org.junit.Test;

import de.micromata.genome.util.runtime.RuntimeCallable;
import de.micromata.mgc.common.test.ThreadedRunner;

/**
 * Just a playzone for raw threading tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class RawThreadedTest extends RawJPATest
{
  int loopCount = 100;

  int threadCount = 5;

  public static void doItWithMultipleTrans()
  {
    long pk = RawJPATest.insert();
    RawJPATest.update(pk);
    RawJPATest.delete(pk);
  }

  public static void doItWithOneTrans()
  {
    EntityManager ent = RawJPATest.entityManagerFactory.createEntityManager();
    ent.getTransaction().begin();

    long pk = RawJPATest.insertNoTrans(ent);
    RawJPATest.updateNoTrans(ent, pk);
    RawJPATest.deleteNoTrans(ent, pk);
    ent.getTransaction().commit();
    ent.close();
  }

  @Test
  public void testThreaded()
  {
    RawJPATest.createEntityManagerFactory();
    new ThreadedRunner(loopCount, threadCount).run(new RuntimeCallable() {

      @Override
      public void call()
      {
        doItWithOneTrans();
      }
    });
  }
}
