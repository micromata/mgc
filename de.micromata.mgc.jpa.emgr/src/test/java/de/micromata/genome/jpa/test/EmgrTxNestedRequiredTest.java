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

import javax.persistence.TransactionRequiredException;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrTxNestedRequiredTest extends MgcTestCase
{
  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  @Test
  public void testRequiredFail()
  {
    try {
      callInsert("Roger");
      Assert.fail("Missing required exception TransactionRequiredException");
    } catch (TransactionRequiredException ex) {
      // expected
    }
  }

  GenomeJpaTestTableDO callInsert(String name)
  {
    return emfac.tx().requires().go((emgr) -> {
      GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
      d.setFirstName(name);
      emgr.insert(d);
      return d;
    });
  }

  @Test
  public void testRequired()
  {
    emfac.tx().go((emgr) -> {
      return callInsert("Roger");
    });
  }

  @Test
  public void testRequiredRollback()
  {
    GenomeJpaTestTableDO d = emfac.tx().rollback().go((emgr) -> {
      return callInsert("testRequiredRollback");
    });

    List<GenomeJpaTestTableDO> list = emfac.notx().go((emgr) -> emgr.select(GenomeJpaTestTableDO.class,
        "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName", "firstName",
        "testRequiredRollback"));
    Assert.assertEquals(0, list.size());
  }
}
