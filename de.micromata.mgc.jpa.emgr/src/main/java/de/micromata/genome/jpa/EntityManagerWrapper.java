package de.micromata.genome.jpa;

import javax.persistence.EntityManager;

/**
 * The Class EntityManagerWrapper.
 *
 * @author Sebastian Hardt (s.hardt@micromata.de) Date: 10/14/13 Time: 4:13 PM
 */
public class EntityManagerWrapper
{

  /**
   * The entity manager.
   */
  final private EntityManager entityManager;

  /**
   * The in transaction.
   */
  private boolean inTransaction = false;

  /**
   * Instantiates a new entity manager wrapper.
   *
   * @param entityManager the entity manager
   */
  public EntityManagerWrapper(EntityManager entityManager)
  {
    this.entityManager = entityManager;
  }

  public boolean isInTransaction()
  {
    return inTransaction;
  }

  public void setInTransaction(boolean inTransaction)
  {
    this.inTransaction = inTransaction;
  }

  public EntityManager getEntityManager()
  {
    return entityManager;
  }
}
