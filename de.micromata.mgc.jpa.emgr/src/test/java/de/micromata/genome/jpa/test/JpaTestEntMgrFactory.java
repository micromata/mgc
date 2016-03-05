package de.micromata.genome.jpa.test;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * Just a manager factory for testing.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class JpaTestEntMgrFactory extends EmgrFactory<JpaTestEntMgr>
{
  // An EmgrFactory has to be singlton
  static JpaTestEntMgrFactory INSTANCE;

  // because this method is synchronized hold the factory in you service
  public static synchronized JpaTestEntMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new JpaTestEntMgrFactory();
    return INSTANCE;
  }

  protected JpaTestEntMgrFactory()
  {
    // the name of the persistence context
    super("de.micromata.genome.jpa.test");
  }

  /**
   * Create an IEmgr instance. If you don't want to create your own type, you can also use DefaultEmgr {@inheritDoc}
   *
   */
  @Override
  protected JpaTestEntMgr createEmgr(EntityManager entitManager, EmgrTx<JpaTestEntMgr> emgrTx)
  {
    return new JpaTestEntMgr(entitManager, this, emgrTx);
  }
}
