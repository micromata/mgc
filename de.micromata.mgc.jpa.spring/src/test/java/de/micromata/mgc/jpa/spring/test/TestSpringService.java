package de.micromata.mgc.jpa.spring.test;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;

public class TestSpringService
{
  @Autowired
  Session hibernateSession;

  @Transactional
  public void storeUser(MyUserDO user)
  {
    hibernateSession.persist(user);
    Long pk = user.getPk();
  }
}
