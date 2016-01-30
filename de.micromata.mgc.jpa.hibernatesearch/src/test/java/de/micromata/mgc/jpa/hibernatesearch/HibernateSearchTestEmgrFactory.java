package de.micromata.mgc.jpa.hibernatesearch;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.EmgrTx;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;
import de.micromata.mgc.jpa.hibernatesearch.impl.DefaultSearchEmgr;

public class HibernateSearchTestEmgrFactory extends SearchEmgrFactory<DefaultSearchEmgr>
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
  protected DefaultSearchEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultSearchEmgr> emgrTx)
  {
    return new DefaultSearchEmgr(entityManager, this, emgrTx);
  }
}
