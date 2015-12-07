package de.micromata.genome.db.jpa.tabattr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestMasterAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgr;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgrFactory;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrJpaDAOImpl;
import de.micromata.genome.jpa.EmgrCallable;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class BaseOpTest extends GenomeTestCase
{
  @Test
  public void testOps()
  {
    try {
      final TestTabAttrJpaDAOImpl dao = new TestTabAttrJpaDAOImpl();
      TestMasterAttrDO master = FirstTest.createMaster(dao, 10, 1);
      dao.insert(master);
      master.setRecvName1("DHL Headquarter");
      // master.putAttribute("Key No " + 1, "New Value");
      master.putStringAttribute("Key No " + 1, "A value to write");
      // master.putAttribute("Key No 0", "Also new: " + FirstTest.genLongString());
      dao.update(master);

    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      ex.printStackTrace();
      throw ex;
    }
  }

  @Test
  public void selectDetailOnly()
  {
    final TestTabAttrJpaDAOImpl dao = new TestTabAttrJpaDAOImpl();
    TestMasterAttrDO master = FirstTest.createMaster(dao, 10, 1);
    dao.insert(master);
    JpaTabAttrBaseDO<TestMasterAttrDO, Long> key = master.getAttributeRow("Key No " + 1);
    final Long pk = key.getPk();
    final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
    mgrfac.runWoTrans(new EmgrCallable<Void, TestTabAttrEntityMgr>()
    {

      @Override
      public Void call(TestTabAttrEntityMgr mgr)
      {
        TestTabAttrDO attr = mgr.selectByPkAttached(TestTabAttrDO.class, pk);
        TestTabAttrDO attr2 = mgr.createQuery(TestTabAttrDO.class, "select m from TestTabAttrDO m where m.pk = :pk")
            .setParameter("pk", pk)
            .getSingleResult();
        TestMasterAttrDO parent = attr.getParent();
        parent.getPk();
        return null;
      }
    });

  }

  /**
   * Tests if the longValue is returned in the right order
   */
  @Test
  public void testLongDataValues()
  {
    final TestTabAttrJpaDAOImpl dao = new TestTabAttrJpaDAOImpl();
    TestMasterAttrDO master = FirstTest.createMaster(dao, 0, 1);

    String longValueKey = "LONGVALUE";
    String aString = StringUtils.leftPad("", JpaTabAttrDataBaseDO.DATA_MAXLENGTH, 'A');
    String longString = aString;
    String bString = StringUtils.leftPad("", JpaTabAttrDataBaseDO.DATA_MAXLENGTH, 'B');
    longString += bString;
    String cString = StringUtils.leftPad("", JpaTabAttrDataBaseDO.DATA_MAXLENGTH, 'C');
    longString += cString;
    String dString = StringUtils.leftPad("", JpaTabAttrDataBaseDO.DATA_MAXLENGTH, 'D');
    longString += dString;

    master.putAttribute(longValueKey, longString);
    dao.insert(master);

    TestMasterAttrDO testMasterAttrDO = dao.loadByPk(master.getPk());
    JpaTabAttrBaseDO<TestMasterAttrDO, Long> testMasterAttrDOJpaTabAttrBaseDO = testMasterAttrDO.getAttributes()
        .get(longValueKey);
    List<JpaTabAttrDataBaseDO<?, Long>> data = testMasterAttrDOJpaTabAttrBaseDO.getData();
    Assert.assertEquals(3, data.size());

    Assert.assertEquals(0, data.get(0).getDatarow());
    Assert.assertEquals(1, data.get(1).getDatarow());
    Assert.assertEquals(2, data.get(2).getDatarow());

    JpaTabAttrDataBaseDO<?, Long> jpaTabAttrDataBaseDO = data.get(0);
    String attrData1 = jpaTabAttrDataBaseDO.getData();
    Assert.assertEquals(bString, attrData1);

    JpaTabAttrDataBaseDO<?, Long> jpaTabAttrDataBaseDO2 = data.get(2);
    String attrData2 = jpaTabAttrDataBaseDO2.getData();
    Assert.assertEquals(dString, attrData2);

    String attribute = testMasterAttrDO.getAttribute(longValueKey, String.class);
    Assert.assertEquals(longString, attribute);
  }

  @Test
  public void testLargeByteObjects() throws IOException
  {
    final TestTabAttrJpaDAOImpl dao = new TestTabAttrJpaDAOImpl();
    TestMasterAttrDO master = FirstTest.createMaster(dao, 0, 1);

    String longValueKey = "LONGVALUE";

    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("largeImage.jpg");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    IOUtils.copy(resourceAsStream, baos);
    byte[] bytes = baos.toByteArray();
    master.putAttribute(longValueKey, bytes);
    dao.insert(master);
    TestMasterAttrDO testMasterAttrDO = dao.loadByPk(master.getPk());
    byte[] attribute = (byte[]) testMasterAttrDO.getAttribute(longValueKey);
    Assert.assertArrayEquals(bytes, attribute);
  }

}
