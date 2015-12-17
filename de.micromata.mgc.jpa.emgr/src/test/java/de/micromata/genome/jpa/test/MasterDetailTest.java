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
