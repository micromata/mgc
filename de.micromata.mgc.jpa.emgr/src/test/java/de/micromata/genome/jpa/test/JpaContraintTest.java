package de.micromata.genome.jpa.test;

import javax.persistence.NoResultException;

import org.junit.Test;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.db.jpa.ConstraintPersistenceException;
import de.micromata.mgc.db.jpa.DataPersistenceException;
import de.micromata.mgc.db.jpa.EmgrCallable;

/**
 * Tests constraints
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */

public class JpaContraintTest extends MgcTestCase
{
  @Test
  public void testNullable()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    try {
      mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
      {

        @Override
        public Void call(JpaTestEntMgr emgr)
        {
          GenomeJpaTestTableDO ntable = new GenomeJpaTestTableDO();
          ntable.setFirstName(null);
          emgr.insert(ntable);
          throw new RuntimeException("Should not reach here");
        }
      });
    } catch (ConstraintPersistenceException ex) {
      GLog.note(GenomeLogCategory.Jpa, "Expected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      GLog.error(GenomeLogCategory.Jpa, "UnExpected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
      throw ex;
    }
  }

  @Test
  public void testUnique()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    try {
      mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
      {

        @Override
        public Void call(JpaTestEntMgr emgr)
        {
          String firstName = "UT_UNIQNAME";
          String sql = "delete from " + GenomeJpaTest2TableDO.class.getSimpleName() + " where firstName = :firstName";
          int count = emgr.createQuery(sql, "firstName", firstName).executeUpdate();
          GenomeJpaTest2TableDO t1 = new GenomeJpaTest2TableDO();
          t1.setFirstName(firstName);
          emgr.insert(t1);

          GenomeJpaTest2TableDO t2 = new GenomeJpaTest2TableDO();
          t2.setFirstName(firstName);
          emgr.insert(t2);
          throw new RuntimeException("Should not reach here");
        }
      });
    } catch (ConstraintPersistenceException ex) {
      GLog.note(GenomeLogCategory.Jpa, "Expected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      GLog.error(GenomeLogCategory.Jpa, "UnExpected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
      throw ex;
    }
  }

  @Test
  public void testTooLarge()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    try {
      mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
      {

        @Override
        public Void call(JpaTestEntMgr emgr)
        {
          GenomeJpaTestTableDO ntable = new GenomeJpaTestTableDO();
          ntable.setFirstName("This is a text, which is too large and has 51 chars");
          emgr.insert(ntable);
          throw new RuntimeException("Should not reach here");
        }
      });
    } catch (DataPersistenceException ex) {
      GLog.note(GenomeLogCategory.Jpa, "Expected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      GLog.error(GenomeLogCategory.Jpa, "UnExpected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
      throw ex;
    }
  }

  @Test
  public void testNotTooLarge()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    try {
      mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
      {

        @Override
        public Void call(JpaTestEntMgr emgr)
        {
          GenomeJpaTestTableDO ntable = new GenomeJpaTestTableDO();
          ntable.setFirstName("Thís ís á text, whích ís täÄ largè and has 50 char");
          emgr.insert(ntable);
          return null;
        }
      });
    } catch (ConstraintPersistenceException ex) {
      GLog.note(GenomeLogCategory.Jpa, "Expected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
      throw ex;
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      GLog.error(GenomeLogCategory.Jpa, "UnExpected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
      throw ex;
    }
  }

  @Test
  public void testHandleSelectExInTrans()
  {
    final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
    try {
      mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
      {

        @Override
        public Void call(JpaTestEntMgr emgr)
        {
          String firstName = "this_value_should_exists";
          String query = "select n from " + GenomeJpaTestTableDO.class.getSimpleName()
              + " n where firstName = :firstName";
          try {
            GenomeJpaTestTableDO instance = emgr.createQuery(GenomeJpaTestTableDO.class, query, "firstName", firstName)
                .getSingleResult();
          } catch (NoResultException ex) {
            GLog.note(GenomeLogCategory.Jpa, "Expected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
          }
          GenomeJpaTestTableDO ntable = new GenomeJpaTestTableDO();
          ntable.setFirstName("But this may exists");
          emgr.insert(ntable);
          return null;
        }
      });

    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      GLog.error(GenomeLogCategory.Jpa, "UnExpected ex: " + ex.getMessage(), new LogExceptionAttribute(ex));
      throw ex;
    }
  }
}
