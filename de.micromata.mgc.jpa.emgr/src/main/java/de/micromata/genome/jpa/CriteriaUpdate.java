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

import java.util.Map;
import java.util.TreeMap;

import de.micromata.genome.jpa.Clauses.Clause;

/**
 * In JPA 2.1 there is also a CriteriaUpdate. Maybe replace this with JPA version, if using 2.1.
 *
 *
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class CriteriaUpdate<T>
{

  /**
   * The Class SetExpression.
   *
   * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
   */
  private static class SetExpression
  {

    /**
     * The expression.
     */
    private final String expression;

    /**
     * Instantiates a new sets the expression.
     *
     * @param expression the expression
     */
    public SetExpression(String expression)
    {
      this.expression = expression;
    }

    public String getExpression()
    {
      return expression;
    }
  }

  /**
   * The set map.
   */
  private final Map<String, Object> setMap = new TreeMap<String, Object>();

  /**
   * The entity class.
   */
  private final Class<T> entityClass;

  /**
   * The where clause.
   */
  private Clause whereClause;

  /**
   * The master alias.
   */
  private String masterAlias = "e";

  /**
   * Instantiates a new critieria update.
   *
   * @param clazz the clazz
   */
  protected CriteriaUpdate(Class<T> clazz)
  {
    this.entityClass = clazz;

  }

  /**
   * Creates the update.
   *
   * @param <T> the generic type
   * @param clazz the clazz
   * @return the critieria update
   */
  public static <T> CriteriaUpdate<T> createUpdate(Class<T> clazz)
  {
    return new CriteriaUpdate<T>(clazz);
  }

  /**
   * Set a column value.
   *
   * @param column the column
   * @param value the value
   * @return the critieria update
   */
  public CriteriaUpdate<T> set(String column, Object value)
  {
    setMap.put(column, value);
    return this;
  }

  /**
   * If column not already set in critia update, set it with vlue
   * 
   * @param column the column
   * @param value the value
   * @return
   */
  public CriteriaUpdate<T> setIfAbsend(String column, Object value)
  {
    setMap.putIfAbsent(column, value);
    return this;
  }

  /**
   * set a expression, which will be used as raw expression in the hql.
   *
   * Use setMasterAlias
   *
   * @param column the column
   * @param expression Like s.printCounter + 1
   * @return the critieria update
   */
  public CriteriaUpdate<T> setExpression(String column, String expression)
  {
    setMap.put(column, new SetExpression(expression));
    return this;
  }

  /**
   * Add a where clause. If already a where clause was set, the clauses are joined with AND.
   *
   * @param clause the clause
   * @return the critieria update
   */
  public CriteriaUpdate<T> addWhere(Clause clause)
  {
    if (this.whereClause == null) {
      this.whereClause = clause;
    } else {
      this.whereClause = Clauses.and(this.whereClause, clause);
    }
    return this;
  }

  /**
   * Renders the hql and fill args for passing to entity manager.
   *
   * @param args pass an empty map to fill.
   * @return hql
   */
  public String renderHql(Map<String, Object> args)
  {
    if (setMap.isEmpty() == true) {
      throw new IllegalArgumentException("CritieriaUpdate has nothing to set");
    }
    final int minLength = 30;
    StringBuilder sb = new StringBuilder(minLength);
    sb.append("update ").append(entityClass.getName()).append(' ').append(masterAlias).append(" set ");
    boolean first = true;
    for (Map.Entry<String, Object> me : setMap.entrySet()) {
      if (first == false) {
        sb.append(", ");
      }
      first = false;
      if (me.getValue() instanceof SetExpression) {
        SetExpression se = (SetExpression) me.getValue();
        sb.append(masterAlias).append('.').append(me.getKey()).append(" = ").append(se.getExpression());
        continue;
      }
      String key = Clauses.getVariable(me.getKey(), args);
      sb.append(masterAlias).append('.').append(me.getKey()).append(" = :").append(key);
      args.put(key, me.getValue());
    }
    if (whereClause != null) {
      sb.append(" where ");
      whereClause.renderClause(sb, masterAlias, args);
    }
    return sb.toString();
  }

  public Class<T> getEntityClass()
  {
    return entityClass;
  }

  public String getMasterAlias()
  {
    return masterAlias;
  }

  /**
   * Sets the master alias of the entity, which may be used in expression.
   *
   * see setExpression.
   *
   * @param masterAlias the master alias
   * @return the critieria update
   */
  public CriteriaUpdate<T> setMasterAlias(String masterAlias)
  {
    this.masterAlias = masterAlias;
    return this;
  }
}
