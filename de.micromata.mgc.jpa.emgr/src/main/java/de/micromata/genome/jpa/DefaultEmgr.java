package de.micromata.genome.jpa;

import javax.persistence.EntityManager;

/**
 * Can be used if no extended functions are needed in Emgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class DefaultEmgr extends Emgr<DefaultEmgr>
{

  /**
   * Instantiates a new default emgr.
   *
   * @param entityManager the entity manager
   * @param emgrFactory the emgr factory
   */
  public DefaultEmgr(EntityManager entityManager, EmgrFactory<DefaultEmgr> emgrFactory, EmgrTx<DefaultEmgr> emgrTx)
  {
    super(entityManager, emgrFactory, emgrTx);
  }

}
