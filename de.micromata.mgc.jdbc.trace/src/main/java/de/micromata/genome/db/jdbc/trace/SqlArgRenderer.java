package de.micromata.genome.db.jdbc.trace;

/**
 * Interface to render an sql argument to a string.
 *
 * @author roger
 */
public interface SqlArgRenderer
{

  /**
   * Render sql arg.
   *
   * @param arg the arg
   * @return the string
   */
  public String renderSqlArg(Object arg);

  /**
   * Render literal statement.
   *
   * @param sql the sql
   * @param args the args
   * @return the string
   */
  public String renderLiteralStatement(String sql, Object[] args);
}
