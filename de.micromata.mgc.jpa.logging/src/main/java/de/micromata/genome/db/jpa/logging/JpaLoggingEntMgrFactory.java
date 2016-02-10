/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome 
//
// Author    r.kommer.extern@micromata.de
// Created   18.02.2013
// Copyright Micromata 2013
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.db.jpa.logging;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * Entity Manager for AttrBaseDAOImpl.
 *
 * @author roger
 */
public class JpaLoggingEntMgrFactory extends EmgrFactory<DefaultEmgr>
{

  /**
   * The instance.
   */
  static JpaLoggingEntMgrFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa logging ent mgr factory
   */
  public static synchronized JpaLoggingEntMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new JpaLoggingEntMgrFactory();
    return INSTANCE;
  }

  /**
   * Instantiates a new jpa logging ent mgr factory.
   */
  protected JpaLoggingEntMgrFactory()
  {
    super("de.micromata.genome.db.jpa.logging");

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
