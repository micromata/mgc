package de.micromata.genome.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wraps a typed query and delegates the most methods to it
 *
 * @author Sebastian Hardt (s.hardt@micromata.de)
 *         Date: 9/10/13
 *         Time: 1:43 PM
 */
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 * Wrappes a typed query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Sebastian Hardt (s.hardt@micromata.de) Date: 9/5/13 Time: 2:00 PM
 * @param <X> the generic type
 */
public abstract class WrappedTypedQuery<X>implements TypedQuery<X>
{

  /**
   * The nested.
   */
  protected final TypedQuery<X> nested;

  /**
   * Instantiates a new wrapped typed query.
   *
   * @param nested the nested
   */
  public WrappedTypedQuery(TypedQuery<X> nested)
  {
    this.nested = nested;
  }

  public TypedQuery<X> getNested()
  {
    return nested;
  }

  @Override
  public abstract List<X> getResultList();

  @Override
  public abstract X getSingleResult();

  @Override
  public abstract int executeUpdate();

  @Override
  public TypedQuery<X> setMaxResults(int maxResult)
  {
    nested.setMaxResults(maxResult);
    return this;
  }

  @Override
  public int getMaxResults()
  {
    return nested.getMaxResults();
  }

  @Override
  public TypedQuery<X> setFirstResult(int startPosition)
  {
    nested.setFirstResult(startPosition);
    return this;
  }

  @Override
  public int getFirstResult()
  {
    return nested.getFirstResult();
  }

  @Override
  public TypedQuery<X> setHint(String hintName, Object value)
  {
    nested.setHint(hintName, value);
    return this;
  }

  @Override
  public Map<String, Object> getHints()
  {
    return nested.getHints();
  }

  @Override
  public TypedQuery<X> setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType)
  {
    nested.setParameter(param, value, temporalType);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(Parameter<Date> param, Date value, TemporalType temporalType)
  {
    nested.setParameter(param, value, temporalType);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(String name, Object value)
  {
    nested.setParameter(name, value);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(String name, Calendar value, TemporalType temporalType)
  {
    nested.setParameter(name, value, temporalType);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(String name, Date value, TemporalType temporalType)
  {
    nested.setParameter(name, value, temporalType);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(int position, Object value)
  {
    nested.setParameter(position, value);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(int position, Calendar value, TemporalType temporalType)
  {
    nested.setParameter(position, value, temporalType);
    return this;
  }

  @Override
  public TypedQuery<X> setParameter(int position, Date value, TemporalType temporalType)
  {
    nested.setParameter(position, value, temporalType);
    return this;
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
    return nested.getParameter(position);
  }

  @Override
  public TypedQuery<X> setFlushMode(FlushModeType flushMode)
  {
    nested.setFlushMode(flushMode);
    return this;
  }

  @Override
  public FlushModeType getFlushMode()
  {
    return nested.getFlushMode();
  }

  @Override
  public TypedQuery<X> setLockMode(LockModeType lockMode)
  {
    nested.setLockMode(lockMode);
    return this;
  }

  @Override
  public LockModeType getLockMode()
  {
    return nested.getLockMode();
  }

  @Override
  public <X> X unwrap(Class<X> cls)
  {
    return nested.unwrap(cls);
  }

  @Override
  public TypedQuery setParameter(Parameter param, Object value)
  {
    nested.setParameter(param, value);
    return this;
  }
}
