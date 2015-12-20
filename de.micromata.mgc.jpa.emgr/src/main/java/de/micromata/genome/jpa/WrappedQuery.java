package de.micromata.genome.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Wrapps a Query.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class WrappedQuery implements Query
{
  protected Query nested;

  public WrappedQuery(Query nested)
  {
    super();
    this.nested = nested;
  }

  @Override
  public List getResultList()
  {
    return nested.getResultList();
  }

  @Override
  public Object getSingleResult()
  {
    return nested.getSingleResult();
  }

  @Override
  public int executeUpdate()
  {
    return nested.executeUpdate();
  }

  @Override
  public Query setMaxResults(int maxResult)
  {
    return nested.setMaxResults(maxResult);
  }

  @Override
  public int getMaxResults()
  {
    return nested.getMaxResults();
  }

  @Override
  public Query setFirstResult(int startPosition)
  {
    return nested.setFirstResult(startPosition);
  }

  @Override
  public int getFirstResult()
  {
    return nested.getFirstResult();
  }

  @Override
  public Query setHint(String hintName, Object value)
  {
    return nested.setHint(hintName, value);
  }

  @Override
  public Map<String, Object> getHints()
  {
    return nested.getHints();
  }

  @Override
  public <T> Query setParameter(Parameter<T> param, T value)
  {
    return nested.setParameter(param, value);
  }

  @Override
  public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(param, value, temporalType);
  }

  @Override
  public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType)
  {
    return nested.setParameter(param, value, temporalType);
  }

  @Override
  public Query setParameter(String name, Object value)
  {
    return nested.setParameter(name, value);
  }

  @Override
  public Query setParameter(String name, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(name, value, temporalType);
  }

  @Override
  public Query setParameter(String name, Date value, TemporalType temporalType)
  {
    return nested.setParameter(name, value, temporalType);
  }

  @Override
  public Query setParameter(int position, Object value)
  {
    return nested.setParameter(position, value);
  }

  @Override
  public Query setParameter(int position, Calendar value, TemporalType temporalType)
  {
    return nested.setParameter(position, value, temporalType);
  }

  @Override
  public Query setParameter(int position, Date value, TemporalType temporalType)
  {
    return nested.setParameter(position, value, temporalType);
  }

  @Override
  public Set<Parameter<?>> getParameters()
  {
    return nested.getParameters();
  }

  @Override
  public Parameter<?> getParameter(String name)
  {
    return nested.getParameter(name);
  }

  @Override
  public <T> Parameter<T> getParameter(String name, Class<T> type)
  {
    return nested.getParameter(name, type);
  }

  @Override
  public Parameter<?> getParameter(int position)
  {
    return nested.getParameter(position);
  }

  @Override
  public <T> Parameter<T> getParameter(int position, Class<T> type)
  {
    return nested.getParameter(position, type);
  }

  @Override
  public boolean isBound(Parameter<?> param)
  {
    return nested.isBound(param);
  }

  @Override
  public <T> T getParameterValue(Parameter<T> param)
  {
    return nested.getParameterValue(param);
  }

  @Override
  public Object getParameterValue(String name)
  {
    return nested.getParameterValue(name);
  }

  @Override
  public Object getParameterValue(int position)
  {
    return nested.getParameterValue(position);
  }

  @Override
  public Query setFlushMode(FlushModeType flushMode)
  {
    return nested.setFlushMode(flushMode);
  }

  @Override
  public FlushModeType getFlushMode()
  {
    return nested.getFlushMode();
  }

  @Override
  public Query setLockMode(LockModeType lockMode)
  {
    return nested.setLockMode(lockMode);
  }

  @Override
  public LockModeType getLockMode()
  {
    return nested.getLockMode();
  }

  @Override
  public <T> T unwrap(Class<T> cls)
  {
    return nested.unwrap(cls);
  }

}
