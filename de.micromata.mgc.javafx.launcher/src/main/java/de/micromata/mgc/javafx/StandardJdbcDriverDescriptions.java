package de.micromata.mgc.javafx;

import java.util.function.Function;

/**
 * Standard known driver.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum StandardJdbcDriverDescriptions implements JdbcDriverDescription
{
  LOCAL_DERBY("Embedded Derby", "org.apache.derby.jdbc.EmbeddedDriver",
      (appid) -> "jdbc:derby:" + appid + "-derby"),

  ORACLE(
      "Oracle", "oracle.jdbc.driver.OracleDriver",
      appid -> "jdbc:oracle:thin:@localhost:1521:" + appid),

  POSTGRES("Postgres",
      "org.postgresql.Driver", appid -> "jdbc:postgresql://localhost:5432/" + appid)

  ;
  ;
  private String description;
  private String driverName;
  private Function<String, String> sampleUrl;

  private StandardJdbcDriverDescriptions(String description, String driverName, Function<String, String> sampleUrl)
  {
    this.description = description;
    this.driverName = driverName;
    this.sampleUrl = sampleUrl;
  }

  @Override
  public String getSampleUrlForApp(String appid)
  {
    return sampleUrl.apply(appid);
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public String getDriverClassName()
  {
    return driverName;
  }

}
