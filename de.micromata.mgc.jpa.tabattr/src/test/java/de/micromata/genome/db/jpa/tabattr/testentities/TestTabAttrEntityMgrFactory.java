package de.micromata.genome.db.jpa.tabattr.testentities;

import javax.persistence.EntityManager;

import org.junit.Ignore;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * emgr factory for tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class TestTabAttrEntityMgrFactory extends EmgrFactory<TestTabAttrEntityMgr>
{

  static TestTabAttrEntityMgrFactory INSTANCE;

  public static synchronized TestTabAttrEntityMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new TestTabAttrEntityMgrFactory();
    return INSTANCE;
  }

  public TestTabAttrEntityMgrFactory()
  {
    super("de.micromata.genome.jpa.tabattr.testentities");
  }

  @Override
  protected TestTabAttrEntityMgr createEmgr(EntityManager entitManager, EmgrTx<TestTabAttrEntityMgr> emgrTx)
  {
    return new TestTabAttrEntityMgr(entitManager, this, emgrTx);
  }

}
