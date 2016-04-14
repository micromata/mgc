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

package de.micromata.mgc.jpa.spring.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.spring.test.entities.MySkillDO;
import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext-main.xml")
public class SpringHibernateTest extends MgcTestCase
{
  @Autowired
  TestSpringService testSpringService;

  @Test
  public void testUser()
  {
    MyUserDO user = new MyUserDO();
    user.setName("asdf");

    testSpringService.storeUser(user);

    user = testSpringService.loadUser(user.getPk());
    Assert.assertNotNull(user);
    user = testSpringService.loadUserNoTx(user.getPk());
    Assert.assertNotNull(user);

    MySkillDO sk = new MySkillDO();
    sk.setName("Coden");
    sk.setUser(user);
    testSpringService.storeSkill(sk);

  }
}
