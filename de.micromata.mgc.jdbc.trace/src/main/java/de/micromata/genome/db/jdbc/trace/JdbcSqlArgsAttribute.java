package de.micromata.genome.db.jdbc.trace;

import java.util.Arrays;

import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;

/**
 * The Class JdbcSqlArgsAttribute.
 *
 * @author roger
 */
public class JdbcSqlArgsAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -152709636218028663L;

  /**
   * The args.
   */
  private Object[] args;

  /**
   * Instantiates a new jdbc sql args attribute.
   *
   * @param args the args
   */
  public JdbcSqlArgsAttribute(Object[] args)
  {
    super(GenomeAttributeType.SqlArgs, "");
    this.args = args;
  }

  @Override
  public String getValue()
  {
    if (args == null) {
      return "";
    }
    String sargs = Arrays.toString(args);
    return sargs;
  }

}
