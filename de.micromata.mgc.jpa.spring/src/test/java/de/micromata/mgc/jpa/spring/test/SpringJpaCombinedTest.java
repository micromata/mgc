package de.micromata.mgc.jpa.spring.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.micromata.genome.util.types.Holder;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.spring.test.entities.MySkillDO;
import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext-main.xml")
public class SpringJpaCombinedTest extends MgcTestCase
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
      emgr.insert(mskill);
      mskill.setUser(loadedUser);
      emgr.update(mskill);
      return mskill;
    });
    Assert.assertNotNull(skill);
    Assert.assertNotNull(skill.getPk());

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
      Assert.fail("should catch illegal");
    } catch (IllegalArgumentException ex) {
      // ignore
    }
    SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
      MySkillDO skill = emgr.findByPkDetached(MySkillDO.class, insertedSkillPk.get());
      Assert.assertNull(skill);
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
      Assert.assertNull(skill);
      return skill;
    });
    MyUserDO nuser = testSpringService.loadUser(newUserPk);
    Assert.assertNotNull(nuser);
  }
}
