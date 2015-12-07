package de.micromata.mgc.db.jpa.api.events;

import java.util.Map;

import javax.persistence.TypedQuery;

import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Wrapps creating a typed query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic type
 */
public class EmgrCreateTypedQueryFilterEvent<R>extends EmgrFilterEvent<TypedQuery<R>>
{

  /**
   * The entity class.
   */
  final Class<R> entityClass;

  /**
   * The sql.
   */
  private String sql;
  /**
   * either argMap or keyValues is set.
   */
  private Map<String, Object> argMap;

  /**
   * The key values.
   */
  private Object[] keyValues;

  /**
   * Instantiates a new emgr create typed query filter event.
   *
   * @param emgr the emgr
   * @param entityClass the entity class
   * @param sql the sql
   * @param args the args
   */
  public EmgrCreateTypedQueryFilterEvent(IEmgr<?> emgr, Class<R> entityClass, String sql, Map<String, Object> args)
  {
    super(emgr);
    this.entityClass = entityClass;
    this.sql = sql;
    this.argMap = args;
  }

  /**
   * Instantiates a new emgr create typed query filter event.
   *
   * @param emgr the emgr
   * @param entityClass the entity class
   * @param sql the sql
   * @param keyValues the key values
   */
  public EmgrCreateTypedQueryFilterEvent(IEmgr<?> emgr, Class<R> entityClass, String sql, Object... keyValues)
  {
    super(emgr);
    this.entityClass = entityClass;
    this.sql = sql;
    this.keyValues = keyValues;
  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
  }

  public Map<String, Object> getArgs()
  {
    return argMap;
  }

  public void setArgs(Map<String, Object> args)
  {
    this.argMap = args;
  }

  public Object[] getKeyValues()
  {
    return keyValues;
  }

  public void setKeyValues(Object[] keyValues)
  {
    this.keyValues = keyValues;
  }

}
