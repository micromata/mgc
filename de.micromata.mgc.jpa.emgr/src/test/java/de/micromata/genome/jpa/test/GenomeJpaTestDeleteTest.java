package de.micromata.genome.jpa.test;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.logging.LoggingContextServiceDefaultImpl;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * Tests to set entities to marked.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class GenomeJpaTestDeleteTest extends MgcTestCase
{
  String userName = "Roger";

  @Test
  public void testMarkDeleted()
  {

    LoggingServiceManager.get().setLoggingContextService(new LoggingContextServiceDefaultImpl()
    {

      @Override
      public String getCurrentUserName()
      {
        return userName;
      }

    });
    JpaTestEntMgrFactory emf = JpaTestEntMgrFactory.get();
    GenomeJpaTestTableDO ent = new GenomeJpaTestTableDO();
    ent.setFirstName("Roger");
    emf.runInTrans((emgr) -> emgr.insertDetached(ent));
    Assert.assertNotNull(ent.getPk());
    Assert.assertNotNull(ent.getCreatedAt());
    Assert.assertEquals(userName, ent.getModifiedBy());
    userName = "Kommer";
    boolean deleted = emf.runInTrans((emgr) -> emgr.markDeleted(ent));
    Assert.assertTrue(deleted);
    Assert.assertEquals(userName, ent.getModifiedBy());
    deleted = emf.runInTrans((emgr) -> emgr.markDeleted(ent));
    Assert.assertFalse(deleted);
    GenomeJpaTestTableDO sent = emf.runWoTrans((emgr) -> emgr.selectByPk(GenomeJpaTestTableDO.class, ent.getPk()));
    Assert.assertTrue(sent.isDeleted());
    Assert.assertEquals(userName, sent.getModifiedBy());

    boolean undeleted = emf.runInTrans((emgr) -> emgr.markUndeleted(ent));
    Assert.assertTrue(undeleted);
    sent = emf.runWoTrans((emgr) -> emgr.selectByPk(GenomeJpaTestTableDO.class, ent.getPk()));
    Assert.assertFalse(sent.isDeleted());
  }
}
