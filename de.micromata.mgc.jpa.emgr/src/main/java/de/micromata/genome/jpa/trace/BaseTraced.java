package de.micromata.genome.jpa.trace;

import de.micromata.genome.jpa.trace.eventhandler.StatsJpaTracer;

/**
 * Base tracing Wrapper.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class BaseTraced
{
  /** The sql. */
  protected String sql;

  /** The key values. */
  protected Object[] keyValues;

  /**
   * The jpa tracer.
   */
  protected JpaTracer jpaTracer = StatsJpaTracer.get();

  /**
   * Instantiates a new base traced.
   */
  public BaseTraced()
  {

  }

  /**
   * Instantiates a new base traced.
   * 
   * @param jpaTracer the jpa tracer
   * @param sql the sql
   * @param keyValues the key values
   */
  public BaseTraced(JpaTracer jpaTracer, String sql, Object[] keyValues)
  {
    this.sql = sql;
    this.keyValues = keyValues;
    this.jpaTracer = jpaTracer;
  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
  }

  public Object[] getKeyValues()
  {
    return keyValues;
  }

  public void setKeyValues(Object[] keyValues)
  {
    this.keyValues = keyValues;
  }

  public JpaTracer getJpaTracer()
  {
    return jpaTracer;
  }

  public void setJpaTracer(JpaTracer jpaTracer)
  {
    this.jpaTracer = jpaTracer;
  }
}
