package de.micromata.mgc.jpa.hibernatesearch;

import javax.persistence.EntityManager;

import de.micromata.mgc.db.jpa.DefaultEmgr;
import de.micromata.mgc.db.jpa.EmgrFactory;

public class HibernateSearchTestEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  static HibernateSearchTestEmgrFactory INSTANCE;

  public static synchronized HibernateSearchTestEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new HibernateSearchTestEmgrFactory();
    return INSTANCE;
  }

  protected HibernateSearchTestEmgrFactory()
  {
    super("de.micromata.mgc.jpa.hibernatesearch.test");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager)
  {
    return new DefaultEmgr(entityManager, this);
  }
}
