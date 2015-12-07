package de.micromata.mgc.db.jpa.multipc;

import javax.persistence.EntityManager;

import de.micromata.mgc.db.jpa.DefaultEmgr;
import de.micromata.mgc.db.jpa.EmgrFactory;

public class MultiPcFirstEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  static MultiPcFirstEmgrFactory INSTANCE;

  public static synchronized MultiPcFirstEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new MultiPcFirstEmgrFactory();
    return INSTANCE;
  }

  protected MultiPcFirstEmgrFactory()
  {
    super("de.micromata.mgc.db.jpa.multipc.first");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager)
  {
    return new DefaultEmgr(entityManager, this);
  }

}
