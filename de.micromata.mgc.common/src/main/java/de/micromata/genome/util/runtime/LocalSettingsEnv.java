package de.micromata.genome.util.runtime;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import de.micromata.genome.util.runtime.jndi.JndiMockupNamingContextBuilder;

/**
 * The Class LocalSettingsEnv.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class LocalSettingsEnv
{
  private static LocalSettingsEnv INSTANCE;
  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(LocalSettingsEnv.class);

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
    INSTANCE = createJndiLocalSettingsEnv();
    return INSTANCE;
  }

  private static LocalSettingsEnv createJndiLocalSettingsEnv()
  {
    JndiMockupNamingContextBuilder contextBuilder = new JndiMockupNamingContextBuilder();
    Hashtable<String, Object> env = new Hashtable<String, Object>();
    InitialContextFactory initialContextFactory = contextBuilder.createInitialContextFactory(env);
    try {
      Context initialContext = initialContextFactory.getInitialContext(env);
      LocalSettingsEnv localSettingsEnv = new LocalSettingsEnv(initialContext);
      contextBuilder.activate();
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
      key = dsn + ".smtp.host";
      msprops.put("mail.smtp.host", localSettings.getProperty(key));
      key = dsn + ".smtp.port";
      msprops.put("mail.smtp.port", localSettings.getProperty(key, "25"));
      javax.mail.Session mailSession = javax.mail.Session.getInstance(msprops);
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
      BasicDataSource bd = new BasicDataSource();
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
      bd.setDefaultAutoCommit(localSettings.getBooleanValue(dsn + ".defaultAutoCommit", false));
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
    bindHibernateSettings();
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
    try {
      initialContext.bind(path, object);
    } catch (NameNotFoundException ex) {
      createSubContextDirs(path);
      initialContext.bind(path, object);
    }
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
   * Bind hibernate settings.
   *
   * @throws NamingException the naming exception
   */
  protected void bindHibernateSettings() throws NamingException
  {
    String hibernateDialect = localSettings.getProperty("env.HibernateDialect");
    if (StringUtils.isNotEmpty(hibernateDialect) == true) {
      bind("java:/comp/env/HibernateDialect", hibernateDialect);
    }
    String hibernateSchemaUpdate = localSettings.getProperty("env.HibernateSchemaUpdate");
    if (StringUtils.isNotEmpty(hibernateDialect) == true) {
      bind("java:/comp/env/HibernateSchemaUpdate", Boolean.getBoolean(hibernateSchemaUpdate));
    }
  }

  /**
   * Bind standards.
   *
   * @throws NamingException the naming exception
   */
  protected void bindStandards() throws NamingException
  {
    initialContext.createSubcontext("java:/comp/env");
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
