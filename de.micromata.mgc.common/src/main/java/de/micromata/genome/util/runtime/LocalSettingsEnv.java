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

package de.micromata.genome.util.runtime;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.InitialContextFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import de.micromata.genome.util.runtime.jndi.JndiDumper;
import de.micromata.genome.util.runtime.jndi.JndiMockupNamingContextBuilder;
import de.micromata.genome.util.types.Pair;

/**
 * The Class LocalSettingsEnv.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class LocalSettingsEnv
{
  /**
   * if true, binds old legacy jndi genome specifc values.
   */
  private static boolean bindDefaultGenomeEnvs = false;

  private static LocalSettingsEnv INSTANCE;
  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(LocalSettingsEnv.class);

  public static Supplier<LocalSettingsEnv> localSettingsEnvCreateor = () -> {
    return LocalSettingsEnv.createJndiLocalSettingsEnv();
  };
  /**
   * Factory method to create new local settings evn.
   */
  public static Function<Context, LocalSettingsEnv> localSettingsEnvSupplier = (context) -> new LocalSettingsEnv(
      context);
  public static Supplier<BasicDataSource> dataSourceSuplier = () -> new BasicDataSource();
  /**
   * The local settings.
   */
  LocalSettings localSettings;

  /**
   * The data sources.
   */
  Map<String, BasicDataSource> dataSources = new HashMap<String, BasicDataSource>();

  /**
   * The mail sessions.
   */
  Map<String, Session> mailSessions = new HashMap<String, Session>();

  /**
   * The initial context.
   */
  Context initialContext;

  public Context getInitialContext()
  {
    return initialContext;
  }

  public void setInitialContext(Context initialContext)
  {
    this.initialContext = initialContext;
  }

  /**
   * Instantiates a new local settings env.
   *
   * @param initialContext the initial context
   */
  public LocalSettingsEnv(Context initialContext)
  {
    localSettings = LocalSettings.get();
    this.initialContext = initialContext;
    parse();
  }

  public static LocalSettingsEnv get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = localSettingsEnvCreateor.get();
    return INSTANCE;
  }

  public static void reset()
  {
    INSTANCE = null;
  }

  private static LocalSettingsEnv createJndiLocalSettingsEnv()
  {
    Hashtable<String, Object> env = new Hashtable<String, Object>();
    Context initialContext;
    try {
      try {
        initialContext = new InitialContext();
        initialContext.lookup("java:");
      } catch (NameNotFoundException | NoInitialContextException ex) {
        log.info("No initialContext. Create own context");
        JndiMockupNamingContextBuilder contextBuilder = new JndiMockupNamingContextBuilder();
        InitialContextFactory initialContextFactory = contextBuilder.createInitialContextFactory(env);
        initialContext = initialContextFactory.getInitialContext(env);
        contextBuilder.activate();

      }
      LocalSettingsEnv localSettingsEnv = localSettingsEnvSupplier.apply(initialContext);
      log.info("Jndi LocalSettingsEnv intialized: " + JndiDumper.getJndiDump());
      return localSettingsEnv;
    } catch (NamingException ex) {
      throw new RuntimeException(ex);
    }

  }

  /**
   * Parses the.
   */
  protected void parse()
  {
    parseDs();
    parseMailSession();
    try {
      parseJndi();
    } catch (NamingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Parses the mail session.
   */
  protected void parseMailSession()
  {
    List<String> dse = localSettings.getKeysPrefixWithInfix("mail.session", "name");
    for (String dsn : dse) {
      String key = dsn + ".name";
      String name = localSettings.get(key);
      Properties msprops = new Properties();
      List<Pair<String, String>> settings = localSettings.getEntriesWithPrefix(dsn + ".smtp.");
      for (Pair<String, String> p : settings) {
        msprops.put("mail.smtp." + p.getFirst(), p.getSecond());
      }
      key = dsn + ".smtp.host";
      msprops.put("mail.smtp.host", localSettings.getProperty(key));
      key = dsn + ".smtp.port";
      msprops.put("mail.smtp.port", localSettings.getProperty(key, "25"));

      javax.mail.Session mailSession;
      if (StringUtils.isNotBlank(msprops.getProperty("mail.smtp.password")) == true) {
        mailSession = Session.getInstance(msprops, new Authenticator()
        {
          @Override
          protected PasswordAuthentication getPasswordAuthentication()
          {
            return new PasswordAuthentication(msprops.getProperty("mail.smtp.user"),
                msprops.getProperty("mail.smtp.password"));
          }
        });
      } else {
        mailSession = Session.getInstance(msprops);
      }

      mailSessions.put(name, mailSession);
    }
  }

  /**
   * Parses the ds.
   */
  protected void parseDs()
  {
    // db.ds.rogerdb.name=RogersOracle
    // db.ds.rogerdb.drivername=oracle.jdbc.driver.OracleDriver
    // db.ds.rogerdb.url=jdbc:oracle:thin:@localhost:1521:rogdb
    // db.ds.rogerdb.username=genome
    // db.ds.rogerdb.password=genome
    List<String> dse = localSettings.getKeysPrefixWithInfix("db.ds", "name");
    for (String dsn : dse) {
      String key = dsn + ".name";
      String name = localSettings.get(key);
      if (StringUtils.isBlank(name) == true) {
        log.error("Name in local-settings is not defined with key: " + key);
        continue;
      }
      key = dsn + ".drivername";
      String driverName = localSettings.get(key);
      if (StringUtils.isBlank(name) == true) {
        log.error("drivername in local-settings is not defined with key: " + key);
        continue;
      }
      key = dsn + ".url";
      String url = localSettings.get(key);
      if (StringUtils.isBlank(name) == true) {
        log.error("url in local-settings is not defined with key: " + key);
        continue;
      }
      key = dsn + ".username";
      String userName = localSettings.get(key);
      key = dsn + ".password";
      String password = localSettings.get(key);
      BasicDataSource bd = dataSourceSuplier.get();

      bd.setDriverClassName(driverName);
      bd.setUrl(url);
      bd.setUsername(userName);
      bd.setPassword(password);
      bd.setMaxActive(localSettings.getIntValue(dsn + ".maxActive", GenericObjectPool.DEFAULT_MAX_ACTIVE));
      bd.setMaxIdle(localSettings.getIntValue(dsn + ".maxIdle", GenericObjectPool.DEFAULT_MAX_IDLE));
      bd.setMinIdle(localSettings.getIntValue(dsn + ".minIdle", GenericObjectPool.DEFAULT_MIN_IDLE));
      bd.setMaxWait(localSettings.getLongValue(dsn + ".maxWait", GenericObjectPool.DEFAULT_MAX_WAIT));
      bd.setInitialSize(localSettings.getIntValue(dsn + ".intialSize", 0));
      bd.setDefaultCatalog(localSettings.get(dsn + ".defaultCatalog", null));
      bd.setDefaultAutoCommit(localSettings.getBooleanValue(dsn + ".defaultAutoCommit", true));
      bd.setValidationQuery(localSettings.get(dsn + ".validationQuery", null));
      bd.setValidationQueryTimeout(localSettings.getIntValue(dsn + ".validationQueryTimeout", -1));
      dataSources.put(name, bd);
    }
  }

  /**
   * Parses the jndi.
   *
   * @throws NamingException the naming exception
   */
  protected void parseJndi() throws NamingException
  {
    // jndi.bind.1.target=genome/jdbc/dsWebDomainAdmin

    bindStandards();
    // bindMailSession();
    bindJndi();
  }

  /**
   * Bind jndi.
   */
  protected void bindJndi()
  {
    List<String> dse = localSettings.getKeysPrefixWithInfix("jndi.bind", "target");
    for (String dsn : dse) {
      String key = dsn + ".type";
      String type = localSettings.get(key);
      if (StringUtils.isBlank(type) == true) {
        log.error("type for jndi in local-settings is not defined with key: " + key);
        continue;
      }
      key = dsn + ".target";
      String target = localSettings.get(key);
      if (StringUtils.isBlank(target) == true) {
        log.error("target for jndi in local-settings is not defined with key: " + key);
        continue;
      }
      key = dsn + ".source";
      String source = localSettings.get(key);
      if (StringUtils.isBlank(source) == true) {
        log.error("source for jndi in local-settings is not defined with key: " + key);
        continue;
      }
      if (type.equals("DataSource") == true) {
        BasicDataSource bd = dataSources.get(source);
        if (bd == null) {
          log.error("No datasource found under name " + source);
          continue;
        }
        bind(target, bd);
      } else if (type.equals("String") == true) {
        bind(target, source);
      } else if (type.equals("Boolean") == true) {
        bind(target, Boolean.valueOf(source));
      } else if (type.equals("MailSession") == true) {
        Session session = mailSessions.get(source);
        if (session == null) {
          log.error("No mailsession found with name: " + source);
          continue;
        }
        bind(target, session);
      } else {
        log.error("Unknown JNDI type: " + type);
      }
    }

  }

  /**
   * Creates the sub context dirs.
   *
   * @param path the path
   */
  protected void createSubContextDirs(String path)
  {
    int idx = path.lastIndexOf('/');
    if (idx == -1) {
      throw new RuntimeException("Cannot create JNDI Path, because no subcontext: " + path);
    }
    String spath = path.substring(0, idx);
    try {
      initialContext.createSubcontext(spath);
    } catch (NameNotFoundException ex) {
      createSubContextDirs(spath);
      try {
        initialContext.createSubcontext(spath);
      } catch (NamingException ex2) {
        throw new RuntimeException("Cannot create Subcontext: " + spath + "; " + ex2.getMessage(), ex2);
      }
    } catch (NamingException ex) {
      throw new RuntimeException("Cannot create subcontext: " + spath + "; " + ex.getMessage(), ex);
    }
  }

  /**
   * Bind internal.
   *
   * @param path the path
   * @param object the object
   * @throws NamingException the naming exception
   */
  protected void bindInternal(String path, Object object) throws NamingException
  {
    Name name = jndiName(path);
    try {
      // OJEJEJEJE bind modifies name and make it unusable!
      initialContext.bind(name, object);
      name = jndiName(path);
      initialContext.lookup(name);
    } catch (NameAlreadyBoundException ex) {
      // org.eclipse.jetty.jndi.NamingContext buggy, does not use internal normalize to find first component.
      name = jndiName(path);
      initialContext.unbind(name);
      name = jndiName(path);
      initialContext.bind(name, object);
    } catch (NameNotFoundException ex) {
      createSubContextDirs(path);
      name = jndiName(path);
      initialContext.bind(name, object);
      name = jndiName(path);
      initialContext.lookup(name);
    }
  }

  private Name jndiName(String path) throws InvalidNameException
  {
    String striped = stripJndiProtocol(path);
    String[] components = StringUtils.split(striped, '/');
    if (striped.startsWith("/") == true) {
      striped = striped.substring(1);
    }
    CompositeName ret = new CompositeName(path);

    return ret;
  }

  public static final String URL_PREFIX = "java:";

  protected String stripJndiProtocol(String name)
  {
    if (StringUtils.isEmpty(name) == true) {
      return name;
    }
    if (name.startsWith(URL_PREFIX) == true) {
      return name.substring(URL_PREFIX.length());
    }
    return name;
  }

  /**
   * Bind.
   *
   * @param path the path
   * @param object the object
   */
  protected void bind(String path, Object object)
  {
    try {
      bindInternal(path, object);
    } catch (NamingException ex) {
      throw new RuntimeException(ex);
    }
  }

  // protected void bindMailSession()
  // {
  //
  // Properties msprops = new Properties();
  // msprops.put("mail.smtp.host", localSettings.getProperty("mail.smtp.host", "mail.micromata.de"));
  // msprops.put("mail.smtp.port", localSettings.getProperty("mail.smtp.port", "25"));
  //
  // javax.mail.Session mailSession = javax.mail.Session.getInstance(msprops);
  // bind("java:/comp/env/genome/mail/mailSession", mailSession);
  // bind("java:/comp/env/mail/Session", mailSession);
  // }

  /**
   * Bind standards.
   *
   * @throws NamingException the naming exception
   */
  protected void bindStandards() throws NamingException
  {
    if (bindDefaultGenomeEnvs == false) {
      return;
    }
    createSubContext("java:/comp/env");
    File genomeHome = new File(localSettings.get("genome.home", "."));

    bind("java:/comp/env/log4jConfigLocation",
        new File(genomeHome, "dev/extrc/config/log4j.properties").getAbsolutePath());
    bind("java:/comp/env/ProjectRoot", genomeHome.getPath());
    bind("java:/comp/env/ShortApplicationName", localSettings.getShortApplicationName());
    bind("java:/comp/env/ApplicationDevelopmentModus", localSettings.getApplicationDevelopmentModus());
    String publicUrl = localSettings.getPublicUrl();
    bind("java:/comp/env/ApplicationPublicUrl", publicUrl);
    bind("java:/comp/env/DatabaseProvider", localSettings.getDatabaseProvider());
    bind("java:/comp/env/ApplicationEnvironment", localSettings.getApplicationEnvironment());
  }

  private void createSubContext(String context) throws NamingException
  {
    try {
      initialContext.createSubcontext(context);
    } catch (NameAlreadyBoundException ex) {
      // ignore
    }
  }

  public LocalSettings getLocalSettings()
  {
    return localSettings;
  }

  public void setLocalSettings(LocalSettings localSettings)
  {
    this.localSettings = localSettings;
  }

  public Map<String, BasicDataSource> getDataSources()
  {
    return dataSources;
  }

  public void setDataSources(Map<String, BasicDataSource> dataSources)
  {
    this.dataSources = dataSources;
  }
}
