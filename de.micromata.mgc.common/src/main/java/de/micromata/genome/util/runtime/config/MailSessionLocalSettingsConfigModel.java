package de.micromata.genome.util.runtime.config;

import de.micromata.genome.util.validation.ValContext;

/**
 * Configuration for a SMPT mail session.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MailSessionLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  /**
   * name of the smpt
   */
  private String name;
  @ALocalSettingsPath(key = "enabled", defaultValue = "false")
  private String emailEnabled;
  @ALocalSettingsPath(key = "smtp.host", defaultValue = "localhost")
  private String emailHost;
  @ALocalSettingsPath(key = "smtp.port", defaultValue = "25")
  private String emailPort;

  @ALocalSettingsPath(key = "smtp.auth", defaultValue = "false")
  private String emailAuthEnabled;

  @ALocalSettingsPath(key = "smtp.user")
  private String emailAuthUser;

  @ALocalSettingsPath(key = "smtp.password")
  private String emailAuthPass;

  @ALocalSettingsPath(key = "smtp.starttls.enable", defaultValue = "false")
  private String emailAuthEnableStartTls;
  @ALocalSettingsPath(key = "smtp.ssl.enable", defaultValue = "false")
  private String emailAuthEnableStartSsl;

  public MailSessionLocalSettingsConfigModel()
  {

  }

  public MailSessionLocalSettingsConfigModel(String name)
  {
    this.name = name;
  }

  @Override
  public String buildKey(String key)
  {
    return "mail.session." + name + "." + key;
  }

  @Override
  public void validate(ValContext ctx)
  {
    // TODO RK validate

  }

}
