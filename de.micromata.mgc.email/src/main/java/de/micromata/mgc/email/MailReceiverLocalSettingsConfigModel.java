package de.micromata.mgc.email;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Config model for mail receiver.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MailReceiverLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  @ALocalSettingsPath(defaultValue = "localhost")
  private String hostname;

  @ALocalSettingsPath
  private String protocol;

  @ALocalSettingsPath(defaultValue = "25")
  private String port;
  @ALocalSettingsPath
  private String username;
  @ALocalSettingsPath
  private String password;
  @ALocalSettingsPath
  private String sslSocketFactory;

  @Override
  public void validate(ValContext ctx)
  {

  }

  public String getSslSocketFactory()
  {
    return sslSocketFactory;
  }

  public String getHostname()
  {
    return hostname;
  }

  public String getProtocol()
  {
    return protocol;
  }

  public int getPortAsInt()
  {
    return Integer.parseInt(port);
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }

}
