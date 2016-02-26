package de.micromata.genome.util.runtime.config.jdbc;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MemoryDerbyJdbcProviderServiceImpl extends AbstractJdbcProviderServiceImpl
{

  public MemoryDerbyJdbcProviderServiceImpl()
  {
    super("In Memory Derby", "org.apache.derby.jdbc.EmbeddedDriver");
  }

  @Override
  public String getSampleUrl(String appName)
  {
    return "jdbc:derby:memory:" + appName + ";create=true";
  }

}
