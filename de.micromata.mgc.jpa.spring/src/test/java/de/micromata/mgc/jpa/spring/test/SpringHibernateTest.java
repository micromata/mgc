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
