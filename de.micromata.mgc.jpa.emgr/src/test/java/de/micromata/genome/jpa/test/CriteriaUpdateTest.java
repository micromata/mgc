package de.micromata.genome.jpa.test;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.Clauses;
import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * Test criteria Update
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class CriteriaUpdateTest extends MgcTestCase
{
  JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  /**
   * Check if listener is working
   */
  @Test
  public void testUpdateModified()
  {
    GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
    d.setFirstName("CriteriaUpdateTest");
    emfac.tx().go(emgr -> emgr.insertDetached(d));
    // to get difference between createdAt and modifiedAt
    sleep(2000);
    // make an update
    emfac.tx().go(emgr -> {
      CriteriaUpdate<GenomeJpaTestTableDO> cu = CriteriaUpdate.createUpdate(GenomeJpaTestTableDO.class);
      cu.addWhere(Clauses.equal("firstName", "CriteriaUpdateTest"))
          .set("firstName", "CriteriaUpdateTest");
      return emgr.update(cu);
    });
    GenomeJpaTestTableDO dl = emfac.tx().go(emgr -> emgr.selectByPk(GenomeJpaTestTableDO.class, d.getPk()));
    Assert.assertTrue(dl.getCreatedAt().getTime() < dl.getModifiedAt().getTime());
  }
}
