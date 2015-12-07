package de.micromata.genome.db.jpa.history.api;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;

/**
 * A factory for creating JpaEntityManager objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaHistoryEntityManagerFactory extends EmgrFactory<DefaultEmgr>
{

  /**
   * The instance.
   */
  static JpaHistoryEntityManagerFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa history entity manager factory
   */
  public static synchronized JpaHistoryEntityManagerFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new JpaHistoryEntityManagerFactory();
    return INSTANCE;
  }

  /**
   * Instantiates a new jpa entity manager factory.
   */
  public JpaHistoryEntityManagerFactory()
  {
    super("de.micromata.genome.db.jpa.history");
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager)
  {
    return new DefaultEmgr(entityManager, this);
  }

}
