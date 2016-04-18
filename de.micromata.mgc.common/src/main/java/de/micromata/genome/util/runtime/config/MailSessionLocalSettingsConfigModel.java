//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime.config;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

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
  public static enum Encryption
  {
    Plain, StartTLS, SSL;
    public static Encryption fromString(String s)
    {
      for (Encryption v : values()) {
        if (v.name().equals(s) == true) {
          return v;
        }
      }
      return Encryption.Plain;
    }
  }

  /**
   * name of the smpt
   */
  private String name;

  @ALocalSettingsPath(defaultValue = "false")
  private String emailEnabled;

  private String defaultEmailSender;
  @ALocalSettingsPath(comment = "A standard sender email address. The application may use another one")
  private String standardEmailSender;
  /**
   * 
   */
  @ALocalSettingsPath(comment = "Mail protocol: Plain, StartTLS,SSL", defaultValue = "Plain")
  private String encryption;
  /**
   * "mail.smtp.host"
   */
  @ALocalSettingsPath(key = "smtp.host", defaultValue = "localhost", comment = "Hostname of the email server")
  private String emailHost;
  /**
   * "mail.smtp.port"
   */
  @ALocalSettingsPath(key = "smtp.port", defaultValue = "25", comment = "Port number of the email server")
  private String emailPort;
  /**
   * "mail.smtp.auth"
   */
  @ALocalSettingsPath(key = "smtp.auth", defaultValue = "false", comment = "The email server needs authentification")
  private String emailAuthEnabled;

  @ALocalSettingsPath(key = "smtp.user", comment = "Authentification by user name")
  private String emailAuthUser;

  @ALocalSettingsPath(key = "smtp.password", comment = "Users password")
  private String emailAuthPass;

  /**
   * If set, the datasource will be registered as jndi name.
   */
  private String jndiName;
  /**
   * Debugging the SMPT communication.
   */
  private boolean smptDebug = false;

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

  public List<String> getAvailableProtocols()
  {
    return Arrays.asList("Plain", "StartTLS", "SSL");
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
      ctx.directError("standardEmailSender", "Please provide an email");
    }
    if (StringUtils.isBlank(emailHost) == true) {
      ctx.directError("emailHost", "Please provide an hostname");
    }
    if (StringUtils.isBlank(emailPort) == true) {
      ctx.directError("emailPort", "Please provide an port number");
    } else if (NumberUtils.isDigits(emailPort) == false) {
      ctx.directError("emailPort", "Please provide an port *number*");
    }
    // TODO RK continue with smptauth
  }

  public Session createMailSession()
  {
    Properties msprops = new Properties();

    msprops.put("mail.debug", Boolean.toString(smptDebug));
    msprops.put("mail.smtp.host", this.emailHost);
    msprops.put("mail.smtp.port", this.emailPort);
    //    msprops.put("mail.smtp.ssl.enable", "true");
    //msprops.put("mail.smtp.starttls.enable", "true");
    Encryption encr = Encryption.fromString(encryption);
    if (encr == Encryption.StartTLS) {
      msprops.put("mail.smtp.starttls.enable", "true");
    } else if (encr == Encryption.SSL) {
      msprops.put("mail.smtp.ssl.enable", "true");
      //      msprops.put("mail.smtp.socketFactory.port", emailPort);
      //      msprops.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }
    javax.mail.Session mailSession;
    msprops.put("mail.smtp.auth", Boolean.toString(isEmailAuthEnabled()));
    if (isEmailAuthEnabled() == true) {
      mailSession = Session.getInstance(msprops, new Authenticator()
      {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
          return new PasswordAuthentication(emailAuthUser, emailAuthPass);
        }
      });
    } else {
      mailSession = Session.getInstance(msprops);
    }
    return mailSession;
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

  public String getDefaultEmailSender()
  {
    return defaultEmailSender;
  }

  public void setDefaultEmailSender(String defaultEmailSender)
  {
    this.defaultEmailSender = defaultEmailSender;
  }

  public String getEmailEnabled()
  {
    return emailEnabled;
  }

  public void setEmailEnabled(String emailEnabled)
  {
    this.emailEnabled = emailEnabled;
  }

  public String getStandardEmailSender()
  {
    return standardEmailSender;
  }

  public void setStandardEmailSender(String standardEmailSender)
  {
    this.standardEmailSender = standardEmailSender;
  }

  public String getEncryption()
  {
    return encryption;
  }

  public void setEncryption(String protocol)
  {
    this.encryption = protocol;
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

  public boolean isSmptDebug()
  {
    return smptDebug;
  }

  public void setSmptDebug(boolean smptDebug)
  {
    this.smptDebug = smptDebug;
  }

}
