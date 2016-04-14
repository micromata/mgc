//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
