package de.micromata.mgc.jpa.hibernatesearch;

import org.hibernate.search.jpa.FullTextEntityManager;
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
      emgr.deleteFromQuery(MyEntityDO.class, "select e from " + MyEntityDO.class.getName() + " e");
      FullTextEntityManager ftem = emgr.getFullTextEntityManager();
      ftem.purgeAll(MyEntityDO.class);
      ftem.flushToIndexes();
      return null;

    });
  }
}
