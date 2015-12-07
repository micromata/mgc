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
