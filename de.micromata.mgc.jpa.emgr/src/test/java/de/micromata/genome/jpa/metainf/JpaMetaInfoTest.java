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
    Set<Class<?>> keyset = fac.getMetadataRepository().getEntities().keySet();

    int len = fac.getMetadataRepository().getMaxLength(MyAnotTestDO.class, "testName");

    Assert.assertEquals(30, len);
    MyAnnotation foundanot = fac.getMetadataRepository().findColumnAnnotation(MyAnotTestDO.class, "testName",
        MyAnnotation.class);
    Assert.assertNotNull(foundanot);
  }
}
