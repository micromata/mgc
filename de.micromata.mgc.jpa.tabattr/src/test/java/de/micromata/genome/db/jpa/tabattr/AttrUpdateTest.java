package de.micromata.genome.db.jpa.tabattr;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.tabattr.testentities.TestMasterAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgrFactory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AttrUpdateTest extends GenomeTestCase
{
  TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();

  String getLongStringValue(int length)
  {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; ++i) {
      sb.append("x");
    }
    return sb.toString();
  }

  @Test
  public void updateAttachedTest()
  {
    try {
      TestMasterAttrDO master = FirstTest.createMaster(22);
      String flong = getLongStringValue(60000);
      String longAttrKey = "longAttr";
      master.putAttribute(longAttrKey, flong);
      mgrfac.runInTrans((emgr) -> {
        emgr.insertDetached(master);
        return null;
      });
      TestMasterAttrDO raster;
      String rflognew;

      master.putAttribute(longAttrKey, "x");
      mgrfac.runInTrans((emgr) -> {
        emgr.merge(master);
        return null;
      });
      raster = mgrfac
          .runWoTrans((emgr) -> emgr.selectByPkDetached(TestMasterAttrDO.class, master.getPk()));
      rflognew = raster.getAttribute(longAttrKey, String.class);
      Assert.assertEquals("x", rflognew);

      String flongnew = getLongStringValue(62000);
      master.putAttribute(longAttrKey, flongnew);
      mgrfac.runInTrans((emgr) -> {
        emgr.merge(master);
        return null;
      });
      raster = mgrfac
          .runWoTrans((emgr) -> emgr.selectByPkDetached(TestMasterAttrDO.class, master.getPk()));
      rflognew = raster.getAttribute(longAttrKey, String.class);
      Assert.assertEquals(flongnew, rflognew);

    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
