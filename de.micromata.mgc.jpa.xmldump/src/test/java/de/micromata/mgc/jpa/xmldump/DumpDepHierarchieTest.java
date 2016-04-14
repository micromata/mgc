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

package de.micromata.mgc.jpa.xmldump;

import java.util.List;

import org.junit.Test;

import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.MetaInfoUtils;
import de.micromata.mgc.common.test.MgcTestCase;

public class DumpDepHierarchieTest extends MgcTestCase
{
  XmlDumpEntityMgrFactory emfac = XmlDumpEntityMgrFactory.get();

  @Test
  public void testDump()
  {
    List<EntityMetadata> tables = emfac.getMetadataRepository().getTableEntities();
    System.out.println(MetaInfoUtils.dumpMetaTableReferenceByTree(tables, true));
  }

}
