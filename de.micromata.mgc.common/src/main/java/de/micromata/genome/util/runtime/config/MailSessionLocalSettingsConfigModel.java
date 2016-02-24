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
  @ALocalSettingsPath()
  private String standardEmailSender;

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

  public String getEmailEnabled()
  {
    return emailEnabled;
  }

  public void setEmailEnabled(String emailEnabled)
  {
    this.emailEnabled = emailEnabled;
  }

  public boolean isEmailEnabled()
  {
    return "true".equals(emailEnabled);
  }

  public void setEmailEnabled(boolean enabled)
  {
    emailEnabled = Boolean.toString(enabled);
  }

  public String getEmailHost()
  {
    return emailHost;
  }

  public void setEmailHost(String emailHost)
  {
    this.emailHost = emailHost;
  }

  public String getEmailPort()
  {
    return emailPort;
  }

  public void setEmailPort(String emailPort)
  {
    this.emailPort = emailPort;
  }

  public String getEmailAuthEnabled()
  {
    return emailAuthEnabled;
  }

  public void setEmailAuthEnabled(String emailAuthEnabled)
  {
    this.emailAuthEnabled = emailAuthEnabled;
  }

  public boolean isEmailAuthEnabled()
  {
    return "true".equals(emailAuthEnabled);
  }

  public void setEmailAuthEnabled(boolean enabeld)
  {
    emailAuthEnabled = Boolean.toString(enabeld);
  }

  public String getEmailAuthUser()
  {
    return emailAuthUser;
  }

  public void setEmailAuthUser(String emailAuthUser)
  {
    this.emailAuthUser = emailAuthUser;
  }

  public String getEmailAuthPass()
  {
    return emailAuthPass;
  }

  public void setEmailAuthPass(String emailAuthPass)
  {
    this.emailAuthPass = emailAuthPass;
  }

  public String getEmailAuthEnableStartTls()
  {
    return emailAuthEnableStartTls;
  }

  public void setEmailAuthEnableStartTls(String emailAuthEnableStartTls)
  {
    this.emailAuthEnableStartTls = emailAuthEnableStartTls;
  }

  public String getEmailAuthEnableStartSsl()
  {
    return emailAuthEnableStartSsl;
  }

  public void setEmailAuthEnableStartSsl(String emailAuthEnableStartSsl)
  {
    this.emailAuthEnableStartSsl = emailAuthEnableStartSsl;
  }

  public String getStandardEmailSender()
  {
    return standardEmailSender;
  }

  public void setStandardEmailSender(String standardEmailSender)
  {
    this.standardEmailSender = standardEmailSender;
  }

  public String getDefaultEmailSender()
  {
    return defaultEmailSender;
  }

  public void setDefaultEmailSender(String defaultEmailSender)
  {
    this.defaultEmailSender = defaultEmailSender;
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
