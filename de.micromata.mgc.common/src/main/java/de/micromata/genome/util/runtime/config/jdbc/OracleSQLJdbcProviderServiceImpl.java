package de.micromata.genome.util.runtime.config.jdbc;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class OracleSQLJdbcProviderServiceImpl extends AbstractJdbcProviderServiceImpl
{

  public OracleSQLJdbcProviderServiceImpl()
  {
    super("Oracle", "oracle.jdbc.driver.OracleDriver");
  }

  @Override
  public String getSampleUrl(String appName)
  {
    return "jdbc:oracle:thin:@localhost:1521:" + appName;
  }

}
