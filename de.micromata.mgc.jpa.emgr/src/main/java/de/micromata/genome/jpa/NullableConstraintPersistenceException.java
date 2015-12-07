package de.micromata.genome.jpa;

/**
 * Should be thrown if a not null constraint is volatatd.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NullableConstraintPersistenceException extends ConstraintPersistenceException
{

  public NullableConstraintPersistenceException(String message, RuntimeException cause)
  {
    super(message, cause);
  }

  public NullableConstraintPersistenceException(String message, String sql, String sqlState, String constraintName,
      RuntimeException cause)
  {
    super(message, sql, sqlState, constraintName, cause);
  }

  public NullableConstraintPersistenceException(String message)
  {
    super(message);
  }

}
