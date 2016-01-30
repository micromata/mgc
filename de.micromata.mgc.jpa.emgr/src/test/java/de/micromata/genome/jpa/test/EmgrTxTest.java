package de.micromata.genome.jpa.test;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrTxTest extends MgcTestCase
{
  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  @Test
  public void testTxApi()
  {

    GenomeJpaTestTableDO td = emfac.tx().go((emgr) -> {
      GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
      d.setFirstName("Roger");
      emgr.insert(d);
      return d;
    });

    emfac.tx().rollback().go((emgr) -> {
      GenomeJpaTestTableDO tdr = emgr.selectByPkAttached(GenomeJpaTestTableDO.class, td.getPk());
      tdr.setFirstName("Rene");
      emgr.update(tdr);
      return null;
    });
    GenomeJpaTestTableDO ntd = emfac.notx()
        .go((emgr) -> emgr.selectByPkDetached(GenomeJpaTestTableDO.class, td.getPk()));
    Assert.assertEquals("Roger", ntd.getFirstName());
  }
}
