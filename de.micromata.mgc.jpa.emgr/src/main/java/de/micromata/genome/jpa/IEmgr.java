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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

/**
 * Basic interface to an Entitymanager.
 *
 * find*-Methods may return null.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <EMGR> the element type
 */
public interface IEmgr<EMGR extends IEmgr<?>>
{

  /**
   * Gets the entity manager.
   *
   * @return the entity manager
   */
  EntityManager getEntityManager();

  /**
   * The factory, created this emgr.
   *
   * @return the emgr factory
   */
  EmgrFactory<EMGR> getEmgrFactory();

  /**
   * Get the Transaction.
   *
   * @return the emgr tx
   */
  EmgrTx<EMGR> getEmgrTx();

  /**
   * Find by pk attached.
   *
   * @param <R> the generic type
   * @param <PK> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return null if not found
   */
  <R, PK extends Serializable> R findByPkAttached(final Class<R> cls, final PK pk);

  /**
   * Find by pk detached.
   *
   * @param <R> the generic type
   * @param <PK> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   */
  <R, PK extends Serializable> R findByPkDetached(final Class<R> cls, final PK pk);

  /**
   * Select by pk detached.
   *
   * @param <R> the generic type
   * @param <PK> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   * @throws NoResultException the no result exception
   */
  <R, PK extends Serializable> R selectByPkDetached(final Class<R> cls, final PK pk) throws NoResultException;

  /**
   * Select by pk attached.
   *
   * @param <R> the generic type
   * @param <PK> the generic type
   * @param cls the cls
   * @param pk the pk
   * @return the r
   * @throws NoResultException the no result exception
   */
  <R, PK extends Serializable> R selectByPkAttached(final Class<R> cls, final PK pk) throws NoResultException;

  /**
   * Select single attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the r
   */
  <R> R selectSingleAttached(final Class<R> cls, final String sql, final Object... keyValues);

  <R> List<R> selectAttached(Class<R> cls, String sql, Map<String, Object> args);

  <R> List<R> selectAttached(final Class<R> cls, final String sql, final Object... keyValues);

  /**
   * Select single detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the r
   */
  <R> R selectSingleDetached(final Class<R> cls, final String sql, final Object... keyValues);

  /**
   * Select single attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the r
   */
  <R> R selectSingleAttached(final Class<R> cls, final String sql, final Map<String, Object> values);

  /**
   * Select single detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the r
   */
  <R> R selectSingleDetached(final Class<R> cls, final String sql, final Map<String, Object> values);

  /**
   * Select entities and return them detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the list
   */
  <R> List<R> selectDetached(final Class<R> cls, final String sql, final Object... keyValues);

  /**
   * Select detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the list
   */
  <R> List<R> selectDetached(Class<R> cls, String sql, Map<String, Object> values);

  /**
   * Select all existant entities of given class.
   *
   * Be carfull, use it only for adminstrational usses.
   *
   * @param  <R> the type of the entity
   * @param cls the class
   * @return a list of attached entities matching to the given class
   */
  <R> List<R> selectAllAttached(Class<R> cls);

  /**
   * Creates the query detached.
   *
   * The query returns the entity detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the typed query
   *         EmgrAfterDetachEvent
   */
  <R> TypedQuery<R> createQueryDetached(final Class<R> cls, final String sql, final Map<String, Object> values);

  /**
   * Creates the query attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the typed query
   */
  <R> TypedQuery<R> createQueryAttached(final Class<R> cls, final String sql, final Object... keyValues);

  /**
   * Creates the query attached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param values the values
   * @return the typed query
   */
  <R> TypedQuery<R> createQueryAttached(final Class<R> cls, final String sql, final Map<String, Object> values);

  /**
   * Creates the query detached.
   *
   * The query returns the entity detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the typed query
   */
  <R> TypedQuery<R> createQueryDetached(final Class<R> cls, final String sql, final Object... keyValues);

  /**
   * Creates the untyped query.
   *
   * @param sql the sql
   * @param keyValues the key values
   * @return the query
   */
  Query createUntypedQuery(String sql, Object... keyValues);

  /**
   * Sets the select for update.
   *
   * @param query the query
   * @param lockTimetimeInMs the lock timetime in ms
   */
  void setSelectForUpdate(Query query, int lockTimetimeInMs);

  /**
   * Sets the timeout.
   *
   * @param query the query
   * @param timeOutInMs the time out in ms
   */
  void setQueryTimeout(Query query, int timeOutInMs);

  /**
   * Find single detached.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return null if not found.
   */
  <R> R findSingleDetached(Class<R> cls, String sql, Object... keyValues);

  /**
   * Inserts an entity.
   *
   * Entity is detachaed.
   *
   * @param <PK> the generic type
   * @param rec the rec
   * @return PK of the inserted entity.
   */
  <PK extends Serializable> PK insertDetached(final DbRecord<PK> rec);

  /**
   * Insert attached.
   *
   * @param <PK> the generic type
   * @param rec the rec
   * @return the pk
   */
  <PK extends Serializable> PK insertAttached(final DbRecord<PK> rec);

  /**
   * Update an already attached object.
   *
   * @param rec the rec
   */
  void updateAttached(final DbRecord<?> rec);

  /**
   * Update copy.
   *
   * @param <R> the generic type
   * @param rec the rec
   * @return the emgr
   */
  <R extends DbRecord<?>> EntityCopyStatus updateCopy(final R rec);

  /**
   * Update copy.
   *
   * @param <R> the generic type
   * @param rec the rec
   * @param overwrite the overwrite
   * @return the emgr
   */
  <R extends DbRecord<?>> EntityCopyStatus updateCopy(final R rec, boolean overwrite);

  /**
   * Update.
   *
   * @param <R> the generic type
   * @param iface the iface
   * @param entityClass the entity class
   * @param newE the new e
   * @param overwrite the overwrite
   * @param ignoreCopyFields ignore copy fields
   * @return the emgr
   */
  <R extends DbRecord<?>> EntityCopyStatus update(Class<? extends R> iface, Class<? extends R> entityClass, R newE,
      boolean overwrite, String... ignoreCopyFields);

  /**
   * Merge.
   *
   * @param <R> the generic type
   * @param rec the rec
   * @return the r
   */
  <R extends DbRecord<?>> R merge(final R rec);

  /**
   * Update.
   *
   * @param <R> the generic type
   * @param update the update
   * @return the int
   */
  <R extends DbRecord<?>> int update(CriteriaUpdate<R> update);

  /**
   * Delete the classes found by the query.
   *
   * @param <R> the generic type
   * @param cls the cls
   * @param sql the sql
   * @param keyValues the key values
   * @return the int
   */
  <R extends DbRecord<?>> int deleteFromQuery(Class<R> cls, String sql, Object... keyValues);

  /**
   * Delete an already attached entity.
   *
   * @param rec the rec
   * @throws IllegalArgumentException if the instance is not an entity or is a detached entity
   */
  void deleteAttached(DbRecord<?> rec) throws IllegalArgumentException;

  /**
   * Delete an detached entity. The entity will first be loaded with selectByPkAttached and this passed to
   * deleteAttached.
   *
   * @param rec the rec
   * @param overwrite checks if entity has the same modification counter.
   * @throws OptimisticLockException in case updatecounter doesn't match.
   */
  void deleteDetached(DbRecord<?> rec, boolean overwrite) throws OptimisticLockException;

  /**
   * Mark deleted.
   *
   * @param rec the rec
   * @param <T> the type of the record
   * @return true, if record was found and it was previously not marked as deleted.
   */
  <T extends MarkDeletableRecord<?>> boolean markDeleted(T rec);

  /**
   * Mark undeleted.
   *
   * @param rec the rec
   * @param <T> the type of the record
   * @return true, if record was found and it was previously marked as deleted.
   */
  <T extends MarkDeletableRecord<?>> boolean markUndeleted(T rec);

  /**
   * Detach.
   *
   * @param entity the entity
   */
  void detach(final Object entity);

  /**
   * Detach.
   *
   * @param <R> the generic type
   * @param result the result
   */
  <R> void detach(List<R> result);

}
