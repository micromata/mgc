package de.micromata.genome.db.jpa.history.test;

import javax.persistence.EntityManager;

import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * A factory for creating HistoryTestEmgr objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryTestEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  private static HistoryTestEmgrFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa genome core ent mgr factory
   */
  public static synchronized HistoryTestEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new HistoryTestEmgrFactory();
    return INSTANCE;
  }

  /**
   * Instantiates a new history test emgr factory.
   */
  public HistoryTestEmgrFactory()
  {
    super("de.micromata.genome.db.jpa.history.test");
  }

  @Override
  protected void registerEvents()
  {
    super.registerEvents();
    HistoryServiceManager.get().getHistoryService().registerEmgrListener(this);
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
