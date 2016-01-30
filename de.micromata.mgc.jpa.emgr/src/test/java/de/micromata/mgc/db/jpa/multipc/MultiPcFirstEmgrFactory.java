package de.micromata.mgc.db.jpa.multipc;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

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
    super("de.micromata.genome.jpa.multipc.first");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entityManager, this, emgrTx);
  }

}
