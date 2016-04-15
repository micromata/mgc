package de.micromata.genome.db.jpa.genomecore.chronos;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaChronosEmgrFactory extends EmgrFactory<DefaultEmgr>
{

  /**
   * The instance.
   */
  static JpaChronosEmgrFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa genome core ent mgr factory
   */
  public static JpaChronosEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    synchronized (JpaChronosEmgrFactory.class) {
      if (INSTANCE != null) {
        return INSTANCE;
      }
      INSTANCE = new JpaChronosEmgrFactory();
      return INSTANCE;
    }
  }

  /**
   * Instantiates a new jpa genome core ent mgr factory.
   */
  protected JpaChronosEmgrFactory()
  {
    super("de.micromata.mgc.chronos.jpa");
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entityManager, this, emgrTx);
  }

}
