package de.micromata.genome.jpa;

/**
 * Will be thrown if a Contraint in the dabase is violated.
 * 
 * NOTE: This exception can only be catched outside the transaction. The transaction will be rolled back.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class ConstraintPersistenceException extends JpaPersistenceException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -3764048049335654565L;

  /**
   * The constraint name.
   */
  private String constraintName;

  /**
   * Instantiates a new constraint persistence exception.
   *
   * @param message the message
   */
  public ConstraintPersistenceException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new constraint persistence exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public ConstraintPersistenceException(String message, RuntimeException cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new constraint persistence exception.
   *
   * @param message the message
   * @param sql the sql
   * @param sqlState the sql state
   * @param constraintName the constraint name
   * @param cause the cause
   */
  public ConstraintPersistenceException(String message, String sql, String sqlState, String constraintName,
      RuntimeException cause)
  {
    super(message, sql, sqlState, cause);
    this.constraintName = constraintName;
  }

  public String getConstraintName()
  {
    return constraintName;
  }

  public void setConstraintName(String constraintName)
  {
    this.constraintName = constraintName;
  }
}
