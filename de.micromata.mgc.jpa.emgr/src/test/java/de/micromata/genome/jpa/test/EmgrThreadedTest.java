package de.micromata.genome.jpa.test;

import org.junit.Test;

import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.util.runtime.RuntimeCallable;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.common.test.ThreadedRunner;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class EmgrThreadedTest extends MgcTestCase
{
  @Test
  public void testThreaded()
  {
    int loops = 100;
    int threadCount = 4;
    new ThreadedRunner(loops, threadCount).run(new RuntimeCallable()
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
    });
  }
}
