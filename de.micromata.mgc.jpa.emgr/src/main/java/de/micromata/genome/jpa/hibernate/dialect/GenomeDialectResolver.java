package de.micromata.genome.jpa.hibernate.dialect;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;

/**
 * resolves dialect.
 * 
 * Currently only return Oracle10gDialectNoFollowOnLocking in case of Oracle10gDialect
 * 
 * Define
 * 
 * <pre>
 * hibernate.dialect_resolvers = de.micromata.genome.jpa.hibernate.dialect.GenomeDialectResolver
 * </pre>
 * 
 * In your local-settings.properties.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class GenomeDialectResolver extends StandardDialectResolver
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7095360183898128038L;

  @Override
  public Dialect resolveDialect(DialectResolutionInfo info)
  {
    Dialect dialect = super.resolveDialect(info);
    if (dialect.getClass() == Oracle10gDialect.class) {
      return new Oracle10gDialectNoFollowOnLocking();
    }
    return dialect;
  }

}
