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

package de.micromata.genome.jpa.trace;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;

import de.micromata.genome.util.runtime.CallableX;

/**
 * A traced Query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <X> the generic type
 */
public class TracedTypedQuery<X>extends BaseTraced implements TypedQuery<X>
{

  /**
   * The nested.
   */
  private TypedQuery<X> nested;

  /**
   * Instantiates a new traced typed query.
   *
   * @param jpaTracer the jpa tracer
   * @param nested the nested
   * @param sql the sql
   * @param keyValues the key values
   */
  public TracedTypedQuery(JpaTracer jpaTracer, TypedQuery<X> nested, String sql, Object[] keyValues)
  {
    super(jpaTracer, sql, keyValues);
    this.nested = nested;
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#getResultList()
   */
  @Override
  public List<X> getResultList()
  {
    return jpaTracer.execute(sql, keyValues, new CallableX<List<X>, RuntimeException>()
    {

      @Override
      public List<X> call() throws RuntimeException
      {
        return nested.getResultList();
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#getSingleResult()
   */
  @Override
  public X getSingleResult()
  {
    return jpaTracer.execute(sql, keyValues, new CallableX<X, RuntimeException>()
    {

      @Override
      public X call() throws RuntimeException
      {
        return nested.getSingleResult();
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setMaxResults(int)
   */
  @Override
  public TypedQuery<X> setMaxResults(int maxResult)
  {
    return nested.setMaxResults(maxResult);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#executeUpdate()
   */
  @Override
  public int executeUpdate()
  {
    return jpaTracer.execute(sql, keyValues, new CallableX<Integer, RuntimeException>()
    {

      @Override
      public Integer call() throws RuntimeException
      {
        return nested.executeUpdate();
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setFirstResult(int)
   */
  @Override
  public TypedQuery<X> setFirstResult(int startPosition)
  {
    return nested.setFirstResult(startPosition);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setHint(java.lang.String, java.lang.Object)
   */
  @Override
  public TypedQuery<X> setHint(String hintName, Object value)
  {
    return nested.setHint(hintName, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getMaxResults()
   */
  @Override
  public int getMaxResults()
  {
    return nested.getMaxResults();
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(jakarta.persistence.Parameter, java.lang.Object)
   */
  @Override
  public <T> TypedQuery<X> setParameter(Parameter<T> param, T value)
  {
    return nested.setParameter(param, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getFirstResult()
   */
  @Override
  public int getFirstResult()
  {
    return nested.getFirstResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(jakarta.persistence.Parameter, java.util.Calendar,
   * jakarta.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(param, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(jakarta.persistence.Parameter, java.util.Date,
   * jakarta.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(Parameter<Date> param, Date value, TemporalType temporalType)
  {
    return nested.setParameter(param, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getHints()
   */
  @Override
  public Map<String, Object> getHints()
  {
    return nested.getHints();
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(java.lang.String, java.lang.Object)
   */
  @Override
  public TypedQuery<X> setParameter(String name, Object value)
  {
    return nested.setParameter(name, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(java.lang.String, java.util.Calendar,
   * jakarta.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(String name, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(name, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(java.lang.String, java.util.Date, jakarta.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(String name, Date value, TemporalType temporalType)
  {
    return nested.setParameter(name, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(int, java.lang.Object)
   */
  @Override
  public TypedQuery<X> setParameter(int position, Object value)
  {
    return nested.setParameter(position, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(int, java.util.Calendar, jakarta.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(int position, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(position, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setParameter(int, java.util.Date, jakarta.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(int position, Date value, TemporalType temporalType)
  {
    return nested.setParameter(position, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setFlushMode(jakarta.persistence.FlushModeType)
   */
  @Override
  public TypedQuery<X> setFlushMode(FlushModeType flushMode)
  {
    return nested.setFlushMode(flushMode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.TypedQuery#setLockMode(jakarta.persistence.LockModeType)
   */
  @Override
  public TypedQuery<X> setLockMode(LockModeType lockMode)
  {
    return nested.setLockMode(lockMode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameters()
   */
  @Override
  public Set<Parameter<?>> getParameters()
  {
    return nested.getParameters();
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameter(java.lang.String)
   */
  @Override
  public Parameter<?> getParameter(String name)
  {
    return nested.getParameter(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameter(java.lang.String, java.lang.Class)
   */
  @Override
  public <T> Parameter<T> getParameter(String name, Class<T> type)
  {
    return nested.getParameter(name, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameter(int)
   */
  @Override
  public Parameter<?> getParameter(int position)
  {
    return nested.getParameter(position);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameter(int, java.lang.Class)
   */
  @Override
  public <T> Parameter<T> getParameter(int position, Class<T> type)
  {
    return nested.getParameter(position, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#isBound(jakarta.persistence.Parameter)
   */
  @Override
  public boolean isBound(Parameter<?> param)
  {
    return nested.isBound(param);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameterValue(jakarta.persistence.Parameter)
   */
  @Override
  public <T> T getParameterValue(Parameter<T> param)
  {
    return nested.getParameterValue(param);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameterValue(java.lang.String)
   */
  @Override
  public Object getParameterValue(String name)
  {
    return nested.getParameterValue(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getParameterValue(int)
   */
  @Override
  public Object getParameterValue(int position)
  {
    return nested.getParameterValue(position);
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getFlushMode()
   */
  @Override
  public FlushModeType getFlushMode()
  {
    return nested.getFlushMode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#getLockMode()
   */
  @Override
  public LockModeType getLockMode()
  {
    return nested.getLockMode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.persistence.Query#unwrap(java.lang.Class)
   */
  @Override
  public <T> T unwrap(Class<T> cls)
  {
    return nested.unwrap(cls);
  }

  public TypedQuery<X> getNested()
  {
    return nested;
  }

  public void setNested(TypedQuery<X> nested)
  {
    this.nested = nested;
  }

}
