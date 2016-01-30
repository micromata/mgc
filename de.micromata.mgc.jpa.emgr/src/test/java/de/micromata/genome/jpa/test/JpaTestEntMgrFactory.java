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

  static JpaTestEntMgrFactory INSTANCE;

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
    super("de.micromata.genome.jpa.test");
  }

  @Override
  protected JpaTestEntMgr createEmgr(EntityManager entitManager, EmgrTx<JpaTestEntMgr> emgrTx)
  {
    return new JpaTestEntMgr(entitManager, this, emgrTx);
  }

}
