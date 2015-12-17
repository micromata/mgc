package de.micromata.genome.jpa.test;

import org.junit.Test;

import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.util.runtime.RuntimeCallable;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.common.test.ThreadedRunner;

/**
 * A threaded test of the emgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class ThreadedEmgrTest extends MgcTestCase
{
  @Test
  public void testThreaded()
  {
    int loopCount = 100;
    int threadCount = 4;
    new ThreadedRunner(loopCount, threadCount).run(new RuntimeCallable()
    {

      @Override
      public void call()
      {
        final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
        mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
        {

          @Override
          public Void call(JpaTestEntMgr mgr)
          {
            final GenomeJpaTestTableDO m = new GenomeJpaTestTableDO();
            m.setFirstName("Roger");
            mgr.insertAttached(m);
            mgr.update(m);
            mgr.remove(m);
            return null;
          }
        });
      }
    });
  }
}
