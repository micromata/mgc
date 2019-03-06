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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Represent a transaction.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrTx<E extends IEmgr<?>>
{
  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(EmgrTx.class);

  /**
   * The emfac.
   */
  private EmgrFactory<E> emfac;

  /**
   * The requires new.
   */
  boolean requiresNew = false;

  boolean rollback = false;

  /**
   * The requires existing.
   */
  boolean requiresExisting = false;

  /**
   * The timeout.
   * 
   * TODO RK if (emHolder != null && emHolder.hasTimeout()) { int timeoutValue = (int) emHolder.getTimeToLiveInMillis();
   * try { query.setHint("javax.persistence.query.timeout", timeoutValue); } catch (IllegalArgumentException ex) { // oh
   * well, at least we tried... }
   */
  long timeout = -1;
  /**
   * This has an own transaction.
   */
  private boolean ownEmgr = false;

  private E prevEmgr = null;

  private EntityTransaction tx;

  /**
   * Instantiates a new tx info.
   *
   * @param emfac the emfac
   */
  public EmgrTx(EmgrFactory<E> emfac)
  {
    this.emfac = emfac;
  }

  private void setTransactionTimeOut(E emgr)
  {
    if (timeout == -1) {
      return;
    }
    Session session = emgr.getEntityManager().unwrap(Session.class);
    Transaction trans = session.getTransaction();
    trans.setTimeout((int) (timeout / 1000));

  }

  private void startTransaction(E emgr)
  {

    setTransactionTimeOut(emgr);
    tx.begin();
    if (rollback == true) {
      tx.setRollbackOnly();
    }
  }

  private void finalTransaction(E emgr)
  {
    emgr.getEntityManager().flush();
    if (rollback == true) {
      tx.rollback();
    } else {
      tx.commit();
    }
  }

  private RuntimeException handleException(E emgr, RuntimeException ex)
  {
    if (ex instanceof RollbackException) {
      if (log.isDebugEnabled() == true) {
        log.debug("rollback tx:  in thread " + Thread.currentThread().getId() + "; " + ex.getMessage());
      }
      return ex;
    } else {
      if (tx != null && tx.isActive() == true) {
        tx.rollback();
      }
      return EmgrFactory.convertException(ex);
    }
  }

  private void checkFlags()
  {
    if (requiresExisting == true && requiresNew == true) {
      // TODO RK throw
    }
  }

  public <R> R go(EmgrCallable<R, E> call)
  {
    checkFlags();
    E emgr = getCreateEmgr();
    try {
      if (ownEmgr == false) {
        return call.call(emgr);
      }
      tx = emgr.getEntityManager().getTransaction();
      if (tx.isActive() == true) {
        return call.call(emgr);
      }
      startTransaction(emgr);
      R ret = call.call(emgr);
      finalTransaction(emgr);
      return ret;
      // TODO RK caching ex
    } catch (RuntimeException ex) {
      throw handleException(emgr, ex);
    } finally {
      releaseEmgr(emgr);
    }
  }

  protected E getCreateEmgr()
  {
    if (requiresNew == true) {
      return createNewEmgr();
    }
    E pEmgr = emfac.getThreadEmgr().get();
    if (pEmgr == null) {
      if (requiresExisting == true) {
        throw new TransactionRequiredException("no transaction is in progress");
      }
      return createNewEmgr();
    }
    EmgrTx<?> parentTx = pEmgr.getEmgrTx();
    boolean reqNew = false;
    if (parentTx.rollback != true && rollback == true) {
      reqNew = true;
    }

    if (reqNew == true) {
      return createNewEmgr();
    }
    return pEmgr;

  }

  protected E createNewEmgr()
  {
    ownEmgr = true;
    EntityManager entityManager = emfac.getEntityManagerFactory().createEntityManager();
    E emgr = emfac.createEmgr(entityManager, this);
    prevEmgr = emfac.getThreadEmgr().get();
    emfac.getThreadEmgr().set(emgr);
    return emgr;
  }

  protected void releaseEmgr(E emgr)
  {
    if (ownEmgr == false) {
      return;
    }
    ownEmgr = false;
    emgr.getEntityManager().close();
    emfac.getThreadEmgr().set(prevEmgr);
  }

  /**
   * Requires new.
   *
   * @return the tx info
   */
  public EmgrTx<E> requiresNew()
  {
    requiresNew = true;
    return this;
  }

  /**
   * Requires new.
   *
   * @return the tx info
   */
  public EmgrTx<E> requires()
  {
    requiresExisting = true;
    return this;
  }


  /**
   * Rollback at the end of the transaction.
   * 
   * @return the entitiy transaction
   */
  public EmgrTx<E> rollback()
  {
    rollback = true;
    return this;
  }

  /**
   * Read only.
   *
   * @return the tx info
   */
  public EmgrTx<E> readOnly()
  {
    rollback = true;
    return this;
  }

  /**
   * Read only.
   *
   * @return the tx info
   */
  public EmgrTx<E> timeOut(long timeout)
  {
    this.timeout = timeout;
    return this;
  }

  public EmgrFactory<E> getEmfac()
  {
    return emfac;
  }

  public void setEmfac(EmgrFactory<E> emfac)
  {
    this.emfac = emfac;
  }

  public boolean isRequiresNew()
  {
    return requiresNew;
  }

  public void setRequiresNew(boolean requiresNew)
  {
    this.requiresNew = requiresNew;
  }

  public boolean isRequiresExisting()
  {
    return requiresExisting;
  }

  public void setRequiresExisting(boolean requiresExisting)
  {
    this.requiresExisting = requiresExisting;
  }

  public long getTimeout()
  {
    return timeout;
  }

  public void setTimeout(long timeout)
  {
    this.timeout = timeout;
  }

}
