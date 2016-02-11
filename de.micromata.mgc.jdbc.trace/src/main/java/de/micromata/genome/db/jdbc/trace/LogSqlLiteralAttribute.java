package de.micromata.genome.db.jdbc.trace;

import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;

/**
 * The Class LogSqlLiteralAttribute.
 *
 * @author roger
 */
public class LogSqlLiteralAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1552643479809968590L;

  /**
   * The prepared sql.
   */
  private String preparedSql;

  /**
   * The args.
   */
  private Object[] args;

  /**
   * The renderer.
   */
  private SqlArgRenderer renderer;

  /**
   * Instantiates a new log sql literal attribute.
   *
   * @param preparedSql the prepared sql
   * @param args the args
   * @param renderer the renderer
   */
  public LogSqlLiteralAttribute(String preparedSql, Object[] args, SqlArgRenderer renderer)
  {
    super(GenomeAttributeType.SqlResolvedStatement, null);
    this.preparedSql = preparedSql;
    this.args = args;
    this.renderer = renderer;
  }

  @Override
  public String getValue()
  {
    String s = super.getValue();
    if (s != null) {
      return s;
    }
    s = renderer.renderLiteralStatement(preparedSql, args);
    super.setValue(s);
    return s;
  }

  public String getPreparedSql()
  {
    return preparedSql;
  }

  public void setPreparedSql(String preparedSql)
  {
    this.preparedSql = preparedSql;
  }

  public Object[] getArgs()
  {
    return args;
  }

  public void setArgs(Object[] args)
  {
    this.args = args;
  }
}
