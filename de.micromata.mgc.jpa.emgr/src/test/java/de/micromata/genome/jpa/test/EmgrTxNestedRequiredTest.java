package de.micromata.genome.jpa.test;

import java.util.List;

import javax.persistence.TransactionRequiredException;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrTxNestedRequiredTest extends MgcTestCase
{
  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  @Test
  public void testRequiredFail()
  {
    try {
      callInsert("Roger");
      Assert.fail("Missing required exception TransactionRequiredException");
    } catch (TransactionRequiredException ex) {
      // expected
    }
  }

  GenomeJpaTestTableDO callInsert(String name)
  {
    return emfac.tx().requires().go((emgr) -> {
      GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
      d.setFirstName(name);
      emgr.insert(d);
      return d;
    });
  }

  @Test
  public void testRequired()
  {
    emfac.tx().go((emgr) -> {
      return callInsert("Roger");
    });
  }

  @Test
  public void testRequiredRollback()
  {
    GenomeJpaTestTableDO d = emfac.tx().rollback().go((emgr) -> {
      return callInsert("testRequiredRollback");
    });

    List<GenomeJpaTestTableDO> list = emfac.notx().go((emgr) -> emgr.select(GenomeJpaTestTableDO.class,
        "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName", "firstName",
        "testRequiredRollback"));
    Assert.assertEquals(0, list.size());
  }
}
