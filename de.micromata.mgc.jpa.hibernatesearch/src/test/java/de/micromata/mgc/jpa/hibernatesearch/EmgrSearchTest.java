package de.micromata.mgc.jpa.hibernatesearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * Basic search test.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrSearchTest extends HibernateSearchTestBase
{
  @Test
  public void testSearch()
  {
    HibernateSearchTestEmgrFactory emf = HibernateSearchTestEmgrFactory.get();

    MyEntityDO net = new MyEntityDO();
    net.setName("Bla");
    net.setLoginName("Blub");
    net.setNotSearchable("NOTFOUND");
    emf.runInTrans((emgr) -> emgr.insert(net));

    List<MyEntityDO> found = emf.runWoTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("bla", MyEntityDO.class, "name");
      return lret;
    });

    Assert.assertEquals(1, found.size());

    found = emf.runWoTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("blub", MyEntityDO.class, "name");
      return lret;
    });

    Assert.assertEquals(0, found.size());

    found = emf.runWoTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("blub", MyEntityDO.class);
      return lret;
    });
    Assert.assertEquals(1, found.size());

    found = emf.runWoTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("bla", MyEntityDO.class);
      return lret;
    });
    Assert.assertEquals(1, found.size());

    found = emf.runWoTrans((emgr) -> {
      List<MyEntityDO> lret = emgr.searchAttached("NOTFOUND", MyEntityDO.class);
      return lret;
    });
    Assert.assertEquals(0, found.size());
  }
}
