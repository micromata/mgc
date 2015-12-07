package de.micromata.mgc.db.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Helper to build where clauses.
 *
 * Used in building filter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public final class Clauses
{
  /**
   * Private constructor.
   */
  private Clauses()
  {
    // Empty.
  }

  /**
   * Or.
   *
   * @param first the first
   * @param second the second
   * @return the logic clause
   */
  public static LogicClause or(Clause first, Clause second)
  {
    return new LogicClause("or", first, second);
  }

  /**
   * Hql clause.
   *
   * @param hql the hql
   * @return the base clause
   */
  public static BaseClause hql(String hql)
  {
    return new HqlClause(hql);
  }

  /**
   * And.
   *
   * @param clause the clause
   * @return the logic clause
   */
  public static LogicClause and(Clause... clause)
  {
    return new LogicClause("and", clause);
  }

  /**
   * Equal. Handles <code>is null</code>, too.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause equal(final String column, final Object value)
  {
    return new OpClause("=", column, value)
    {
      @Override
      public void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args)
      {
        if (value != null) {
          super.renderClause(sb, masterEntityName, args);
        } else {
          if (StringUtils.isNotBlank(masterEntityName) == true) {
            sb.append(masterEntityName).append('.');
          }
          sb.append(column).append(" is null");
        }
      }
    };
  }

  /**
   * Not equal. Handles <code>is not null</code>, tpoo.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause notEqual(String column, Object value)
  {
    return new OpClause("!=", column, value)
    {
      @Override
      public void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args)
      {
        if (value != null) {
          super.renderClause(sb, masterEntityName, args);
        } else {
          if (StringUtils.isNotBlank(masterEntityName) == true) {
            sb.append(masterEntityName).append('.');
          }
          sb.append(column).append(" is not null");
        }
      }
    };

  }

  /**
   * creates in in clause.
   *
   * @param column the column
   * @param values the values
   * @return the op clause
   */
  public static OpClause in(String column, Collection<?> values)
  {
    return new OpClause("in", column, values);
  }

  /**
   * Less than.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause lessThan(String column, Object value)
  {
    return new OpClause("<", column, value);
  }

  /**
   * like expression.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause like(String column, Object value)
  {
    return new OpClause("like", column, value);
  }

  /**
   * Less or equal.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause lessOrEqual(String column, Object value)
  {
    return new OpClause("<=", column, value);
  }

  /**
   * More or equal.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause moreOrEqual(String column, Object value)
  {
    return new OpClause(">=", column, value);
  }

  /**
   * More than.
   *
   * @param column the column
   * @param value the value
   * @return the op clause
   */
  public static OpClause moreThan(String column, Object value)
  {
    return new OpClause(">", column, value);
  }

  /**
   * The Interface Clause.
   */
  public interface Clause
  {

    /**
     * Render clause.
     *
     * @param sb the sb
     * @param masterEntityName the master entity name
     * @param args the args
     */
    void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args);

  }

  /**
   * for the hql arguments, you need variables, which not conflicts.
   *
   * If the column name is already in currentArgs, uses an incremented name.
   *
   * @param column the column
   * @param currentArgs the current args
   * @return the variable
   */
  public static String getVariable(String column, Map<String, Object> currentArgs)
  {
    if (currentArgs.containsKey(column) == false) {
      return column;
    }
    int counter = 1;
    do {
      if (currentArgs.containsKey(column + counter) == false) {
        return column + counter;
      }
      ++counter;
    } while (true);
  }

  /**
   * The Class BaseClause.
   */
  public abstract static class BaseClause implements Clause
  {
    /**
     * for debugging only.
     *
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      Map<String, Object> args = new HashMap<String, Object>();
      renderClause(sb, "e", args);
      return sb.toString();
    }
  }

  /**
   * The Class FieldClause.
   */
  private abstract static class FieldClause extends BaseClause
  {
    // CHECKSTYLE.OFF com.puppycrawl.tools.checkstyle.checks.design.VisibilityModifierCheck Nur intern verwendet.

    /**
     * The column.
     */
    protected String column;

    /**
     * The value.
     */
    protected Object value;
    // CHECKSTYLE.ON
  }

  /**
   * The Class OpClause.
   */
  public static class OpClause extends FieldClause
  {

    /**
     * Empty seperator.
     */
    private static final String EMPTY_SEP = " ";

    /**
     * The operation.
     */
    private final String operation;

    /**
     * Instantiates a new op clause.
     *
     * @param operation the operation
     * @param column the column
     * @param value the value
     */
    public OpClause(String operation, String column, Object value)
    {
      this.operation = operation;
      this.column = column;
      this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args)
    {
      String colval = column;
      int idx = StringUtils.lastIndexOf(colval, '.');
      if (idx != -1) {
        colval = colval.substring(idx + 1);
      }

      String variable = getVariable("master" + colval, args);
      if (StringUtils.isNotBlank(masterEntityName) == true) {
        sb.append(masterEntityName).append('.');
      }
      sb.append(column).append(EMPTY_SEP).append(operation).append(EMPTY_SEP);
      if ("in".equals(operation) == true) {
        sb.append('(');
      }
      sb.append(':').append(variable);
      if ("in".equals(operation) == true) {
        sb.append(')');
      }
      args.put(variable, value);
    }

  }

  /**
   * The Class HqlClause.
   */
  public static class HqlClause extends BaseClause
  {

    /**
     * The hql.
     */
    private final String hql;

    /**
     * Instantiates a new hql clause.
     *
     * @param hql the hql
     */
    public HqlClause(String hql)
    {
      this.hql = hql;
    }

    /**
     * Of.
     *
     * @param hql the hql
     * @return the hql clause
     */
    public static HqlClause of(String hql)
    {
      return new HqlClause(hql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args)
    {
      sb.append('(');
      sb.append(hql);
      sb.append(')');
    }

  }

  /**
   * The Class LogicClause.
   */
  public static class LogicClause extends BaseClause
  {

    /**
     * The logop.
     */
    private String logop;

    /**
     * The clauses.
     */
    private List<Clause> clauses = new ArrayList<Clause>();

    /**
     * Instantiates a new logic clause.
     *
     * @param logop the logop
     * @param clauses the clauses
     */
    public LogicClause(String logop, Clause... clauses)
    {
      this.logop = logop;
      for (Clause clause : clauses) {
        this.clauses.add(clause);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args)
    {
      sb.append('(');
      boolean first = true;
      for (Clause clause : clauses) {
        if (first == false) {
          sb.append(' ').append(logop).append(' ');
        }
        first = false;
        sb.append('(');
        clause.renderClause(sb, masterEntityName, args);
        sb.append(')');
      }
      sb.append(')');
    }

    /**
     * Gets the logop.
     *
     * @return the logop
     */
    public String getLogop()
    {
      return logop;
    }

    /**
     * Sets the logop.
     *
     * @param logop the new logop
     */
    public void setLogop(String logop)
    {
      this.logop = logop;
    }

    /**
     * Gets the clauses.
     *
     * @return the clauses
     */
    public List<Clause> getClauses()
    {
      return clauses;
    }

    /**
     * Sets the clauses.
     *
     * @param clauses the new clauses
     */
    public void setClauses(List<Clause> clauses)
    {
      this.clauses = clauses;
    }

  }

  /**
   * unary "not".
   *
   * @param term the first
   * @return the logic clause
   */
  public static LogicClause not(Clause term)
  {
    return new LogicClause("not ", term)
    {
      @Override
      public void renderClause(StringBuilder sb, String masterEntityName, Map<String, Object> args)
      {
        sb.append("not ");
        super.renderClause(sb, masterEntityName, args);
      }
    };
  }
}
