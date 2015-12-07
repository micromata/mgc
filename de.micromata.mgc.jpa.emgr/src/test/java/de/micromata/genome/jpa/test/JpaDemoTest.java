package de.micromata.genome.jpa.test;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.db.jpa.Clauses;
import de.micromata.mgc.db.jpa.CriteriaUpdate;

/**
 * Demonstration Tests for JPA
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaDemoTest extends MgcTestCase
{
  @Test
  public void testSelectComplex()
  {
    List<GenomeJpaTestTableDO> list;
    list = JpaTestEntMgrFactory.get().runWoTrans((emgr) -> {
      TypedQuery<GenomeJpaTestTableDO> query = emgr.createQuery(GenomeJpaTestTableDO.class,
          "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName");
      query.setParameter("firstName", "Roger");
      List<GenomeJpaTestTableDO> rlist = query.getResultList();
      emgr.detach(rlist);
      return rlist;
    });
    Assert.assertNotNull(list);
  }

  public void testSelectComplexWithDetached()
  {
    List<GenomeJpaTestTableDO> list;
    list = JpaTestEntMgrFactory.get().runWoTrans((emgr) -> {
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
    list = JpaTestEntMgrFactory.get().runWoTrans((emgr) -> {
      return emgr.selectDetached(GenomeJpaTestTableDO.class,
          "select e from " + GenomeJpaTestTableDO.class.getName() + " e where e.firstName = :firstName",
          "firstName", "Roger");
    });
    Assert.assertNotNull(list);
  }

  @Test
  public void testUpdateWithCriteria()
  {
    JpaTestEntMgrFactory.get().runInTrans((emgr) -> {
      CriteriaUpdate<GenomeJpaTestTableDO> cu = CriteriaUpdate.createUpdate(GenomeJpaTestTableDO.class)
          .addWhere(Clauses.equal("firstName", "Roger"))
          .set("firstName", "Roger Rene ");
      return emgr.update(cu);
    });
  }
}
