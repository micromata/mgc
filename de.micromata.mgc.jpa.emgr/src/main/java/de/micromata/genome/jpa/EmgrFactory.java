package de.micromata.genome.jpa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import de.micromata.genome.jpa.events.EmgrEventRegistry;
import de.micromata.genome.jpa.events.impl.BeforeCopyForUpdateUpdateCounterEventEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrAfterCopyForUpdateEventEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrMarkDeletedCriteriaUpdateFilterEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrMarkUndeletedCriteriaUpdateFilterEventHandler;
import de.micromata.genome.jpa.events.impl.InitCreatedStdRecordFieldsEventHandler;
import de.micromata.genome.jpa.events.impl.InitUpdateStdRecordFieldsEventHandler;
import de.micromata.genome.jpa.events.impl.UpdateStdRecordCriteriaUpdateEventHandler;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.jpa.metainf.MetaInfoUtils;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrCreateQueryFilterEventHandler;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrInsertDbRecordFilterEventHandler;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrMergeDbRecordFilterEventHandler;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrRemoveDbRecordFilterEventHandler;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrUpdateDbRecordFilterEventHandler;
import de.micromata.genome.jpa.trace.eventhandler.TracerFindByPkFilterEventHandler;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Factory class to create an Emgr instance. This class should be held by a singleton.
 * 
 * With the method runWoTrans/runInTrans an Emgr Instance will be provided.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public abstract class EmgrFactory<E extends IEmgr<?>>
{

  /**
   * The Enum TaFlags.
   */
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
     * Creates new EntiManager.
     */
    RequireNew(0x2);

    /**
     * The flag mask.
     */
    private final int flagMask;

    /**
     * Instantiates a new ta flags.
     *
     * @param flags the flags
     */
    private TaFlags(int flags)
    {
      this.flagMask = flags;
    }

    /**
     * Gets the flag mask.
     *
     * @return the flag mask
     */
    public int getFlagMask()
    {
      return flagMask;
    }

    /**
     * Matches.
     *
     * @param flags the flags
     * @return true, if successful
     */
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

  /**
   * The metadata repository.
   */
  private JpaMetadataRepostory metadataRepository;

  /**
   * Gets the unit name.
   *
   * @return the unit name
   */
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
  protected EmgrEventRegistry eventFactory = new EmgrEventRegistry();
  /**
   * The thread emgr.
   */
  private final ThreadLocal<E> threadEmgr = new ThreadLocal<E>();

  /**
   * Registered copied to type.
   */
  private Map<Class<?>, List<Class<? extends EntityCopier>>> registeredCopier = new HashMap<>();

  /**
   * Instantiates a new emgr factory.
   * 
   * @param unitName the unit name
   */
  protected EmgrFactory(String unitName)
  {
    this.unitName = unitName;
    entityManagerFactory = createEntityManagerFactory(unitName);
    initMetadata();
    registerEvents();
  }

  /**
   * Inits the metadata.
   */
  protected void initMetadata()
  {
    this.metadataRepository = MetaInfoUtils.fillEntityMetadata(this);
  }

  /**
   * Register events.
   */
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
    eventFactory.registerEvent(new EmgrMarkUndeletedCriteriaUpdateFilterEventHandler());
    eventFactory.registerEvent(new EmgrMarkDeletedCriteriaUpdateFilterEventHandler());

  }

  /**
   * Creates a new specific Emgr .
   * 
   * @param entityManager the entity manager
   * @return the e
   */
  protected abstract E createEmgr(EntityManager entityManager);

  /**
   * Creates a new Emgr object.
   *
   * @param unitName the unit name
   * @return the entity manager factory
   */
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

  /**
   * Run in trans.
   *
   * @param <R> the generic type
   * @param call the call
   * @return the r
   */
  public <R> R runInTrans(final EmgrCallable<R, E> call)
  {
    return runInTrans(TaFlags.None.getFlagMask(), call);
  }

  /**
   * Run ro trans.
   *
   * @param <R> the generic type
   * @param call the call
   * @return the r
   */
  public <R> R runRoTrans(final EmgrCallable<R, E> call)
  {
    return runInTrans(TaFlags.ReadOnly.getFlagMask(), call);
  }

  /**
   * Run new trans.
   *
   * @param <R> the generic type
   * @param call the call
   * @return the r
   */
  public <R> R runNewTrans(final EmgrCallable<R, E> call)
  {
    return runInTrans(TaFlags.RequireNew.getFlagMask(), call);
  }

  /**
   * Run in trans.
   *
   * @param <R> the generic type
   * @param flags the flags
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

  /**
   * Gets the event factory.
   *
   * @return the event factory
   */
  public EmgrEventRegistry getEventFactory()
  {
    return eventFactory;
  }

  /**
   * Get the user id for createdBy/modifiedBy. Should not be longer than 32 charachter.
   *
   * @return the current user id
   */
  public String getCurrentUserId()
  {
    String user = LoggingServiceManager.get().getLoggingContextService().getCurrentUserName();
    return user;
  }

  /**
   * Get the current time stamp.
   *
   * @return the now
   */
  public Date getNow()
  {
    return new Date();
  }

  /**
   * Checks if is checks for insert trigger for version.
   *
   * @return true, if is checks for insert trigger for version
   */
  public boolean isHasInsertTriggerForVersion()
  {
    return hasInsertTriggerForVersion;
  }

  /**
   * Sets the checks for insert trigger for version.
   *
   * @param hasInsertTriggerForVersion the new checks for insert trigger for version
   */
  public void setHasInsertTriggerForVersion(boolean hasInsertTriggerForVersion)
  {
    this.hasInsertTriggerForVersion = hasInsertTriggerForVersion;
  }

  /**
   * Checks if is checks for update trigger for version.
   *
   * @return true, if is checks for update trigger for version
   */
  public boolean isHasUpdateTriggerForVersion()
  {
    return hasUpdateTriggerForVersion;
  }

  /**
   * Sets the checks for update trigger for version.
   *
   * @param hasUpdateTriggerForVersion the new checks for update trigger for version
   */
  public void setHasUpdateTriggerForVersion(boolean hasUpdateTriggerForVersion)
  {
    this.hasUpdateTriggerForVersion = hasUpdateTriggerForVersion;
  }

  /**
   * Gets the entity manager factory.
   *
   * @return the entity manager factory
   */
  public EntityManagerFactory getEntityManagerFactory()
  {
    return entityManagerFactory;
  }

  /**
   * Sets the entity manager factory.
   *
   * @param entityManagerFactory the new entity manager factory
   */
  public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
  {
    this.entityManagerFactory = entityManagerFactory;
  }

  /**
   * Gets the thread emgr.
   *
   * @return the thread emgr
   */
  public ThreadLocal<E> getThreadEmgr()
  {
    return threadEmgr;
  }

  /**
   * Sets the unit name.
   *
   * @param unitName the new unit name
   */
  public void setUnitName(String unitName)
  {
    this.unitName = unitName;
  }

  /**
   * Registered copier.
   *
   * @param beanClass the bean class
   * @param copierClass the copier class
   */
  public void registeredCopier(Class<?> beanClass, Class<? extends EntityCopier> copierClass)
  {
    registeredCopier.putIfAbsent(beanClass, new ArrayList<>());
    registeredCopier.get(beanClass).add(copierClass);
  }

  /**
   * Find copier for bean.
   *
   * @param beanClass the bean class
   * @return the class<? extends entity copier>
   */
  public List<Class<? extends EntityCopier>> getRegisteredCopierForBean(Class<?> beanClass)
  {
    ArrayList<Class<? extends EntityCopier>> ret = new ArrayList<>();
    for (Class<?> bc : registeredCopier.keySet()) {
      if (bc.isAssignableFrom(beanClass) == true) {
        ret.addAll(registeredCopier.get(bc));
      }
    }
    return ret;
  }

  /**
   * Gets the metadata repository.
   *
   * @return the metadata repository
   */
  public JpaMetadataRepostory getMetadataRepository()
  {
    return metadataRepository;
  }
}
