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

package de.micromata.genome.jpa.events;

import java.util.Map;

import javax.persistence.TypedQuery;

import de.micromata.genome.jpa.IEmgr;

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
