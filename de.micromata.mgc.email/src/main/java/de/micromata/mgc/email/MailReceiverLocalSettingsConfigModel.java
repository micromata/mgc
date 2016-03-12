package de.micromata.mgc.email;

public class MailReceiverLocalSettingsConfigModel
{
  private String hostname;
  private String protocol;
  private String port;
  private String username;
  private String password;
  private String sslSocketFactory;

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
