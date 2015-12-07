package de.micromata.mgc.db.jpa;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.mgc.db.jpa.api.IEmgr;
import de.micromata.mgc.db.jpa.api.eventhandler.BeforeCopyForUpdateUpdateCounterEventEventHandler;
import de.micromata.mgc.db.jpa.api.eventhandler.EmgrAfterCopyForUpdateEventEventHandler;
import de.micromata.mgc.db.jpa.api.eventhandler.InitCreatedStdRecordFieldsEventHandler;
import de.micromata.mgc.db.jpa.api.eventhandler.InitUpdateStdRecordFieldsEventHandler;
import de.micromata.mgc.db.jpa.api.eventhandler.UpdateStdRecordCriteriaUpdateEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrEventRegistry;
import de.micromata.mgc.db.jpa.trace.eventhandler.TracerEmgrCreateQueryFilterEventHandler;
import de.micromata.mgc.db.jpa.trace.eventhandler.TracerEmgrInsertDbRecordFilterEventHandler;
import de.micromata.mgc.db.jpa.trace.eventhandler.TracerEmgrMergeDbRecordFilterEventHandler;
import de.micromata.mgc.db.jpa.trace.eventhandler.TracerEmgrRemoveDbRecordFilterEventHandler;
import de.micromata.mgc.db.jpa.trace.eventhandler.TracerEmgrUpdateDbRecordFilterEventHandler;
import de.micromata.mgc.db.jpa.trace.eventhandler.TracerFindByPkFilterEventHandler;

/**
 * Factory class to create an Emgr instance. This class should be held by a singleton.
 * 
 * With the method runWoTrans/runInTrans an Emgr Instance will be provided.
 * 
 * @param <E> the element type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public abstract class EmgrFactory<E extends IEmgr<?>>
{
  public static enum TaFlags
  {
    /**
     * automatically participate existing, if parent is same ro or rw.
     */
    None(0x0),

    /**
     * Force readonly.
     */
    ReadOnly(0x1),

    /**
     * Creates new EntiManager
     */
    RequireNew(0x2);
    private final int flagMask;

    private TaFlags(int flags)
    {
      this.flagMask = flags;
    }

    public int getFlagMask()
    {
      return flagMask;
    }

    public boolean matches(int flags)
    {
      return (flags & flagMask) == flagMask;
    }
  }

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(EmgrFactory.class);

  /**
   * The unit name.
   */
  private String unitName;

  public String getUnitName()
  {
    return unitName;
  }

  /**
   * The entity manager factory.
   */
  public EntityManagerFactory entityManagerFactory;

  /**
   * If a trigger is defined to manage time stamps.
   */
  private boolean hasInsertTriggerForVersion = false;

  /**
   * If a trigger is defined to manage time stamps.
   */
  private boolean hasUpdateTriggerForVersion = false;

  /**
   * The event factory.
   */
  private final EmgrEventRegistry eventFactory = new EmgrEventRegistry();
  /**
   * The thread emgr.
   */
  private final ThreadLocal<E> threadEmgr = new ThreadLocal<E>();

  /**
   * Instantiates a new emgr factory.
   * 
   * @param unitName the unit name
   */
  protected EmgrFactory(String unitName)
  {
    this.unitName = unitName;
    entityManagerFactory = createEntityManagerFactory(unitName);
    registerEvents();
  }

  protected void registerEvents()
  {
    eventFactory.registerEvent(new InitCreatedStdRecordFieldsEventHandler());
    eventFactory.registerEvent(new InitUpdateStdRecordFieldsEventHandler());
    eventFactory.registerEvent(new UpdateStdRecordCriteriaUpdateEventHandler());
    eventFactory.registerEvent(new EmgrAfterCopyForUpdateEventEventHandler());
    eventFactory.registerEvent(new BeforeCopyForUpdateUpdateCounterEventEventHandler());

    // tracer wrapper
    eventFactory.registerEvent(new TracerFindByPkFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrRemoveDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrInsertDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrUpdateDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrMergeDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrCreateQueryFilterEventHandler());

  }

  /**
   * Creates a new specific Emgr .
   * 
   * @param entityManager the entity manager
   * @return the e
   */
  protected abstract E createEmgr(EntityManager entityManager);

  public EntityManagerFactory createEntityManagerFactory(String unitName)
  {
    LocalSettings ls = LocalSettings.get();
    Map<String, Object> lsMap = new HashMap<String, Object>();
    for (Map.Entry<String, String> me : ls.getMap().entrySet()) {
      lsMap.put(me.getKey(), me.getValue());
    }
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitName, lsMap);
    return emf;
  }

  /**
   * Creates the entity manager factory.
   * 
   * @param unitName the unit name
   * @return the entity manager factory
   */
  //  public EntityManagerFactory createEntityManagerFactory42(String unitName)
  //  {
  //    LocalSettings ls = LocalSettings.get();
  //    Map<String, Object> lsMap = new HashMap<String, Object>();
  //    for (Map.Entry<String, String> me : ls.getMap().entrySet()) {
  //      lsMap.put(me.getKey(), me.getValue());
  //    }
  //    ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
  //    lsMap.put(AvailableSettings.APP_CLASSLOADER, threadClassLoader);
  //    lsMap.put(AvailableSettings.RESOURCES_CLASSLOADER, threadClassLoader);
  //    lsMap.put(AvailableSettings.HIBERNATE_CLASSLOADER, threadClassLoader);
  //    lsMap.put(AvailableSettings.ENVIRONMENT_CLASSLOADER, threadClassLoader);
  //
  //    ThreadContextClassLoaderScope scope = new ThreadContextClassLoaderScope(getClass().getClassLoader());
  //    try {
  //      // following code is similar to  EntityManagerFactory emfac = Persistence.createEntityManagerFactory(unitName, lsMap);
  //      // but overwrites the classloader
  //      Ejb3Configuration cfg = new Ejb3Configuration();
  //      Ejb3Configuration configured = cfg.configure(unitName, lsMap);
  //      BootstrapServiceRegistryBuilder serviceRegistryBuilder = new BootstrapServiceRegistryBuilder();
  //      serviceRegistryBuilder.withApplicationClassLoader(threadClassLoader);
  //      serviceRegistryBuilder.withEnvironmentClassLoader(threadClassLoader);
  //      serviceRegistryBuilder.withHibernateClassLoader(threadClassLoader);
  //      serviceRegistryBuilder.withResourceClassLoader(threadClassLoader);
  //      EntityManagerFactory emfac = configured.buildEntityManagerFactory(serviceRegistryBuilder);
  //      /**
  //       * @logging
  //       * @reason persistence context was initialized.
  //       * @action nothing.
  //       */
  //      GLog.note(GenomeLogCategory.Jpa, "Initialized persistence context: " + unitName);
  //      return emfac;
  //    } finally {
  //      scope.restore();
  //    }
  //  }

  /**
   * Convert exception.
   * 
   * @param ex the ex
   * @return the runtime exception
   */
  public static RuntimeException convertException(RuntimeException ex)
  {
    if (ex instanceof QueryTimeoutException) {
      // this is a oracle/hibernate bug workouround.
      // hibernate think this is is a query timeout, but should a DataException
      if (ex.getCause() instanceof org.hibernate.QueryTimeoutException) {
        org.hibernate.QueryTimeoutException qto = (org.hibernate.QueryTimeoutException) ex.getCause();
        if (qto.getCause() instanceof SQLException) {
          SQLException sqlex = (SQLException) qto.getCause();
          // ORA-12899
          if (sqlex.getErrorCode() == 12899) {
            return new DataPersistenceException(ex.getMessage(), qto.getSQL(), sqlex.getSQLState(), ex);
          }
        }
      }
    }
    if (ex instanceof PersistenceException) {
      Throwable cause = ex.getCause();
      if (cause instanceof ConstraintViolationException) {
        ConstraintViolationException cve = (ConstraintViolationException) cause;
        cve.getMessage();
        String sql = cve.getSQL();
        return new ConstraintPersistenceException(cve.getMessage(), sql, cve.getSQLState(), cve.getConstraintName(),
            ex);
      } else if (cause instanceof DataException) {
        DataException dex = (DataException) cause;
        return new DataPersistenceException(ex.getMessage(), dex.getSQL(), dex.getSQLState(), ex);
      } else if (cause instanceof PropertyValueException) {
        if (StringUtils.startsWith(cause.getMessage(), "not-null ") == true) {
          return new NullableConstraintPersistenceException(ex.getMessage(), ex);
        }
      }
    }
    return ex;
  }

  /**
   * Run wo trans.
   * 
   * @param <R> the generic type
   * @param call the call
   * @return the r
   */
  public <R> R runWoTrans(EmgrCallable<R, E> call)
  {
    E emgr = threadEmgr.get();
    if (emgr != null) {
      if (log.isDebugEnabled() == true) {
        log.debug("em shortcut " + Thread.currentThread().getId());
      }
      return call.call(emgr);
    }
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    if (log.isDebugEnabled() == true) {
      log.debug("em created " + Thread.currentThread().getId());
    }
    try {
      emgr = createEmgr(entityManager);

      threadEmgr.set(emgr);
      return call.call(emgr);
    } finally {
      threadEmgr.set(null);
      entityManager.close();
      if (log.isDebugEnabled() == true) {
        log.debug("em closed " + Thread.currentThread().getId());
      }
    }
  }

  public <R> R runInTrans(final EmgrCallable<R, E> call)
  {
    return runInTrans(TaFlags.None.getFlagMask(), call);
  }

  public <R> R runRoTrans(final EmgrCallable<R, E> call)
  {
    return runInTrans(TaFlags.ReadOnly.getFlagMask(), call);
  }

  public <R> R runNewTrans(final EmgrCallable<R, E> call)
  {
    return runInTrans(TaFlags.RequireNew.getFlagMask(), call);
  }

  /**
   * Run in trans.
   * 
   * @param <R> the generic type
   * @param call the call
   * @return the r
   */
  public <R> R runInTrans(int flags, final EmgrCallable<R, E> call)
  {
    return runWoTrans(new EmgrCallable<R, E>()
    {

      @Override
      public R call(E emgr)
      {
        EntityTransaction tx = emgr.getEntityManager().getTransaction();
        if (tx.isActive() == true) {
          if (log.isInfoEnabled() == true) {
            log.info("nested tx shortcut " + Thread.currentThread().getId());
          }
          try {
            return call.call(emgr);
          } catch (RollbackException ex) { // NOSONAR Catching 'RuntimeException' is not allowed - Framework
            // nothing
            if (log.isDebugEnabled() == true) {
              log.debug("rollback tx:  in thread " + Thread.currentThread().getId() + "; " + ex.getMessage());
            }
            throw ex;
          } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
            if (log.isDebugEnabled() == true) {
              log.debug(
                  "rollback tx because ex:  in thread " + Thread.currentThread().getId() + "; " + ex.getMessage());
            }

            throw convertException(ex);
          }
        }
        try {
          if (log.isDebugEnabled() == true) {
            log.debug("begin tx:  in thread " + Thread.currentThread().getId());
          }
          tx.begin();
          R ret = call.call(emgr);

          if (log.isDebugEnabled() == true) {
            log.debug("commit tx:  in thread " + Thread.currentThread().getId());
          }
          emgr.getEntityManager().flush();
          tx.commit();
          return ret;
        } catch (RollbackException ex) {
          // nothing
          if (log.isDebugEnabled() == true) {
            log.debug("rollback tx:  in thread " + Thread.currentThread().getId() + "; " + ex.getMessage());
          }
          throw ex;
        } catch (RuntimeException ex) { // NOSONAR Catching 'RuntimeException' is not allowed - Framework
          if (log.isDebugEnabled() == true) {
            log.debug("rollback tx because ex:  in thread " + Thread.currentThread().getId() + "; " + ex.getMessage());
          }
          if (tx.isActive() == true) {
            tx.rollback();
          }
          throw convertException(ex);
        }
      }
    });

  }

  public EmgrEventRegistry getEventFactory()
  {
    return eventFactory;
  }

  public boolean isHasInsertTriggerForVersion()
  {
    return hasInsertTriggerForVersion;
  }

  public void setHasInsertTriggerForVersion(boolean hasInsertTriggerForVersion)
  {
    this.hasInsertTriggerForVersion = hasInsertTriggerForVersion;
  }

  public boolean isHasUpdateTriggerForVersion()
  {
    return hasUpdateTriggerForVersion;
  }

  public void setHasUpdateTriggerForVersion(boolean hasUpdateTriggerForVersion)
  {
    this.hasUpdateTriggerForVersion = hasUpdateTriggerForVersion;
  }

  public EntityManagerFactory getEntityManagerFactory()
  {
    return entityManagerFactory;
  }

  public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
  {
    this.entityManagerFactory = entityManagerFactory;
  }

  public ThreadLocal<E> getThreadEmgr()
  {
    return threadEmgr;
  }

  public void setUnitName(String unitName)
  {
    this.unitName = unitName;
  }

}
