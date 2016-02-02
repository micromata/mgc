package de.micromata.genome.jpa.hibernate.dialect;

import org.hibernate.dialect.Oracle10gDialect;

/**
 * Configuration dialect which allows for <code>SELECT FOR UPDATE</code> operations by using pessimistic locking in
 * Oracle.
 * 
 * Define
 * 
 * <pre>
 * hibernate.dialect_resolvers = de.micromata.genome.jpa.hibernate.dialect.JpaDialectResolver
 * </pre>
 * 
 * or
 * 
 * <pre>
 * hibernate.dialect = de.micromata.genome.jpa.hibernate.dialect.Oracle10gDialectNoFollowOnLocking
 * </pre>
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Michael Lesniak (mlesniak@micromata.de)
 * 
 */
public class Oracle10gDialectNoFollowOnLocking extends Oracle10gDialect
{
  /**
   * Used to enable pesimistic locks (select for update) when using optimistic locks. {@inheritDoc}
   * 
   * @see org.hibernate.dialect.Oracle8iDialect#useFollowOnLocking()
   */
  @Override
  public boolean useFollowOnLocking()
  {
    return false;
  }

}
