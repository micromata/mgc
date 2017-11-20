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

package de.micromata.mgc.email;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * Config model for mail receiver.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MailReceiverLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  private boolean forceEnabled = false;

  @ALocalSettingsPath(defaultValue = "false", comment = "Enable Incoming Mail")
  private String enabled;

  @ALocalSettingsPath(defaultValue = "localhost", comment = "Hostname of the mail server")
  private String host;
  /**
   * One of available protocols
   */
  @ALocalSettingsPath(comment = "Mail protocol")
  private String protocol;

  @ALocalSettingsPath(defaultValue = "993", comment = "Port number of the mail server")
  private String port;

  @ALocalSettingsPath
  private String user;

  @ALocalSettingsPath
  private String defaultFolder;

  @ALocalSettingsPath
  private String password;
  /**
   * Authentification needed
   */
  @ALocalSettingsPath(defaultValue = "true")
  private String auth;

  @ALocalSettingsPath(key = "starttls.enable", defaultValue = "false")
  private String enableTLS;

  @ALocalSettingsPath(defaultValue = "false")
  private String enableSelfSignedCerts;

  @ALocalSettingsPath(key = "socketFactory.port")
  private String socketFactoryPort;

  @ALocalSettingsPath(key = "socketFactory.class")
  private String socketFactoryClass;

  @ALocalSettingsPath(key = "auth.plain.disable", defaultValue = "false")
  private String authPlainDisable;

  @ALocalSettingsPath(defaultValue = "false", comment = "javax.mail debugging enabled. ")
  private String debug;

  @Override
  public String getKeyPrefix()
  {
    return "genome.email.receive";
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    LocalSettingsWriter ret = super.toProperties(writer);
    if (forceEnabled == true) {
      enabled = "true";
    }
    return ret;
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    if (forceEnabled == true) {
      enabled = "true";
    }
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (forceEnabled == true) {
      enabled = "true";
    }
    if (isEnabled() == false) {
      return;
    }
    if (StringUtils.isBlank(protocol) == true) {
      ctx.directError("protocol", "Please select a protocol");
      return;
    }
    if (StringUtils.isBlank(host) == true) {
      ctx.directError("host", "Please give a host");
      return;
    }
    if (StringUtils.isBlank(port) == true) {
      ctx.directError("host", "Please give a port number");
    } else if (isInt(host) == true) {
      ctx.directError("host", "Please give a port number (only numbers are allowed)");
    }
    if (StringUtils.isBlank(user) == true) {
      ctx.directError("host", "Please give a user name");
    }
    if (StringUtils.isBlank(password) == true) {
      ctx.directError("host", "Please give a password");
    }

  }

  public String getSocketFactoryClass()
  {
    return socketFactoryClass;
  }

  public String getHost()
  {
    return host;
  }

  public String getProtocol()
  {
    return protocol;
  }

  public String getPort()
  {
    return port;
  }

  public int getPortAsInt()
  {
    return Integer.parseInt(port);
  }

  public String getUser()
  {
    return user;
  }

  public String getPassword()
  {
    return password;
  }

  public String getEnableTLS()
  {
    return enableTLS;
  }

  public MailReceiverLocalSettingsConfigModel setEnableTLS(String enableTLS)
  {
    this.enableTLS = enableTLS;
    return this;
  }

  public String getAuth()
  {
    return auth;
  }

  public MailReceiverLocalSettingsConfigModel setAuth(String auth)
  {
    this.auth = auth;
    return this;
  }

  public String getAuthPlainDisable()
  {
    return authPlainDisable;
  }

  public String getEnableSelfSignedCerts()
  {
    return enableSelfSignedCerts;
  }

  public boolean isEnableSelfSignedCerts()
  {
    return isTrue(enableSelfSignedCerts);
  }

  public String getDebug()
  {
    return debug;
  }

  public boolean isEnabled()
  {
    boolean ret = Boolean.parseBoolean(enabled);
    return ret;
  }

  public boolean isForceEnabled()
  {
    return forceEnabled;
  }

  public void setForceEnabled(boolean forceEnabled)
  {
    this.forceEnabled = forceEnabled;
  }

  public String getEnabled()
  {
    return enabled;
  }

  public MailReceiverLocalSettingsConfigModel setEnabled(String enabled)
  {
    this.enabled = enabled;
    return this;
  }

  public String getDefaultFolder()
  {
    return defaultFolder;
  }

  public MailReceiverLocalSettingsConfigModel setHost(String host)
  {
    this.host = host;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setProtocol(String protocol)
  {
    this.protocol = protocol;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setPort(String port)
  {
    this.port = port;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setSocketFactoryClass(String socketFactoryClass)
  {
    this.socketFactoryClass = socketFactoryClass;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setAuthPlainDisable(String authPlainDisable)
  {
    this.authPlainDisable = authPlainDisable;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setDebug(String debug)
  {
    this.debug = debug;
    return this;
  }

  public String getSocketFactoryPort()
  {
    return socketFactoryPort;
  }

  public MailReceiverLocalSettingsConfigModel setSocketFactoryPort(String socketFactoryPort)
  {
    this.socketFactoryPort = socketFactoryPort;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setUser(String user)
  {
    this.user = user;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setDefaultFolder(String defaultFolder)
  {
    this.defaultFolder = defaultFolder;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setPassword(String password)
  {
    this.password = password;
    return this;
  }

  public MailReceiverLocalSettingsConfigModel setEnableSelfSignedCerts(String enableSelfSignedCerts)
  {
    this.enableSelfSignedCerts = enableSelfSignedCerts;
    return this;
  }
}
