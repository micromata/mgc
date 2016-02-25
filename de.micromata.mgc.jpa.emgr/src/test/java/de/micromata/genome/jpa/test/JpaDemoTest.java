package de.micromata.genome.jpa.test;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.Clauses;
import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * Demonstration Tests for JPA
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaDemoTest extends MgcTestCase
{
  static JpaTestEntMgrFactory emfac = JpaTestEntMgrFactory.get();

  @Test
  public void testSelectComplex()
  {
    emfac.tx().go(emgr -> {
      return emgr.createQuery("delete from " + GenomeJpaTestTableDO.class.getName() + " e").executeUpdate();
    });
    GenomeJpaTestTableDO table = new GenomeJpaTestTableDO();
    table.setFirstName("Roger");
    // insert the entity. 
    Long pk = emfac.tx().go((emgr) -> {
      return emgr.insertDetached(table);
    });
    Assert.assertNotNull(pk);
    // select the entity by pk
    GenomeJpaTestTableDO readed = emfac.notx().go((emgr) -> emgr.selectByPkAttached(GenomeJpaTestTableDO.class, pk));
    Assert.assertNotNull(readed);

    // select entities by criterias
    List<GenomeJpaTestTableDO> list = emfac.notx().go((emgr) -> {
      return emgr.selectDetached(GenomeJpaTestTableDO.class,
          "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName",
          "firstName", "Roger");
    });
    Assert.assertNotNull(list);
    Assert.assertEquals(1, list.size());
  }

  @Test
  public void testSelectComplexWithDetached()
  {
    emfac.tx().go(emgr -> {
      return emgr.createQuery("delete  from " + GenomeJpaTestTableDO.class.getName() + " e").executeUpdate();
    });
    List<GenomeJpaTestTableDO> list;
    list = emfac.runWoTrans((emgr) -> {
      TypedQuery<GenomeJpaTestTableDO> query = emgr.createQueryDetached(GenomeJpaTestTableDO.class,
          "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName");
      query.setParameter("firstName", "Roger");
      List<GenomeJpaTestTableDO> rlist = query.getResultList();
      return rlist;
    });
    Assert.assertNotNull(list);
  }

  @Test
  public void testSelectSimplified()
  {
    List<GenomeJpaTestTableDO> list;
    list = emfac.runWoTrans((emgr) -> {
      return emgr.selectDetached(GenomeJpaTestTableDO.class,
          "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName",
          "firstName", "Roger");
    });
    Assert.assertNotNull(list);
  }

  @Test
  public void testUpdateWithCriteria()
  {
    emfac.runInTrans((emgr) -> {
      CriteriaUpdate<GenomeJpaTestTableDO> cu = CriteriaUpdate.createUpdate(GenomeJpaTestTableDO.class)
          .addWhere(Clauses.equal("firstName", "Roger"))
          .set("firstName", "Roger Rene ");
      return emgr.update(cu);
    });
  }
}
