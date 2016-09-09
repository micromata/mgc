//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.jpa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import de.micromata.genome.jpa.events.EmgrEventRegistry;
import de.micromata.genome.jpa.events.impl.BeforeCopyForUpdateUpdateCounterEventEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrAfterCopyForUpdateEventEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrBeforeCriteriaUpdateEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrMarkDeletedCriteriaUpdateFilterEventHandler;
import de.micromata.genome.jpa.events.impl.EmgrMarkUndeletedCriteriaUpdateFilterEventHandler;
import de.micromata.genome.jpa.events.impl.InitCreatedStdRecordFieldsEventHandler;
import de.micromata.genome.jpa.events.impl.InitUpdateStdRecordFieldsEventHandler;
import de.micromata.genome.jpa.events.impl.UpdateStdRecordCriteriaUpdateEventHandler;
import de.micromata.genome.jpa.impl.JpaSchemaServiceImpl;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.jpa.metainf.MetaInfoUtils;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrCreateQueryFilterEventHandler;
import de.micromata.genome.jpa.trace.eventhandler.TracerEmgrCreateTypedQueryFilterEventHandler;
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
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(EmgrFactory.class);

  /**
   * The unit name.
   */
  protected String unitName;

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
   * Registered copied to type.
   */
  private Map<Class<?>, List<Class<? extends EntityCopier>>> registeredCopier = new HashMap<>();
  /**
   * The thread emgr.
   */
  private final ThreadLocal<E> threadEmgr = new ThreadLocal<E>();

  /**
   * Instantiates a new emgr factory.
   *
   * @param unitName the unit name
   */
  protected EmgrFactory()
  {
  }

  /**
   * Instantiates a new emgr factory.
   *
   * @param unitName the unit name
   */
  protected EmgrFactory(String unitName)
  {
    this.unitName = unitName;
    initialize();
  }

  protected void initialize()
  {
    this.entityManagerFactory = createEntityManagerFactory(unitName);
    initMetadata();
    registerEvents();
    EmgrFactoryServiceManager.get().getEmgrFactoryService().register(this);
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
    eventFactory.registerEvent(new EmgrBeforeCriteriaUpdateEventHandler());

    // tracer wrapper
    eventFactory.registerEvent(new TracerFindByPkFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrRemoveDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrInsertDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrUpdateDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrMergeDbRecordFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrCreateQueryFilterEventHandler());
    eventFactory.registerEvent(new TracerEmgrCreateTypedQueryFilterEventHandler());
    eventFactory.registerEvent(new EmgrMarkUndeletedCriteriaUpdateFilterEventHandler());
    eventFactory.registerEvent(new EmgrMarkDeletedCriteriaUpdateFilterEventHandler());

  }

  /**
   * Creates a new specific Emgr .
   *
   * @param entityManager the entity manager
   * @return the e
   */
  abstract protected E createEmgr(EntityManager entityManager, EmgrTx<E> emgrTx);

  /**
   * Creates a new Emgr object.
   *
   * @param unitName the unit name
   * @return the entity manager factory
   */
  public EntityManagerFactory createEntityManagerFactory(String unitName)
  {
    Map<String, Object> lsMap = getInitEntityManagerFactoryProperties();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitName, lsMap);
    return emf;
  }

  /**
   * return a modifiable Map for pass to JPA Persistence EntityManagerFactory creation.
   *
   * @return the inits the entity manager factory properties
   */
  protected Map<String, Object> getInitEntityManagerFactoryProperties()
  {
    LocalSettings ls = LocalSettings.get();
    Map<String, Object> lsMap = new HashMap<String, Object>();
    for (Map.Entry<String, String> me : ls.getMap().entrySet()) {
      lsMap.put(me.getKey(), me.getValue());
    }
    return lsMap;
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

  public EmgrTx<E> tx()
  {
    return new EmgrTx<E>(this);
  }

  public EmgrTx<E> notx()
  {
    return new EmgrTx<E>(this).noTx();
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
    return tx().noTx().go(call);
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
    return tx().go(call);
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
    return tx().rollback().go(call);
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
    if (StringUtils.isBlank(user) == true) {
      user = "anon";
    }
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

  /**
   * Provides schema operations.
   *
   * @return the jpa schema service
   */
  public JpaSchemaService getJpaSchemaService()
  {
    return new JpaSchemaServiceImpl(this);
  }
}
