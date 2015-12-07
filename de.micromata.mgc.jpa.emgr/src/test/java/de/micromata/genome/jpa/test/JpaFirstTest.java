package de.micromata.genome.jpa.test;

import org.junit.Test;

import de.micromata.genome.util.types.Holder;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.db.jpa.EmgrCallable;

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
        mgr.insert(d);
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
        mgr.insert(d);
        d.setFirstName("Roger Rene");
        mgr.update(d);
        mgr.remove(d);
        return null;
      }
    });

  }

}
