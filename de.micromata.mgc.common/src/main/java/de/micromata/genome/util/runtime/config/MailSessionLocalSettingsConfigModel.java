package de.micromata.genome.util.runtime.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

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

  @ALocalSettingsPath(defaultValue = "false")
  private String emailEnabled;

  private String defaultEmailSender;
  @ALocalSettingsPath(comment = "A standard sender email address. The application may use another one")
  private String standardEmailSender;

  @ALocalSettingsPath(key = "smtp.host", defaultValue = "localhost", comment = "Hostname of the email server")
  private String emailHost;
  @ALocalSettingsPath(key = "smtp.port", defaultValue = "25", comment = "Port number of the email server")
  private String emailPort;

  @ALocalSettingsPath(key = "smtp.auth", defaultValue = "false", comment = "The email server needs authentification")
  private String emailAuthEnabled;

  @ALocalSettingsPath(key = "smtp.user", comment = "Authentification by user name")
  private String emailAuthUser;

  @ALocalSettingsPath(key = "smtp.password", comment = "Users password")
  private String emailAuthPass;

  @ALocalSettingsPath(key = "smtp.starttls.enable", defaultValue = "false", comment = "Use STARTTLS as encryption")
  private String emailAuthEnableStartTls;
  @ALocalSettingsPath(key = "smtp.ssl.enable", defaultValue = "false", comment = "Use SSL encryption")
  private String emailAuthEnableStartSsl;
  /**
   * If set, the datasource will be registered as jndi name.
   */
  private String jndiName;

  public MailSessionLocalSettingsConfigModel()
  {

  }

  public MailSessionLocalSettingsConfigModel(String name)
  {
    this.name = name;
  }

  public MailSessionLocalSettingsConfigModel(String name, String jndiName)
  {
    this.name = name;
    this.jndiName = jndiName;
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    writer.put(buildKey("name"), name);
    super.toProperties(writer);
    if (StringUtils.isNotBlank(jndiName) == true) {
      writer.put("jndi.bind." + name + ".target", jndiName);
      writer.put("jndi.bind." + name + ".type", "MailSession");
      writer.put("jndi.bind." + name + ".source", name);
    }
    return writer;
  }

  @Override
  public String buildKey(String key)
  {
    return "mail.session." + name + "." + key;
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (isEmailEnabled() == false) {
      return;
    }
    if (StringUtils.isBlank(standardEmailSender) == true || standardEmailSender.contains("@") == false) {
      ctx.error("standardEmailSender", "Please provide an email");
    }
    if (StringUtils.isBlank(emailHost) == true) {
      ctx.error("emailHost", "Please provide an hostname");
    }
    if (StringUtils.isBlank(emailPort) == true) {
      ctx.error("emailPort", "Please provide an port number");
    } else if (NumberUtils.isDigits(emailPort) == false) {
      ctx.error("emailPort", "Please provide an port *number*");
    }
    // TODO RK continue with smptauth
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean isEmailEnabled()
  {
    return "true".equals(emailEnabled);
  }

  public boolean isEmailAuthEnabled()
  {
    return "true".equals(emailAuthEnabled);
  }

  public String getJndiName()
  {
    return jndiName;
  }

  public void setJndiName(String jndiName)
  {
    this.jndiName = jndiName;
  }

}
