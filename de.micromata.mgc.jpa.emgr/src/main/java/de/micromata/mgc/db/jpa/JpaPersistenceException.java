package de.micromata.mgc.db.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.WithLogAttributes;

/**
 * Exception hierarchie missing in JPA.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class JpaPersistenceException extends PersistenceException implements WithLogAttributes
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -386454043640969313L;

  /**
   * The sql.
   */
  private String sql;

  /**
   * The sql state.
   */
  private String sqlState;

  /**
   * Instantiates a new jpa persistence exception.
   */
  public JpaPersistenceException()
  {
    super();
  }

  /**
   * Instantiates a new jpa persistence exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JpaPersistenceException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new jpa persistence exception.
   *
   * @param message the message
   */
  public JpaPersistenceException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new jpa persistence exception.
   *
   * @param cause the cause
   */
  public JpaPersistenceException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Instantiates a new jpa persistence exception.
   *
   * @param message the message
   * @param sql the sql
   * @param sqlState the sql state
   * @param cause the cause
   */
  public JpaPersistenceException(String message, String sql, String sqlState, RuntimeException cause)
  {
    super(message, cause);
    this.sql = sql;
    this.sqlState = sqlState;
  }

  @Override
  public Collection<LogAttribute> getLogAttributes()
  {
    List<LogAttribute> ret = new ArrayList<LogAttribute>();
    if (StringUtils.isNotBlank(sql) == true) {
      ret.add(new LogAttribute(GenomeAttributeType.SqlStatement, sql));
    }
    if (StringUtils.isNotBlank(sqlState) == true) {
      ret.add(new LogAttribute(GenomeAttributeType.SqlState, sqlState));
    }
    return ret;
  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
  }

  public String getSqlState()
  {
    return sqlState;
  }

  public void setSqlState(String sqlState)
  {
    this.sqlState = sqlState;
  }

}
