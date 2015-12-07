package de.micromata.mgc.db.jpa.multipc;

import javax.persistence.EntityManager;

import de.micromata.mgc.db.jpa.DefaultEmgr;
import de.micromata.mgc.db.jpa.EmgrFactory;

public class MultiPcSecondEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  static MultiPcSecondEmgrFactory INSTANCE;

  public static synchronized MultiPcSecondEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new MultiPcSecondEmgrFactory();
    return INSTANCE;
  }

  protected MultiPcSecondEmgrFactory()
  {
    //    super("de.micromata.mgc.db.jpa.multipc.second");
    super("de.micromata.mgc.db.jpa.multipc.auto");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager)
  {
    return new DefaultEmgr(entityManager, this);
  }

}
