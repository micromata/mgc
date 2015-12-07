package de.micromata.mgc.jpa.spring.test;

import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.spring.test.entities.MyUserDO;

public class JpaTest extends MgcTestCase
{

  @Test
  public void testJpa()
  {
    SpringJpaEmgrFactory.get().runInTrans((emgr) -> {
      MyUserDO ent = new MyUserDO();
      ent.setName("Roger");
      emgr.insert(ent);
      return ent;
    });
  }

}
