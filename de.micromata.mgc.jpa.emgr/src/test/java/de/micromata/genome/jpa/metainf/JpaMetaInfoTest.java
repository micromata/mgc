package de.micromata.genome.jpa.metainf;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.test.JpaTestEntMgrFactory;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetaInfoTest extends MgcTestCase
{
  @Test
  public void testMember()
  {
    JpaTestEntMgrFactory fac = JpaTestEntMgrFactory.get();
    JpaMetadataRepostory repo = fac.getMetadataRepository();
    Set<Class<?>> keyset = repo.getEntities().keySet();

    int len = repo.getMaxLength(MyAnotTestDO.class, "testName");

    Assert.assertEquals(30, len);
    MyAnnotation foundanot = repo.findColumnAnnotation(MyAnotTestDO.class, "testName",
        MyAnnotation.class);
    Assert.assertNotNull(foundanot);

    ColumnMetadata colmeta = repo.findColumnMetadata(MyAnotTestDO.class, "testName");
    Assert.assertNotNull(colmeta);
    Assert.assertNotNull(colmeta.getGetter());
    Assert.assertNotNull(colmeta.getSetter());

    MyAnotTestDO myBean = new MyAnotTestDO();
    myBean.setTestName("Roger");
    String tn = (String) colmeta.getGetter().get(myBean);
    Assert.assertEquals("Roger", tn);
    colmeta.getSetter().set(myBean, "Kommer");
    Assert.assertEquals("Kommer", myBean.getTestName());
  }
}
