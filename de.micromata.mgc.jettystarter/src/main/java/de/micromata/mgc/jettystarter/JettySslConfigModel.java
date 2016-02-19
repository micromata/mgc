package de.micromata.mgc.jettystarter;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Configure a https connector.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettySslConfigModel extends AbstractLocalSettingsConfigModel

{

  @ALocalSettingsPath(defaultValue = "false")
  private String sslEnabled;
  @ALocalSettingsPath()
  private String sslKeystorePath;
  @ALocalSettingsPath()
  private String sslKeystorePassword;
  @ALocalSettingsPath()
  private String sslKeyManagerPassword;

  @ALocalSettingsPath()
  private String port;

  @Override
  public String getKeyPrefix()
  {
    return "genome.jetty.ssl";
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (isSslEnabled() == false) {
      return;
    }
  }

  public boolean isSslEnabled()
  {
    return "true".equals(getSslEnabled());
  }

  public String getSslEnabled()
  {
    return sslEnabled;
  }

  public void setSslEnabled(String sslEnabled)
  {
    this.sslEnabled = sslEnabled;
  }

  public String getPort()
  {
    return port;
  }

  public void setPort(String port)
  {
    this.port = port;
  }

  public int getPortAsInt()
  {
    return Integer.parseInt(port);
  }

  public String getSslKeystorePath()
  {
    return sslKeystorePath;
  }

  public void setSslKeystorePath(String sslKeystorePath)
  {
    this.sslKeystorePath = sslKeystorePath;
  }

  public String getSslKeystorePassword()
  {
    return sslKeystorePassword;
  }

  public void setSslKeystorePassword(String sslKeystorePassword)
  {
    this.sslKeystorePassword = sslKeystorePassword;
  }

  public String getSslKeyManagerPassword()
  {
    return sslKeyManagerPassword;
  }

  public void setSslKeyManagerPassword(String sslKeyManagerPassword)
  {
    this.sslKeyManagerPassword = sslKeyManagerPassword;
  }

}
