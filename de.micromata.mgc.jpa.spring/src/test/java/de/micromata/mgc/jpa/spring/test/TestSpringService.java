package de.micromata.mgc.jpa.spring.test;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import de.micromata.mgc.jpa.spring.test.entities.MySkillDO;
import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;

public class TestSpringService
{
  @Autowired
  Session hibernateSession;

  @Transactional(value = TxType.REQUIRES_NEW)
  public Long storeUser(MyUserDO user)
  {
    hibernateSession.persist(user);
    hibernateSession.flush();
    Long pk = user.getPk();
    return pk;
  }

  public MyUserDO loadUserNoTx(Long pk)
  {
    return hibernateSession.load(MyUserDO.class, pk);
  }

  @Transactional()
  public MyUserDO loadUser(Long pk)
  {
    return hibernateSession.load(MyUserDO.class, pk);
  }

  public Long storeSkill(MySkillDO skill)
  {
    hibernateSession.persist(skill);
    return skill.getPk();
  }

  @Transactional()
  public MySkillDO loadSkill(Long pk)
  {
    return hibernateSession.load(MySkillDO.class, pk);
  }

  public static interface WithUser
  {
    void doWithUser(MyUserDO user);
  }

  @Transactional
  public void doWithUser(Long userPk, WithUser withUser)
  {
    MyUserDO user = loadUser(userPk);
    withUser.doWithUser(user);
  }

  @Transactional
  public Long doWithNewUser(String name, WithUser withUser)
  {
    MyUserDO user = new MyUserDO();
    user.setName(name);
    storeUser(user);
    try {
      withUser.doWithUser(user);
    } catch (IllegalArgumentException ex) {
      // expected
    }
    return user.getPk();
  }
}
