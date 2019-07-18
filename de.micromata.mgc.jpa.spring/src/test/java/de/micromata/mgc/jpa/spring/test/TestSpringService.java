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

import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Session;

import de.micromata.mgc.jpa.spring.test.entities.MySkillDO;
import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;

public class TestSpringService
{
  @PersistenceContext
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
  @Transactional()
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
