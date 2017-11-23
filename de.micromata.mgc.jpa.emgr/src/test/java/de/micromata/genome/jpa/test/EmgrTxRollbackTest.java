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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

public class EmgrTxRollbackTest extends MgcTestCase
{

  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  /**
   * Call insert.
   *
   * @param name the name
   * @param toThrow the to throw
   * @return the genome jpa test table do
   */
  GenomeJpaTestTableDO callInsertRollback(String name, RuntimeException toThrow)
  {
    return emfac.tx().rollback().go((emgr) -> {
      GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
      d.setFirstName(name);
      emgr.insert(d);
      if (toThrow != null) {
        throw toThrow;
      }
      return d;
    });
  }

  /**
   * Test required.
   */
  @Test
  public void testRollback()
  {
    callInsertRollback("Roger", null);
  }

  /**
   * Test required.
   */
  @Test
  public void testRollbackNested()
  {
    GenomeJpaTestTableDO d = emfac.tx().go((emgr) -> {
      return callInsertRollback("testRollbackNested", null);
    });
    List<GenomeJpaTestTableDO> list = emfac.tx()
        .go((emgr) -> emgr.select(GenomeJpaTestTableDO.class,
            "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName", "firstName",
            "testRollbackNested"));
    Assert.assertEquals(0, list.size());
  }

}
