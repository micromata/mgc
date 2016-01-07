package de.micromata.mgc.jpa.hibernatesearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyNestedEntity;

public class HibernateSearchComplexTest extends HibernateSearchTestBase
{
  @Test
  public void testComplex()
  {
    try {
      HibernateSearchTestEmgrFactory emf = HibernateSearchTestEmgrFactory.get();

      MyEntityDO ent = new MyEntityDO();
      ent.setName("Roger Kommer THISNAME");
      //      emf.runInTrans((emgr) -> emgr.insertAttached(ent));

      MyNestedEntity oent = new MyNestedEntity();
      oent.setNestedName("Parent Nested PANESTON");
      oent.setNestedComment("Parent Comment PANESTOC");
      emf.runInTrans((emgr) -> emgr.insertAttached(oent));
      oent.setParent(ent);
      ent.setNested(oent);
      MyNestedEntity fent = new MyNestedEntity();
      fent.setNestedName("FirstNested XFIRSTNESTNAME");
      fent.setNestedComment("First Nested Comment XFIRSTNESTCOMMENT");
      //      emf.runInTrans((emgr) -> emgr.insertAttached(fent));
      fent.setParent(ent);
      ent.getAssignedNested().add(fent);
      MyNestedEntity sent = new MyNestedEntity();
      sent.setNestedName("SecondNested YSECONDNESTNAME");
      sent.setNestedComment("Second Nested Comment YSECONDNESTCOMMENT");
      sent.setParent(ent);
      ent.getAssignedNested().add(sent);
      //      emf.runInTrans((emgr) -> emgr.insertAttached(sent));

      emf.runInTrans((emgr) -> {
        emgr.insertAttached(ent);
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

        Assert.assertEquals(1, emgr.searchAttached("PANESTON", MyEntityDO.class, "nested.nestedName").size());
        Assert.assertEquals(1,
            emgr.searchAttached("YSECONDNESTCOMMENT", MyEntityDO.class, "assignedNested.nestedComment").size());
        Assert.assertEquals(1,
            emgr.searchAttached("XFIRSTNESTCOMMENT", MyEntityDO.class, "assignedNested.nestedComment").size());
        Assert.assertEquals(0,
            emgr.searchAttached("XFIRSTNESTCOMMENT", MyEntityDO.class, "assignedNested.nestedName").size());
        return null;
      });
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
