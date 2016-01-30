package de.micromata.genome.jpa.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

public class EmgrTxRollbackTest extends MgcTestCase
{

  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  /**
   * Call insert.
   *
   * @param name the name
   * @param toThrow the to throw
   * @return the genome jpa test table do
   */
  GenomeJpaTestTableDO callInsertRollback(String name, RuntimeException toThrow)
  {
    return emfac.tx().rollback().go((emgr) -> {
      GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
      d.setFirstName(name);
      emgr.insert(d);
      if (toThrow != null) {
        throw toThrow;
      }
      return d;
    });
  }

  /**
   * Test required.
   */
  @Test
  public void testRollback()
  {
    callInsertRollback("Roger", null);
  }

  /**
   * Test required.
   */
  @Test
  public void testRollbackNested()
  {
    GenomeJpaTestTableDO d = emfac.tx().go((emgr) -> {
      return callInsertRollback("testRollbackNested", null);
    });
    List<GenomeJpaTestTableDO> list = emfac.notx()
        .go((emgr) -> emgr.select(GenomeJpaTestTableDO.class,
            "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName", "firstName",
            "testRollbackNested"));
    Assert.assertEquals(0, list.size());
  }

}
