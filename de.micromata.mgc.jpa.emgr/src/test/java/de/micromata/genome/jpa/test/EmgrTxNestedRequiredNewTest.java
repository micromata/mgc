package de.micromata.genome.jpa.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

/**
 * The Class EmgrTxNestedRequiredNewTest.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrTxNestedRequiredNewTest extends MgcTestCase
{

  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  /**
   * Call insert.
   *
   * @param name the name
   * @param toThrow the to throw
   * @return the genome jpa test table do
   */
  GenomeJpaTestTableDO callInsert(String name, RuntimeException toThrow)
  {
    return emfac.tx().requiresNew().go((emgr) -> {
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
  public void testRequired()
  {
    emfac.tx().go((emgr) -> {
      return callInsert("Roger", null);
    });
  }

  /**
   * Test required parent ro.
   */
  @Test
  public void testRequiredParentRo()
  {
    emfac.tx().readOnly().go((emgr) -> {
      return null;
    });
    GenomeJpaTestTableDO d = emfac.tx().readOnly().go((emgr) -> {
      return callInsert("testRequiredParentRo", null);
    });
    List<GenomeJpaTestTableDO> list = emfac.notx()
        .go((emgr) -> emgr.select(GenomeJpaTestTableDO.class,
            "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName", "firstName",
            "testRequiredParentRo"));
    Assert.assertEquals(1, list.size());
  }

  /**
   * Test required parent rollback.
   */
  @Test
  public void testRequiredParentRollback()
  {
    GenomeJpaTestTableDO d = emfac.tx().rollback().go((emgr) -> {
      return callInsert("testRequiredParentRollback", null);
    });

    List<GenomeJpaTestTableDO> list = emfac.notx()
        .go((emgr) -> emgr.select(GenomeJpaTestTableDO.class,
            "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName", "firstName",
            "testRequiredParentRollback"));
    Assert.assertEquals(1, list.size());
  }

}
