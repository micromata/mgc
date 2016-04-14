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

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrTxTest extends MgcTestCase
{
  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  @Test
  public void testTxApi()
  {

    GenomeJpaTestTableDO td = emfac.tx().go((emgr) -> {
      GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
      d.setFirstName("Roger");
      emgr.insert(d);
      return d;
    });

    emfac.tx().rollback().go((emgr) -> {
      GenomeJpaTestTableDO tdr = emgr.selectByPkAttached(GenomeJpaTestTableDO.class, td.getPk());
      tdr.setFirstName("Rene");
      emgr.update(tdr);
      return null;
    });
    GenomeJpaTestTableDO ntd = emfac.notx()
        .go((emgr) -> emgr.selectByPkDetached(GenomeJpaTestTableDO.class, td.getPk()));
    Assert.assertEquals("Roger", ntd.getFirstName());
  }
}
