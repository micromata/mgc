package de.micromata.genome.util.runtime.config.jdbc;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PostgreSQLJdbcProviderServiceImpl extends AbstractJdbcProviderServiceImpl
{

  public PostgreSQLJdbcProviderServiceImpl()
  {
    super("Postgres", "org.postgresql.Driver");
  }

  @Override
  public String getSampleUrl(String appName)
  {
    return "jdbc:postgresql://localhost:5432/" + appName;
  }

}
