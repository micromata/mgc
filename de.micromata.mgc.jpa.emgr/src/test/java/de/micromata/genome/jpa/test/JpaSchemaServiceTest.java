package de.micromata.genome.jpa.test;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.JpaSchemaService;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * Test of the JpaSchemaService.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaSchemaServiceTest extends MgcTestCase
{
  @Test
  public void testExport()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    JpaSchemaService schemaService = mgr.getJpaSchemaService();
    schemaService.exportCreateSchemaToFile("target/testcreate.sql");
    schemaService.exportDropSchemaToFile("target/testdrop.sql");
    Assert.assertTrue(new File("target/testcreate.sql").exists());
    Assert.assertTrue(new File("target/testdrop.sql").exists());
  }

  @Test
  public void testClearDb()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    JpaSchemaService schemaService = mgr.getJpaSchemaService();
    schemaService.clearDatabase();
  }
}
