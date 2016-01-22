package de.micromata.mgc.jpa.xmldump;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.db.jpa.xmldump.api.JpaXmlDumpService;
import de.micromata.genome.db.jpa.xmldump.api.JpaXmlDumpService.RestoreMode;
import de.micromata.genome.db.jpa.xmldump.impl.JpaXmlDumpServiceImpl;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.xmldump.entities.TestByLazyRef;
import de.micromata.mgc.jpa.xmldump.entities.TestMasterAttrDO;
import de.micromata.mgc.jpa.xmldump.entities.TestWithLazyRef;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class XmlDumpTest extends MgcTestCase
{
  JpaXmlDumpService xmlDumpService = new JpaXmlDumpServiceImpl();
  XmlDumpEntityMgrFactory emfac = XmlDumpEntityMgrFactory.get();

  private String createLongAttr(int size)
  {
    StringBuilder sb = new StringBuilder(size);
    for (int i = 0; i < size; ++i) {
      sb.append("x");
    }
    return sb.toString();
  }

  private void createTestData()
  {
    emfac.runInTrans((emgr) -> {
      TestByLazyRef or = new TestByLazyRef();
      emgr.insertAttached(or);
      TestWithLazyRef wl = new TestWithLazyRef();
      wl.setOther(or);
      emgr.insertAttached(wl);
      wl = new TestWithLazyRef();
      wl.setOther(or);
      emgr.insertAttached(wl);

      TestWithLazyRef cwl = new TestWithLazyRef();
      cwl.setParent(wl);
      emgr.insertAttached(cwl);
      TestWithLazyRef pwl = new TestWithLazyRef();
      emgr.insertAttached(pwl);
      wl.setParent(pwl);
      emgr.update(pwl);

      TestMasterAttrDO tm = new TestMasterAttrDO();
      tm.setAbrn("XmlDumpTest");
      tm.putAttribute("myProp", "My Value");
      tm.putAttribute("myLongProp", createLongAttr(6000));
      emgr.insert(tm);
      return null;
    });
  }

  @Test
  public void testDump()
  {
    try {
      createTestData();
      File file = new File("target/db-dump1.xml");
      xmlDumpService.dumpToXml(emfac, file);

      emfac.getJpaSchemaService().clearDatabase();

      xmlDumpService.restoreDb(emfac, file, RestoreMode.InsertAll);

      emfac.runInTrans((emgr) -> {
        List<TestMasterAttrDO> ret = emgr.selectDetached(TestMasterAttrDO.class,
            "select e from " + TestMasterAttrDO.class.getName() + " e where e.abrn = :abrn", "abrn", "XmlDumpTest");
        Assert.assertTrue(ret.isEmpty() == false);
        TestMasterAttrDO attr = ret.get(0);
        Assert.assertEquals("My Value", attr.getStringAttribute("myProp"));
        Assert.assertEquals(createLongAttr(6000), attr.getStringAttribute("myLongProp"));
        return null;
      });

    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
