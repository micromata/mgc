package de.micromata.genome.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.hibernate.ejb.AvailableSettings;

import de.micromata.genome.jpa.events.EmgrAfterBeforeRemovedEvent;
import de.micromata.genome.jpa.events.EmgrAfterCopyForUpdateEvent;
import de.micromata.genome.jpa.events.EmgrAfterDetachEvent;
import de.micromata.genome.jpa.events.EmgrAfterInsertedEvent;
import de.micromata.genome.jpa.events.EmgrAfterRemovedEvent;
import de.micromata.genome.jpa.events.EmgrAfterUpdatedEvent;
import de.micromata.genome.jpa.events.EmgrBeforeCopyForUpdateEvent;
import de.micromata.genome.jpa.events.EmgrBeforeCriteriaUpdateEvent;
import de.micromata.genome.jpa.events.EmgrBeforeDetachEvent;
import de.micromata.genome.jpa.events.EmgrBeforeUpdatedEvent;
import de.micromata.genome.jpa.events.EmgrCreateQueryFilterEvent;
import de.micromata.genome.jpa.events.EmgrCreateTypedQueryFilterEvent;
import de.micromata.genome.jpa.events.EmgrEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrFilterEvent;
import de.micromata.genome.jpa.events.EmgrFindByPkFilterEvent;
import de.micromata.genome.jpa.events.EmgrInitForInsertEvent;
import de.micromata.genome.jpa.events.EmgrInitForUpdateEvent;
import de.micromata.genome.jpa.events.EmgrInsertDbRecordFilterEvent;
import de.micromata.genome.jpa.events.EmgrMergeDbRecordFilterEvent;
import de.micromata.genome.jpa.events.EmgrRemoveDbRecordFilterEvent;
import de.micromata.genome.jpa.events.EmgrUpdateCopyFilterEvent;
import de.micromata.genome.jpa.events.EmgrUpdateCriteriaUpdateFilterEvent;
import de.micromata.genome.jpa.events.EmgrUpdateDbRecordFilterEvent;
import de.micromata.genome.jpa.events.impl.EmgrEventTypedQuery;

/**
 * Main class to interact with JPA.
 * 
 * An instance of this class will be created via EmgrFactory and be passed as argument by a EmgrCallable.
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 * @param <EMGR> thistype
 */
public class Emgr<EMGR extends Emgr<?>> implements IEmgr<EMGR>

{
  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(Emgr.class);

  /**
   * Underlying jpa.
   */
  private final EntityManager entityManager;
  /**
   * The factory created this Emgr.
   */
  private final EmgrFactory<EMGR> emgrFactory;

  /**
   * Instantiates a new emgr.
   *
   * @param entityManager the entity manager
   * @param emgrFactory the emgr factory
   */
  public Emgr(EntityManager entityManager, EmgrFactory<EMGR> emgrFactory)
  {
    this.entityManager = entityManager;
    this.emgrFactory = emgrFactory;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public EntityManager getEntityManager()
  {
    return entityManager;
  }

  /**
   * Detach an entity.
   * 
   * @param entity the entity
   * @return the t
   */
  @Override
  public EMGR detach(final Object entity)
  {
    emgrFactory.getEventFactory().invokeEvents(new EmgrBeforeDetachEvent(this, entity));
    entityManager.detach(entity);
    emgrFactory.getEventFactory().invokeEvents(new EmgrAfterDetachEvent(this, entity));
    return getThis();
  }

  /**
   * detaches a list of entities.
   * 
   * @param <E> the element type
   * @param result the result
   * @return the t
   */
  @Override
  public <R> EMGR detach(List<R> result)
  {
    if (result == null) {
      return getThis();
    }
    for (R entity : result) {
      detach(entity);
    }
    return getThis();
  }

  /**
   * Creates the query.
   * 
   * NOTE this is currenty NOT Wrapped with events.
   * 
   * @param <C> the generic type
   * @param cls the cls
   * @return the criteria query
   */
  public <C> CriteriaQuery<C> createQuery(final Class<C> cls)
  {
    return entityManager.getCriteriaBuilder().createQuery(cls);
  }

  /**
   * Creates the query.
   * 
   * NOTE this is currenty NOT Wrapped with events.
   * 
   * @param sql the sql
   * @return the query
   */
  public Query createQuery(final String sql)
  {
    return filterEvent(new EmgrCreateQueryFilterEvent(this, sql), (event) -> {
      event.setResult(entityManager.createQuery(sql));
    });
    //    
  }

  /**
   * Creates the query. NOTE this is currenty NOT Wrapped with events.
   * 
   * @param sql the sql
   * @param keyValues list of pairs. first is column name, second is value
   * @return the query
   */
  public Query createQuery(final String sql, final Object... keyValues)
  {
    Query namedQuery = entityManager.createQuery(sql);
    setParams(namedQuery, keyValues);
    return namedQuery;
  }

  /**
   * Creates the query.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the typed query
   */
  public <R> TypedQuery<R> createQuery(final Class<R> cls, final String sql, final Map<String, Object> values)
  {
    return createQueryAttached(cls, sql, values);
  }

  @Override
  public <R> TypedQuery<R> createQueryAttached(final Class<R> cls, final String sql, final Map<String, Object> values)
  {
    return filterEvent(new EmgrCreateTypedQueryFilterEvent<R>(this, cls, sql, values), (event) -> {
      TypedQuery<R> q = new EmgrEventTypedQuery<>(this, entityManager.createQuery(sql, cls));
      for (Map.Entry<String, Object> me : values.entrySet()) {
        q.setParameter(me.getKey(), me.getValue());
      }
      event.setResult(q);
    });
  }

  /**
   * Detaches the results.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the typed query
   */
  @Override
  public <R> TypedQuery<R> createQueryDetached(final Class<R> cls, final String sql, final Map<String, Object> values)
  {
    return filterEvent(new EmgrCreateTypedQueryFilterEvent<R>(this, cls, sql, values), (event) -> {
      TypedQuery<R> q = new EmgrEventTypedQuery<R>(this, entityManager.createQuery(sql, cls))
      {
        @Override
        public List<R> getResultList()
        {
          List<R> ret = super.getResultList();
          detach(ret);
          return ret;
        }

        @Override
        public R getSingleResult()
        {
          R ret = super.getSingleResult();
          detach(ret);
          return ret;
        }
      };
      for (Map.Entry<String, Object> me : values.entrySet()) {
        q.setParameter(me.getKey(), me.getValue());
      }
      event.setResult(q);
    });
  }

  /**
   * Alternative API to create a Query.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues list of pairs. first is column name, second is value
   * @return the typed query
   */
  public <R> TypedQuery<R> createQuery(final Class<R> cls, final String sql, final Object... keyValues)
  {
    return createQueryAttached(cls, sql, keyValues);
  }

  @Override
  public <R> TypedQuery<R> createQueryAttached(final Class<R> cls, final String sql, final Object... keyValues)
  {
    return filterEvent(new EmgrCreateTypedQueryFilterEvent<R>(this, cls, sql, keyValues), (event) -> {
      TypedQuery<R> q = new EmgrEventTypedQuery<>(this, entityManager.createQuery(sql, cls));
      setParams(q, keyValues);
      event.setResult(q);
    });
  }

  /**
   * Creates the query detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the typed query
   */
  @Override
  public <R> TypedQuery<R> createQueryDetached(final Class<R> cls, final String sql, final Object... keyValues)
  {
    return filterEvent(new EmgrCreateTypedQueryFilterEvent<R>(this, cls, sql, keyValues), (event) -> {
      TypedQuery<R> q = new EmgrEventTypedQuery<R>(this, entityManager.createQuery(sql, cls))
      {
        @Override
        public List<R> getResultList()
        {
          List<R> ret = super.getResultList();
          detach(ret);
          return ret;
        }

        @Override
        public R getSingleResult()
        {
          R ret = super.getSingleResult();
          detach(ret);
          return ret;
        }

      };
      setParams(q, keyValues);
      event.setResult(q);
    });
  }

  /**
   * Sets the params.
   * 
   * @param q the q
   * @param keyValues the key values
   */
  private void setParams(Query q, Object[] keyValues)
  {
    Validate.isTrue(keyValues.length % 2 == 0, "keyValues has to be even");
    for (int i = 0; i + 1 < keyValues.length; i += 2) {
      q.setParameter((String) keyValues[i], keyValues[i + 1]);
    }
  }

  /**
   * Select single.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the r
   */
  @Override
  public <R> R selectSingleAttached(final Class<R> cls, final String sql, final Object... keyValues)
  {
    TypedQuery<R> q = createQuery(cls, sql, keyValues);
    return q.getSingleResult();

  }

  /**
   * Select single.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the r
   */
  @Deprecated
  public <R> R selectSingle(final Class<R> cls, final String sql, final Map<String, Object> values)
  {
    return selectSingleAttached(cls, sql, values);
  }

  /**
   * Select single attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the r
   */
  @Override
  public <R> R selectSingleAttached(final Class<R> cls, final String sql, final Map<String, Object> values)
  {
    return createQuery(cls, sql, values).getSingleResult();

  }

  /**
   * Note this must not have lazy loaded detail.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the r
   */
  @Override
  public <R> R selectSingleDetached(final Class<R> cls, final String sql, final Map<String, Object> values)
  {
    R r = createQuery(cls, sql, values).getSingleResult();
    detach(r);
    return r;
  }

  /**
   * Select single detached.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the r
   */
  @Override
  public <R> R selectSingleDetached(final Class<R> cls, final String sql, final Object... keyValues)
  {

    TypedQuery<R> q = createQuery(cls, sql, keyValues);
    R r = q.getSingleResult();
    detach(r);
    return r;

  }

  /**
   * Same as selectSingleDetached, but returns null in case of NoResultException.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the r
   */
  @Override
  public <R> R findSingleDetached(Class<R> cls, String sql, Object... keyValues)
  {
    try {
      return selectSingleDetached(cls, sql, keyValues);
    } catch (NoResultException nr) { // NOSONAR don't want to handle ex.
      return null;
    }
  }

  /**
   * Lock.
   * 
   * @param <R> the generic type
   * @param entity the entity
   * @param lockMode the lock mode
   * @return the r
   */
  public <R> R lock(final R entity, final LockModeType lockMode)
  {
    entityManager.lock(entity, lockMode);
    return entity;

  }

  /**
   * Select by pk with detached objects.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   * @throws NoResultException if not found
   */

  public <R, PK extends Serializable> R selectByPk(final Class<R> cls, final PK pk)
  {
    return selectByPkDetached(cls, pk);
  }

  /**
   * Select attached by pk.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   * @throws NoResultException if not found
   */
  @Override
  public <R, PK extends Serializable> R selectByPkAttached(final Class<R> cls, final PK pk)
  {

    R r = findByPkAttached(cls, pk);
    if (r == null) {
      throw new NoResultException("Cannot find " + cls.getSimpleName() + " by pk " + pk);
    }
    return r;

  }

  /**
   * Find entity by name.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r. May be null
   */

  public <R, PK extends Serializable> R find(final Class<R> cls, final PK pk)
  {
    return findByPkDetached(cls, pk);
  }

  /**
   * Find by pk detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   */
  @Override
  public <R, PK extends Serializable> R findByPkDetached(final Class<R> cls, final PK pk)
  {
    R r = findByPkAttached(cls, pk);
    if (r != null) {
      detach(r);
    }
    return r;
  }

  /**
   * Find by pk attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   */
  @Override
  public <R, PK extends Serializable> R findByPkAttached(final Class<R> cls, final PK pk)
  {
    return filterEvent(new EmgrFindByPkFilterEvent<R, PK>(this, cls, pk),
        (event) -> {
          R result = entityManager.find(cls, pk);
          event.setResult(result);
        });

  }

  /**
   * Select by pk detached.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   * @throws NoResultException if not found
   */
  @Override
  public <R, PK extends Serializable> R selectByPkDetached(final Class<R> cls, final PK pk)
  {
    R r = selectByPkAttached(cls, pk);
    detach(r);
    return r;

  }

  /**
   * Select.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @param readOnly the read only
   * @return the list
   */
  public <R> List<R> select(Class<R> cls, String sql, Map<String, Object> values, boolean readOnly)
  {
    return selectDetached(cls, sql, values);
  }

  /**
   * Select.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the list
   */
  public <R extends DbRecord<?>> List<R> select(Class<R> cls, String sql, Object... keyValues)
  {
    return selectDetached(cls, sql, keyValues);
  }

  /**
   * Using the select new semantic.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the list
   */
  public <R> List<R> selectNew(Class<R> cls, String sql, Object... keyValues)
  {
    return selectAttached(cls, sql, keyValues);
  }

  /**
   * Select a basic (non entity) data type.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the list
   */
  public <R> List<R> selectT(Class<R> cls, String sql, Object... keyValues)
  {
    if (DbRecord.class.isAssignableFrom(cls) == true) {
      log.warn("Using selectT with a DbRecord: " + cls);
    }
    return selectAttached(cls, sql, keyValues);
  }

  /**
   * Select attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param args the args
   * @return the list
   */
  public <R> List<R> selectAttached(Class<R> cls, String sql, Map<String, Object> args)
  {
    return createQuery(cls, sql, args).getResultList();
  }

  /**
   * Select attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the list
   */
  public <R> List<R> selectAttached(Class<R> cls, String sql, Object... keyValues)
  {
    TypedQuery<R> q = createQuery(cls, sql, keyValues);
    List<R> ret = q.getResultList();
    return ret;
  }

  /**
   * Removed/delete the entities returned by query.
   *
   * @param <R> the generic type
   * @param cls entity
   * @param sql prepared statement
   * @param keyValues argument to select statement
   * @return the int
   */
  @Override
  public <R extends DbRecord<?>> int deleteFromQuery(Class<R> cls, String sql, Object... keyValues)
  {
    List<R> list = createQuery(cls, sql, keyValues).getResultList();
    int count = list.size();
    for (R r : list) {
      remove(r);
    }
    return count;
  }

  /**
   * Note this must not have lazy loaded details.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the list
   */
  @Override
  public <R> List<R> selectDetached(Class<R> cls, String sql, Map<String, Object> values)
  {
    List<R> ret = selectAttached(cls, sql, values);
    detach(ret);
    return ret;
  }

  /**
   * Note this must not have lazy loaded details.
   * 
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the list
   */
  @Override
  public <R> List<R> selectDetached(final Class<R> cls, final String sql, final Object... keyValues)
  {
    TypedQuery<R> q = createQuery(cls, sql, keyValues);
    List<R> ret = q.getResultList();
    detach(ret);
    return ret;
  }

  /**
   * Set the the query to use select for update.
   * 
   * @param query the query
   * @param lockTimetimeInMs the lock timetime in ms
   * @return the t
   */
  @Override
  public EMGR setSelectForUpdate(Query query, int lockTimetimeInMs)
  {
    // "javax.persistence.query.timeout"
    // javax.persistence.lock.timeout
    query.setHint(AvailableSettings.LOCK_TIMEOUT, lockTimetimeInMs);
    query.setHint("javax.persistence.query.timeout", lockTimetimeInMs);
    query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
    return getThis();
  }

  /**
   * Gets the this.
   *
   * @return the this
   */
  @SuppressWarnings("unchecked")
  protected final EMGR getThis()
  {
    return (EMGR) this;
  }

  /**
   * flushes the underlying entitymanager.
   * 
   * @return this
   */
  public EMGR flush()
  {
    entityManager.flush();
    return getThis();
  }

  /**
   * Persists the given {@link DbRecord}.
   *
   * @param rec the rec
   * @return the t
   */
  public EMGR persist(DbRecord<?> rec)
  {
    entityManager.persist(rec);
    return getThis();
  }

  /**
   * Inits the for update.
   * 
   * @param rec the rec
   * @return the t
   */
  public EMGR initForUpdate(DbRecord<?> rec)
  {
    invokeEvent(new EmgrInitForUpdateEvent(this, rec));
    return getThis();
  }

  /**
   * For version columns, returns the current user id.
   * 
   * Delegates to UserDAO by default.
   * 
   * @return the short form of a user name.
   */
  protected String getCurrentUserName()
  {
    // TODO overwrite this
    return "anon";
  }

  /**
   * Identity hex.
   * 
   * @param o the o
   * @return the string
   */
  private String identityHex(Object o)
  {
    return Integer.toHexString(System.identityHashCode(o));
  }

  /**
   * alias to remove.
   * 
   * @param rec the rec
   * @return the t
   */
  @Override
  public EMGR delete(Object rec)
  {
    return remove(rec);
  }

  /**
   * Removes the.
   * 
   * @param rec the rec
   * @return the t
   */
  public EMGR remove(final Object rec)
  {
    if (rec instanceof DbRecord) {
      invokeEvent(new EmgrAfterBeforeRemovedEvent(this, (DbRecord) rec));
    }
    if (log.isDebugEnabled() == true) {
      log.debug("remove: " + rec + " in thread " + Thread.currentThread().getId() + "; em: "
          + identityHex(getEntityManager()));
    }
    filterEvent(new EmgrRemoveDbRecordFilterEvent(this, rec),
        (event) -> {
          entityManager.remove(rec);
          // flush necessary, because if detached inside a transaction, entity will not be persisted
          entityManager.flush();
        });
    invokeEvent(new EmgrAfterRemovedEvent(this, rec));
    return getThis();
  }

  /**
   * Insert.
   * 
   * @param records the records
   */
  public void insert(Collection<? extends DbRecord<?>> records)
  {
    for (DbRecord<?> rec : records) {
      insert(rec);
    }
  }

  @Override
  public <PK extends Serializable> PK insertDetached(final DbRecord<PK> rec)
  {
    PK ret = insertAttached(rec);
    detach(rec);
    return ret;
  }

  @Override
  public <PK extends Serializable> PK insertAttached(final DbRecord<PK> rec)
  {
    initForCreate(rec);
    filterEvent(new EmgrInsertDbRecordFilterEvent(this, rec),
        (event) -> {
          entityManager.persist(rec);
          // flush necessary, because if detached inside a transaction, entity will not be persisted
          entityManager.flush();
        });

    if (log.isDebugEnabled() == true) {
      log.debug("inserted: " + rec.getPk() + " in thread " + Thread.currentThread().getId() + "; em: "
          + identityHex(getEntityManager()));
    }
    invokeEvent(new EmgrAfterInsertedEvent(this, rec));
    return rec.getPk();
  }

  /**
   * Insert.
   * 
   * @param rec the rec
   * @return the t
   */

  public <PK extends Serializable> PK insert(final DbRecord<PK> rec)
  {
    return insertDetached(rec);
  }

  public EMGR update(final DbRecord<?> rec)
  {
    return updateAttached(rec);
  }

  /**
   * Update.
   * 
   * 
   * @param rec the rec
   * @return the t
   */
  @Override
  public EMGR updateAttached(final DbRecord<?> rec)
  {
    invokeEvent(new EmgrBeforeUpdatedEvent(this, rec));
    filterEvent(new EmgrUpdateDbRecordFilterEvent(this, rec),
        (event) -> {
          initForUpdate(rec);
          if (log.isDebugEnabled() == true) {
            log.debug("update: " + rec.getPk() + " in thread " + Thread.currentThread().getId());
          }
          entityManager.persist(rec);
          // flush necessary, because if detached inside a transaction, entity will not be persisted
          entityManager.flush();
        });

    invokeEvent(new EmgrAfterUpdatedEvent(this, rec));
    return getThis();
  }

  /**
   * Updates a record. The rec is not in the persistence context.
   * 
   * may check for OptimisticLock
   *
   * @param rec the rec
   * @return the t
   */
  @Override
  public <R extends DbRecord<?>> EntityCopyStatus updateCopy(final R rec)
  {
    return updateCopy(rec, false);
  }

  /**
   * Updates a record. The rec is not in the persistence context.
   *
   * @param rec the rec
   * @param overwrite if false, may check for OptimisticLock
   * @return the t
   */
  @Override
  public <R extends DbRecord<?>> EntityCopyStatus updateCopy(final R rec, boolean overwrite)
  {
    return update(rec.getClass(), rec.getClass(), rec, overwrite);
  }

  /**
   * Update an entity by copy the Entity specified by iface.
   *
   * @param <R> the generic type
   * @param iface the iface with getter/setter
   * @param entityClass the entity class to select from db.
   * @param newE the new entity.
   * @param overwrite the overwrite. Used for optimistic lock
   * @return the t
   * @throws EntityNotFoundException the entity not found except)ion
   * @throws OptimisticLockException if newE updateCounter != null and differs from persisted.
   */
  @Override
  public <R extends DbRecord<?>> EntityCopyStatus update(Class<? extends R> iface, Class<? extends R> entityClass,
      R newE,
      boolean overwrite, String... ignoreCopyFields)
  {
    R oldE = entityManager.find(entityClass, newE.getPk());
    invokeEvent(new EmgrBeforeCopyForUpdateEvent(this, iface, oldE, newE, overwrite));
    EmgrUpdateCopyFilterEvent tevent = new EmgrUpdateCopyFilterEvent(this, iface, entityClass, oldE, newE, overwrite);
    filterEvent(tevent,
        (EmgrUpdateCopyFilterEvent event) -> {
          EntityCopyStatus status = EmgrCopyUtils.copyTo(this, event.getIface(), event.getTarget(), event.getSource(),
              ignoreCopyFields);
          update(event.getTarget());
          event.setResult(status);
        });
    // TODO RRK pass result.
    invokeEvent(
        new EmgrAfterCopyForUpdateEvent(this, tevent.getIface(), tevent.getTarget(), tevent.getSource(), overwrite));
    return tevent.getResult();
  }

  /**
   * Merge.
   *
   * @param <E> the element type
   * @param rec the rec
   * @return the t
   */
  @Override
  public <R extends DbRecord<?>> R merge(final R rec)
  {
    initForUpdate(rec);
    if (log.isDebugEnabled() == true) {
      log.debug("merge: " + rec.getPk() + " in thread " + Thread.currentThread().getId());
    }
    return filterEvent(new EmgrMergeDbRecordFilterEvent<R>(this, rec),
        (event) -> {
          final R merged = entityManager.merge(rec);
          event.setResult(merged);
        });
  }

  /**
   * Execute an update with a criteriaupdate.
   *
   * @param <E> the element type
   * @param update the update
   * @return number or rows updated.
   */
  @Override
  public <R extends DbRecord<?>> int update(CriteriaUpdate<R> update)
  {
    beforeUpdate(update);
    return filterEvent(new EmgrUpdateCriteriaUpdateFilterEvent<R>(this, update), (event) -> {
      Map<String, Object> args = new HashMap<String, Object>();
      String hql = update.renderHql(args);
      Query query = createUntypedQuery(hql);
      for (Map.Entry<String, Object> me : args.entrySet()) {
        query.setParameter(me.getKey(), me.getValue());
      }
      int ret = query.executeUpdate();
      event.setResult(ret);
    });
  }

  /**
   * Add limiting parameter ekp if ekpowned and manage versions.
   *
   * @param <E> the element type
   * @param update the update
   */
  public <E> void beforeUpdate(CriteriaUpdate<E> update)
  {
    invokeEvent(new EmgrBeforeCriteriaUpdateEvent(this, update));
  }

  /**
   * Creates an untyped query.
   * <p/>
   * This is necessary for JPA-based executeUpdate() statements, as for example in the deletion of shipments (which is
   * done * without the entity manager's delete method).
   *
   * @param sql the sql statement
   * @param keyValues the key values
   * @return an untyped query
   */
  @Override
  public Query createUntypedQuery(String sql, Object... keyValues)
  {
    return createQuery(sql, keyValues);
  }

  /**
   * Inits the for create.
   * 
   * @param rec the rec
   * @return the t
   */
  public EMGR initForCreate(DbRecord<?> rec)
  {
    invokeEvent(new EmgrInitForInsertEvent(this, rec));
    return getThis();
  }

  /**
   * Checks if is checks for insert trigger for version.
   *
   * @return true, if is checks for insert trigger for version
   */
  public boolean isHasInsertTriggerForVersion()
  {
    return getEmgrFactory().isHasInsertTriggerForVersion();
  }

  /**
   * Checks if is checks for update trigger for version.
   *
   * @return true, if is checks for update trigger for version
   */
  public boolean isHasUpdateTriggerForVersion()
  {
    return getEmgrFactory().isHasUpdateTriggerForVersion();
  }

  /**
   * {@inheritDoc}
   *
   */

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public EmgrFactory<EMGR> getEmgrFactory()
  {
    return emgrFactory;
  }

  /**
   * Invoke event.
   *
   * @param event the event
   */
  protected void invokeEvent(EmgrEvent event)
  {
    getEmgrFactory().getEventFactory().invokeEvents(event);
  }

  /**
   * Filter event.
   *
   * @param <E> the element type
   * @param <R> the generic type
   * @param event the event
   * @param nested the nested
   * @return the r
   */
  protected <E extends EmgrFilterEvent<R>, R> R filterEvent(EmgrFilterEvent<R> event, EmgrEventHandler<E> nested)
  {
    return getEmgrFactory().getEventFactory().invokeEvents(event, nested);
  }
}
