package de.micromata.genome.jpa.test;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.micromata.genome.jpa.StdRecordDO;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Just a playzone for raw jpa tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class RawJPATest
{
  public static EntityManagerFactory entityManagerFactory;

  @BeforeClass
  public static void createEntityManagerFactory()
  {
    if (entityManagerFactory != null) {
      return;
    }
    LocalSettings ls = LocalSettings.get();
    entityManagerFactory = Persistence.createEntityManagerFactory("de.micromata.genome.jpa.test", ls.getMap());

  }

  @AfterClass
  public static void destroyEntityManagerFactory()
  {
    if (entityManagerFactory == null) {
      return;
    }
    entityManagerFactory.close();
    entityManagerFactory = null;
  }

  public static void initForUpdate(StdRecordDO rec, Date now)
  {
    rec.setModifiedAt(now);
    rec.setModifiedBy("none");
    // rec.setUpdateCounter(rec.getUpdateCounter() + 1);
  }

  public static void initForCreate(StdRecordDO rec)
  {
    Date now = new Date();
    rec.setCreatedAt(now);
    rec.setCreatedBy("none");
    initForUpdate(rec, now);
  }

  public static long insertNoTrans(EntityManager ent)
  {
    GenomeJpaTestTableDO d = new GenomeJpaTestTableDO();
    initForCreate(d);
    d.setFirstName("Roger");
    ent.persist(d);
    return d.getPk();
  }

  public static long insert()
  {
    EntityManager ent = entityManagerFactory.createEntityManager();
    ent.getTransaction().begin();
    long ret = insertNoTrans(ent);
    ent.getTransaction().commit();
    ent.close();
    return ret;
  }

  public static void updateNoTrans(EntityManager ent, long pk)
  {
    GenomeJpaTestTableDO d = ent.find(GenomeJpaTestTableDO.class, pk);
    initForUpdate(d, new Date());
    d.setFirstName("Roger Rene");
    ent.persist(d);
  }

  public static void update(long pk)
  {
    EntityManager ent = entityManagerFactory.createEntityManager();
    ent.getTransaction().begin();
    updateNoTrans(ent, pk);
    ent.getTransaction().commit();
    ent.close();
  }

  public static void deleteNoTrans(EntityManager ent, long pk)
  {
    GenomeJpaTestTableDO d = ent.find(GenomeJpaTestTableDO.class, pk);
    ent.remove(d);
  }

  public static void delete(long pk)
  {
    EntityManager ent = entityManagerFactory.createEntityManager();
    ent.getTransaction().begin();
    deleteNoTrans(ent, pk);
    ent.getTransaction().commit();
    ent.close();
  }

  @Test
  public void testRawTransactions()
  {
    long pk = insert();
    update(pk);
    delete(pk);
  }
}
