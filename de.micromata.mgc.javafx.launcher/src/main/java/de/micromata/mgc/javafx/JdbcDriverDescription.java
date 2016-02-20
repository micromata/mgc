package de.micromata.mgc.javafx;

/**
 * Describes an JdbcDriver.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JdbcDriverDescription
{
  public String getDescription();

  public String getDriverClassName();

  public String getSampleUrlForApp(String appid);

}
