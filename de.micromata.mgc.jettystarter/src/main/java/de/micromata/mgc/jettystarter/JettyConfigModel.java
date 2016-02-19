package de.micromata.mgc.jettystarter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Represents a jetty configuration.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyConfigModel extends AbstractCompositLocalSettingsConfigModel
{
  private JettySslConfigModel sslConfigModel = new JettySslConfigModel();

  @ALocalSettingsPath(key = "gwiki.jetty.port", defaultValue = "8081")
  private String serverPort;
  @ALocalSettingsPath(key = "gwiki.jetty.contextpath", defaultValue = "/")
  private String serverContextPath;

  @ALocalSettingsPath(key = "gwiki.public.url", defaultValue = "http://localhost:8081/")
  private String publicUrl;

  @ALocalSettingsPath(key = "genome.jetty.jmx.enabled", defaultValue = "false")
  private String serverEnableJmx;

  @ALocalSettingsPath(key = "genome.jetty.requestlogging.enabled", defaultValue = "false")
  private String serverRequestLoggingEnabled;

  // TODO RK ssl connector
  // TODO RK war?
  @Override
  public void validate(ValContext valContext)
  {
    ValContext ctx = valContext.createSubContext(this, "");

    if (StringUtils.isBlank(serverPort) == true) {
      ctx.error("serverPort", "Please provide a server port");
    } else {
      if (NumberUtils.isDigits(serverPort) == false) {
        ctx.error("serverPort", "Please provid numeric port number");
      }
    }
    sslConfigModel.validate(valContext);
  }

  public String getServerPort()
  {
    return serverPort;
  }

  public int getServerPortAsInt()
  {
    return Integer.parseInt(serverPort);
  }

  public void setServerPort(String serverPort)
  {
    this.serverPort = serverPort;
  }

  public String getServerContextPath()
  {
    return serverContextPath;
  }

  public void setServerContextPath(String serverContextPath)
  {
    this.serverContextPath = serverContextPath;
  }

  public String getPublicUrl()
  {
    return publicUrl;
  }

  public void setPublicUrl(String publicUrl)
  {
    this.publicUrl = publicUrl;
  }

  public boolean isServerEnableJmx()
  {
    return "true".equals(getServerEnableJmx());
  }

  public String getServerEnableJmx()
  {
    return serverEnableJmx;
  }

  public String getServerRequestLoggingEnabled()
  {
    return serverRequestLoggingEnabled;
  }

  public void setServerRequestLoggingEnabled(String serverRequestLoggingEnabled)
  {
    this.serverRequestLoggingEnabled = serverRequestLoggingEnabled;
  }

  public boolean isServerRequestLoggingEnabled()
  {
    return "true".equals(serverRequestLoggingEnabled);
  }

  public void setServerEnableJmx(String serverEnableJmx)
  {
    this.serverEnableJmx = serverEnableJmx;
  }

  public JettySslConfigModel getSslConfigModel()
  {
    return sslConfigModel;
  }

  public void setSslConfigModel(JettySslConfigModel sslConfigModel)
  {
    this.sslConfigModel = sslConfigModel;
  }

}
