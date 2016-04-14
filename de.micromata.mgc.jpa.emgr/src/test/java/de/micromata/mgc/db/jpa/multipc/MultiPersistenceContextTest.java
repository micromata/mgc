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

package de.micromata.mgc.db.jpa.multipc;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.db.jpa.multipc.entities.MySkillDO;
import de.micromata.mgc.db.jpa.multipc.entities.MyUserDO;

public class MultiPersistenceContextTest extends MgcTestCase
{
  @BeforeClass
  public static void initLc()
  {
    LocalSettings.get();
  }

  @Test
  public void testFirst()
  {
    Long pk = MultiPcFirstEmgrFactory.get().runInTrans((emgr) -> {
      MyUserDO mu = new MyUserDO();
      mu.setName("Roger");
      emgr.insert(mu);
      return mu.getPk();
    });
    Assert.assertNotNull(pk);

  }

  @Test
  public void testSecond()
  {
    Long pk = MultiPcSecondEmgrFactory.get().runInTrans((emgr) -> {
      MySkillDO mu = new MySkillDO();
      mu.setName("Coding");
      emgr.insert(mu);
      return mu.getPk();
    });
    Assert.assertNotNull(pk);

  }
}
