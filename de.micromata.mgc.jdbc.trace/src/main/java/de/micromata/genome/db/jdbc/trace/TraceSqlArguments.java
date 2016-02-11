package de.micromata.genome.db.jdbc.trace;

/**
 * The Class TraceSqlArguments.
 *
 * @author roger
 */
public class TraceSqlArguments
{

  /**
   * The sql.
   */
  private String sql;

  /**
   * The args.
   */
  private Object[] args = null;

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(sql).append("; ");
    if (args != null) {
      for (Object a : args) {
        sb.append(a).append(", ");
      }
    }
    return sb.toString();
  }

  /**
   * Resize.
   *
   * @param newSize the new size
   */
  protected void resize(int newSize)
  {
    Object[] nargs = new Object[newSize + (10 - newSize % 10)];
    if (args != null) {
      System.arraycopy(args, 0, nargs, 0, args.length);
    }
    args = nargs;
  }

  /**
   * Put.
   *
   * @param index the index
   * @param arg the arg
   */
  public void put(int index, Object arg)
  {
    if (args == null || args.length <= index) {
      resize(index);
    }
    args[index] = arg;

  }

  /**
   * Clear args.
   */
  public void clearArgs()
  {
    args = null;

  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
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
