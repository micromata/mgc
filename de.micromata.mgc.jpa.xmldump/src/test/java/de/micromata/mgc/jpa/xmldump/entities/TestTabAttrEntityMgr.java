package de.micromata.mgc.jpa.xmldump.entities;

import javax.persistence.EntityManager;

import org.junit.Ignore;

import de.micromata.genome.jpa.Emgr;

/**
 * Emgr for tests
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class TestTabAttrEntityMgr extends Emgr<TestTabAttrEntityMgr>
{

  /**
   * @param entityManager
   */
  public TestTabAttrEntityMgr(EntityManager entityManager, TestTabAttrEntityMgrFactory emgrFactory)
  {
    super(entityManager, emgrFactory);
  }

}
