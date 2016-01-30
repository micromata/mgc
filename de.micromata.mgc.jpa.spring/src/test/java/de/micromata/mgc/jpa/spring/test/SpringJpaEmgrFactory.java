package de.micromata.mgc.jpa.spring.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

public class SpringJpaEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  private static SpringJpaEmgrFactory INSTANCE;

  public static SpringJpaEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new SpringJpaEmgrFactory();
    return INSTANCE;
  }

  protected SpringJpaEmgrFactory()
  {
    super("de.micromata.mgc.jpa.spring.test");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entityManager, this, emgrTx);
  }

  public static EntityManagerFactory getMyEntityManagerFactory()
  {
    return get().getEntityManagerFactory();
  }
}
