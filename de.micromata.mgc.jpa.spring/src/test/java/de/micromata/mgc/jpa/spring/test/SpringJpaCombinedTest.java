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

import de.micromata.genome.util.types.Holder;
import de.micromata.mgc.common.test.MgcTestCase5;
import de.micromata.mgc.jpa.spring.test.entities.MySkillDO;
import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-applicationContext-main.xml")
@Transactional
public class SpringJpaCombinedTest extends MgcTestCase5
{
  @Autowired
  TestSpringService testSpringService;

  @Test
  public void testUser()
  {
    MyUserDO user = new MyUserDO();
    user.setName("asdf");

    testSpringService.storeUser(user);

    MyUserDO loadedUser = testSpringService.loadUser(user.getPk());
    MySkillDO skill = SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
      MyUserDO theuser = emgr.selectByPkAttached(MyUserDO.class, user.getPk());
      MySkillDO mskill = new MySkillDO();
      mskill.setName("Coden");
      mskill.setUser(theuser);
      emgr.insertAttached(mskill);
      mskill.setUser(loadedUser);
      emgr.update(mskill);
      return mskill;
    });
    Assertions.assertNotNull(skill);
    Assertions.assertNotNull(skill.getPk());

  }

  @Test
  public void testNested()
  {
    MyUserDO user = new MyUserDO();
    user.setName("nestedUser");

    testSpringService.storeUser(user);
    testSpringService.doWithUser(user.getPk(), (myUser) -> {
      SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
        MySkillDO mskill = new MySkillDO();
        mskill.setName("Coden");
        mskill.setUser(myUser);
        emgr.insert(mskill);
        return null;
      });
    });
  }

  @Test
  public void testNestedEx()
  {
    MyUserDO user = new MyUserDO();
    user.setName("nestedUser");

    testSpringService.storeUser(user);
    Holder<Long> insertedSkillPk = new Holder<>();
    try {
      testSpringService.doWithUser(user.getPk(), (myUser) -> {
        SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
          MySkillDO mskill = new MySkillDO();
          mskill.setName("Coden");
          mskill.setUser(myUser);
          emgr.insert(mskill);
          insertedSkillPk.set(mskill.getPk());
          if (true) {
            throw new IllegalArgumentException("will nicht");
          }
          return null;
        });
      });
      Assertions.fail("should catch illegal");
    } catch (IllegalArgumentException ex) {
      // ignore
    }
    SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
      MySkillDO skill = emgr.findByPkDetached(MySkillDO.class, insertedSkillPk.get());
      Assertions.assertNull(skill);
      return skill;
    });
  }

  @Test
  public void testNestedEx2()
  {
    MyUserDO user = new MyUserDO();
    user.setName("nestedUser");

    testSpringService.storeUser(user);
    Holder<Long> insertedSkillPk = new Holder<>();

    long newUserPk = testSpringService.doWithNewUser("testNestedEx2", (myUser) -> {
      SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
        MySkillDO mskill = new MySkillDO();
        mskill.setName("Coden");
        mskill.setUser(myUser);
        emgr.insert(mskill);
        insertedSkillPk.set(mskill.getPk());
        if (true) {
          throw new IllegalArgumentException("will nicht");
        }
        return null;
      });
    });

    SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
      MySkillDO skill = emgr.findByPkDetached(MySkillDO.class, insertedSkillPk.get());
      Assertions.assertNull(skill);
      return skill;
    });
    MyUserDO nuser = testSpringService.loadUser(newUserPk);
    Assertions.assertNotNull(nuser);
  }
}
