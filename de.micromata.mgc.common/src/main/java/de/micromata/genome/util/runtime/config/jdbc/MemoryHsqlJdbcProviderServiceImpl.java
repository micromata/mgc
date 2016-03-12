package de.micromata.genome.util.runtime.config.jdbc;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MemoryHsqlJdbcProviderServiceImpl extends AbstractJdbcProviderServiceImpl
{

  public MemoryHsqlJdbcProviderServiceImpl()
  {
    super("Embedded HSQLDB", "org.hsqldb.jdbcDriver");
  }

  @Override
  public String getSampleUrl(String appName)
  {
    return "jdbc:hsqldb:file:" + appName + ";shutdown=true;hsqldb.default_table_type=memory";
  }

}
