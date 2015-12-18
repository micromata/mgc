package de.micromata.mgc.jpa.hibernatesearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyNestedEntity;

public class HibernateSearchComplexTest extends MgcTestCase
{
  @Test
  public void testComplex()
  {
    try {
      HibernateSearchTestEmgrFactory emf = HibernateSearchTestEmgrFactory.get();

      MyEntityDO ent = new MyEntityDO();
      ent.setName("Roger Kommer THISNAME");
      MyNestedEntity oent = new MyNestedEntity();
      oent.setName("Parent Nested PANESTON");
      oent.setComment("Parent Comment PANESTOC");
      emf.runInTrans((emgr) -> emgr.insertAttached(oent));
      ent.setNested(oent);
      MyNestedEntity fent = new MyNestedEntity();
      fent.setName("FirstNested XFIRSTNESTNAME");
      fent.setComment("First Nested Comment XFIRSTNESTCOMMENT");
      //      emf.runInTrans((emgr) -> emgr.insertAttached(fent));

      ent.getAssignedNested().add(fent);
      MyNestedEntity sent = new MyNestedEntity();
      sent.setName("SecondNested YSECONDNESTNAME");
      sent.setComment("Second Nested Comment YSECONDNESTCOMMENT");
      ent.getAssignedNested().add(sent);
      //      emf.runInTrans((emgr) -> emgr.insertAttached(sent));

      emf.runInTrans((emgr) -> {
        emgr.insert(ent);
        return null;
      });
      emf.runWoTrans((emgr) -> {
        // Search on all fields

        List<MyEntityDO> sa = emgr.searchAttached("THISNAME", MyEntityDO.class);
        Assert.assertEquals(1, sa.size());
        Assert.assertEquals(0, emgr.searchAttached("DOESNOTEXISTS", MyEntityDO.class).size());
        // search 1:1 with includePaths declaration
        Assert.assertEquals(1, emgr.searchAttached("PANESTON", MyEntityDO.class).size());
        // not in includePaths
        Assert.assertEquals(0, emgr.searchAttached("PANESTOC", MyEntityDO.class).size());

        Assert.assertEquals(1, emgr.searchAttached("XFIRSTNESTCOMMENT", MyEntityDO.class).size());
        Assert.assertEquals(1, emgr.searchAttached("YSECONDNESTCOMMENT", MyEntityDO.class).size());

        // Column searches
        Assert.assertEquals(1, emgr.searchAttached("THISNAME", MyEntityDO.class, "name").size());
        Assert.assertEquals(0, emgr.searchAttached("THISNAME", MyEntityDO.class, "loginName").size());

        Assert.assertEquals(1, emgr.searchAttached("PANESTON", MyEntityDO.class, "nested.name").size());
        Assert.assertEquals(1,
            emgr.searchAttached("YSECONDNESTCOMMENT", MyEntityDO.class, "assignedNested.comment").size());
        Assert.assertEquals(1,
            emgr.searchAttached("XFIRSTNESTCOMMENT", MyEntityDO.class, "assignedNested.comment").size());
        Assert.assertEquals(0,
            emgr.searchAttached("XFIRSTNESTCOMMENT", MyEntityDO.class, "assignedNested.name").size());
        return null;
      });
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
