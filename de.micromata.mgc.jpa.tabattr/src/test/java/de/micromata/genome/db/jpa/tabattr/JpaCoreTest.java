package de.micromata.genome.db.jpa.tabattr;

import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.tabattr.testentities.TJpaMasterAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TJpaMasterDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgrFactory;

public class JpaCoreTest extends GenomeTestCase
{
  TestTabAttrEntityMgrFactory emgfac = TestTabAttrEntityMgrFactory.get();

  @Test
  public void testCore()
  {
    try {
      String key = "key";
      TJpaMasterDO nm = emgfac.runInTrans((emgr) -> {
        TJpaMasterDO master = new TJpaMasterDO();
        master.putAttribute(key, "Hello");
        emgr.getEntityManager().persist(master);
        return master;
      });

      //    nm.putAttribute(key, "newVal");
      //    emgfac.runInTrans((emgr) -> {
      //      emgr.getEntityManager().merge(nm);
      //      return null;
      //    });

      nm.putNewAttribute(key, "asdfasdfsd");
      emgfac.runInTrans((emgr) -> {
        emgr.getEntityManager().merge(nm);
        return null;
      });
      TJpaMasterAttrDO attr = nm.getAttributes().get(key);
      attr.getPk();
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
