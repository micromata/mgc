package de.micromata.mgc.jpa.hibernatesearch;

import org.junit.Before;

import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.jpa.hibernatesearch.entities.MyEntityDO;

/**
 * Base test for search.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateSearchTestBase extends MgcTestCase
{
  @Before
  public void deleteAll()
  {
    HibernateSearchTestEmgrFactory emf = HibernateSearchTestEmgrFactory.get();

    emf.runInTrans((emgr) -> {
      return emgr.deleteFromQuery(MyEntityDO.class, "select e from " + MyEntityDO.class.getName() + " e");
    });
  }
}
