package de.micromata.genome.jpa.trace;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

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
   * @see javax.persistence.TypedQuery#getResultList()
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
   * @see javax.persistence.TypedQuery#getSingleResult()
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
   * @see javax.persistence.TypedQuery#setMaxResults(int)
   */
  @Override
  public TypedQuery<X> setMaxResults(int maxResult)
  {
    return nested.setMaxResults(maxResult);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#executeUpdate()
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
   * @see javax.persistence.TypedQuery#setFirstResult(int)
   */
  @Override
  public TypedQuery<X> setFirstResult(int startPosition)
  {
    return nested.setFirstResult(startPosition);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setHint(java.lang.String, java.lang.Object)
   */
  @Override
  public TypedQuery<X> setHint(String hintName, Object value)
  {
    return nested.setHint(hintName, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getMaxResults()
   */
  @Override
  public int getMaxResults()
  {
    return nested.getMaxResults();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(javax.persistence.Parameter, java.lang.Object)
   */
  @Override
  public <T> TypedQuery<X> setParameter(Parameter<T> param, T value)
  {
    return nested.setParameter(param, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getFirstResult()
   */
  @Override
  public int getFirstResult()
  {
    return nested.getFirstResult();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(javax.persistence.Parameter, java.util.Calendar,
   * javax.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(param, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(javax.persistence.Parameter, java.util.Date,
   * javax.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(Parameter<Date> param, Date value, TemporalType temporalType)
  {
    return nested.setParameter(param, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getHints()
   */
  @Override
  public Map<String, Object> getHints()
  {
    return nested.getHints();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(java.lang.String, java.lang.Object)
   */
  @Override
  public TypedQuery<X> setParameter(String name, Object value)
  {
    return nested.setParameter(name, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(java.lang.String, java.util.Calendar,
   * javax.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(String name, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(name, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(java.lang.String, java.util.Date, javax.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(String name, Date value, TemporalType temporalType)
  {
    return nested.setParameter(name, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(int, java.lang.Object)
   */
  @Override
  public TypedQuery<X> setParameter(int position, Object value)
  {
    return nested.setParameter(position, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(int, java.util.Calendar, javax.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(int position, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(position, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setParameter(int, java.util.Date, javax.persistence.TemporalType)
   */
  @Override
  public TypedQuery<X> setParameter(int position, Date value, TemporalType temporalType)
  {
    return nested.setParameter(position, value, temporalType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setFlushMode(javax.persistence.FlushModeType)
   */
  @Override
  public TypedQuery<X> setFlushMode(FlushModeType flushMode)
  {
    return nested.setFlushMode(flushMode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.TypedQuery#setLockMode(javax.persistence.LockModeType)
   */
  @Override
  public TypedQuery<X> setLockMode(LockModeType lockMode)
  {
    return nested.setLockMode(lockMode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameters()
   */
  @Override
  public Set<Parameter<?>> getParameters()
  {
    return nested.getParameters();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameter(java.lang.String)
   */
  @Override
  public Parameter<?> getParameter(String name)
  {
    return nested.getParameter(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameter(java.lang.String, java.lang.Class)
   */
  @Override
  public <T> Parameter<T> getParameter(String name, Class<T> type)
  {
    return nested.getParameter(name, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameter(int)
   */
  @Override
  public Parameter<?> getParameter(int position)
  {
    return nested.getParameter(position);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameter(int, java.lang.Class)
   */
  @Override
  public <T> Parameter<T> getParameter(int position, Class<T> type)
  {
    return nested.getParameter(position, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#isBound(javax.persistence.Parameter)
   */
  @Override
  public boolean isBound(Parameter<?> param)
  {
    return nested.isBound(param);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameterValue(javax.persistence.Parameter)
   */
  @Override
  public <T> T getParameterValue(Parameter<T> param)
  {
    return nested.getParameterValue(param);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameterValue(java.lang.String)
   */
  @Override
  public Object getParameterValue(String name)
  {
    return nested.getParameterValue(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getParameterValue(int)
   */
  @Override
  public Object getParameterValue(int position)
  {
    return nested.getParameterValue(position);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getFlushMode()
   */
  @Override
  public FlushModeType getFlushMode()
  {
    return nested.getFlushMode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#getLockMode()
   */
  @Override
  public LockModeType getLockMode()
  {
    return nested.getLockMode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.persistence.Query#unwrap(java.lang.Class)
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
