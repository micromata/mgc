package de.micromata.genome.db.jpa.normsearch.testentities;

import javax.persistence.EntityManager;

import org.junit.Ignore;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchServiceManager;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * emgr factory for tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class TestNSearchMgrFactory extends EmgrFactory<DefaultEmgr>
{

  static TestNSearchMgrFactory INSTANCE;

  public static synchronized TestNSearchMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new TestNSearchMgrFactory();
    return INSTANCE;
  }

  public TestNSearchMgrFactory()
  {
    super("de.micromata.genome.db.jpa.normsearch.testentities");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entitManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entitManager, this, emgrTx);
  }

  @Override
  protected void registerEvents()
  {
    super.registerEvents();
    NormalizedSearchServiceManager.get().getNormalizedSearchService().registerEmgrListener(this);
  }

}
