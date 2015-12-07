package de.micromata.genome.jpa;

/**
 * Will be thrown in case of data errors.
 * 
 * Typically to many characters for column.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class DataPersistenceException extends JpaPersistenceException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -998801803461138060L;

  /**
   * Instantiates a new data persistence exception.
   *
   * @param msg the msg
   * @param sql the sql
   * @param sqlState the sql state
   * @param cause the cause
   */
  public DataPersistenceException(String msg, String sql, String sqlState, RuntimeException cause)
  {
    super(msg, sql, sqlState, cause);
  }

}
