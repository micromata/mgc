package de.micromata.genome.util.runtime.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

/**
 * Wrapps a Datasource definition.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JdbcLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  @ALocalSettingsPath(key = "drivername", comment = "JDBC Java class")
  private String drivername;
  @ALocalSettingsPath(key = "username", comment = "Database user")
  private String username;

  @ALocalSettingsPath(key = "password", comment = "Database password for given user")
  private String password;

  @ALocalSettingsPath(key = "url", comment = "JDBC url to connect to DB")
  private String url;

  @ALocalSettingsPath()
  private String extendedSettings;
  @ALocalSettingsPath(defaultValue = "8",
      comment = "Sets the maximum number of active connections that can be allocated at the same time.\n"
          + "Use a negative value for no limit.")
  private String maxActive;
  @ALocalSettingsPath(defaultValue = "8",
      comment = "Sets the maximum number of connections that can remain idle in the pool.")
  private String maxIdle;
  @ALocalSettingsPath(defaultValue = "0", comment = " Sets the minimum number of idle connections in the pool.")
  private String minIdle;
  @ALocalSettingsPath(defaultValue = "-1",
      comment = "Max waiting while obtaining connection. Use -1 to make the pool wait indefinitely.")
  private String maxWait;

  @ALocalSettingsPath(defaultValue = "0", comment = "Sets the initial size of the connection pool.")
  private String intialSize;
  @ALocalSettingsPath(comment = "Sets the default catalog.")
  private String defaultCatalog;
  @ALocalSettingsPath(comment = "Sets default auto-commit state of connections returned by this datasource.")
  private String defaultAutoCommit;
  @ALocalSettingsPath(comment = "Validation query to test if connection is valid.")
  private String validationQuery;
  @ALocalSettingsPath(defaultValue = "-1",
      comment = "Sets the validation query timeout, the amount of time, in seconds, that" +
          " connection validation will wait for a response from the database when" +
          "  executing a validation query.  \nUse a value less than or equal to 0 for  no timeout.")
  private String validationQueryTimeout;
  /**
   * Internal flag, if datasource is not optional.
   */
  private boolean needDatabase = false;
  /**
   * Has to be set outside.
   */
  private String name;

  private List<JndiLocalSettingsConfigModel> associatedJndi = new ArrayList<>();

  public JdbcLocalSettingsConfigModel(String name, String comment)
  {
    super(comment);
    this.name = name;
  }

  public JdbcLocalSettingsConfigModel(String name, String comment, JndiLocalSettingsConfigModel... jnddis)
  {
    super(comment);
    this.name = name;

    for (JndiLocalSettingsConfigModel jdni : jnddis) {
      this.associatedJndi.add(jdni);
    }
  }

  @Override
  public void validate(ValContext valContext)
  {
    ValContext ctx = valContext.createSubContext(this, null);
    if (StringUtils.isBlank(drivername) == true) {
      ctx.error("drivername", "Please select jdbcDriver");
    }
    if (StringUtils.isBlank(url) == true) {
      ctx.error("url", "Please select url for JDBC");
    }
    if (ctx.hasLocalError() == true) {
      return;
    }
    if (isExtendedSettings() == true) {
      validateExtended(ctx);
    } else {
      resetExtendedSettings();
    }
    checkDbUrl(ctx, drivername, url, username, password);
  }

  private void validateExtended(ValContext ctx)
  {
    checkInteger("maxActive", ctx);
    checkInteger("maxIdle", ctx);
    checkInteger("minIdle", ctx);
    checkInteger("maxWait", ctx);
    checkInteger("intialSize", ctx);
    checkInteger("validationQueryTimeout", ctx);
  }

  private void checkInteger(String field, ValContext ctx)
  {
    String text = (String) PrivateBeanUtils.readField(this, field);
    if (StringUtils.isBlank(text) == true) {
      ctx.directError(field, "Please provide a number for field " + field);
      return;
    }
    try {
      Integer.parseInt(text);
    } catch (NumberFormatException ex) {
      ctx.directError(field, "Please provide a number for field " + field);
    }

  }

  private void resetExtendedSettings()
  {
    resetFielToDefault("maxActive");
    resetFielToDefault("maxIdle");
    resetFielToDefault("minIdle");
    resetFielToDefault("maxWait");
    resetFielToDefault("intialSize");
    resetFielToDefault("defaultCatalog");
    resetFielToDefault("defaultAutoCommit");
    resetFielToDefault("validationQuery");
    resetFielToDefault("validationQueryTimeout");
  }

  private boolean checkDbUrl(ValContext ctx, String driver, String url, String user, String pass)
  {
    try {
      Class.forName(driver);
      try (Connection con = DriverManager.getConnection(url, user, pass)) {
        try (Statement stmt = con.createStatement()) {
          ctx.info("Created DB Connection....");
        }
      }
      return true;
    } catch (ClassNotFoundException e) {
      ctx.error("driver", "Cannot find db driver: " + driver);
      return false;
    } catch (SQLException e) {
      ctx.error("Cannot create connection: " + e.getMessage());
      SQLException ne = e.getNextException();
      if (ne != null && ne != e) {
        ctx.error("", ne.getMessage(), ne);
      } else {
        ctx.error("", e.getMessage(), e);
      }
      return false;
    }
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    writer.put("db.ds." + name + ".name", name, "Name of the Datasource");
    writer = super.toProperties(writer);

    LocalSettingsWriter sw = writer.newSection("JNDI for Datasource " + name);
    for (JndiLocalSettingsConfigModel jndi : associatedJndi) {
      jndi.toProperties(sw);
    }
    return writer;
  }

  @Override
  public String buildKey(String key)
  {
    return "db.ds." + name + "." + key;
  }

  public String getDrivername()
  {
    return drivername;
  }

  public void setDrivername(String drivername)
  {
    this.drivername = drivername;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean isNeedDatabase()
  {
    return needDatabase;
  }

  public void setNeedDatabase(boolean needDatabase)
  {
    this.needDatabase = needDatabase;
  }

  public List<JndiLocalSettingsConfigModel> getAssociatedJndi()
  {
    return associatedJndi;
  }

  public String getExtendedSettings()
  {
    return extendedSettings;
  }

  public boolean isExtendedSettings()
  {
    return "true".equals(extendedSettings);
  }

  public void setExtendedSettings(String extendedSettings)
  {
    this.extendedSettings = extendedSettings;
  }

  public void setExtendedSettings(boolean extendedSettings)
  {
    this.extendedSettings = Boolean.toString(extendedSettings);
  }

  public String getMaxActive()
  {
    return maxActive;
  }

  public void setMaxActive(String maxActive)
  {
    this.maxActive = maxActive;
  }

  public String getMaxIdle()
  {
    return maxIdle;
  }

  public void setMaxIdle(String maxIdle)
  {
    this.maxIdle = maxIdle;
  }

  public String getMinIdle()
  {
    return minIdle;
  }

  public void setMinIdle(String minIdle)
  {
    this.minIdle = minIdle;
  }

  public String getMaxWait()
  {
    return maxWait;
  }

  public void setMaxWait(String maxWait)
  {
    this.maxWait = maxWait;
  }

  public String getIntialSize()
  {
    return intialSize;
  }

  public void setIntialSize(String intialSize)
  {
    this.intialSize = intialSize;
  }

  public String getDefaultCatalog()
  {
    return defaultCatalog;
  }

  public void setDefaultCatalog(String defaultCatalog)
  {
    this.defaultCatalog = defaultCatalog;
  }

  public String getDefaultAutoCommit()
  {
    return defaultAutoCommit;
  }

  public boolean isDefaultAutoCommit()
  {
    return "true".equals(defaultAutoCommit);
  }

  public void setDefaultAutoCommit(String defaultAutoCommit)
  {
    this.defaultAutoCommit = defaultAutoCommit;
  }

  public void setDefaultAutoCommit(boolean defaultAutoCommit)
  {
    this.defaultAutoCommit = Boolean.toString(defaultAutoCommit);
  }

  public String getValidationQuery()
  {
    return validationQuery;
  }

  public void setValidationQuery(String validationQuery)
  {
    this.validationQuery = validationQuery;
  }

  public String getValidationQueryTimeout()
  {
    return validationQueryTimeout;
  }

  public void setValidationQueryTimeout(String validationQueryTimeout)
  {
    this.validationQueryTimeout = validationQueryTimeout;
  }

  public void setAssociatedJndi(List<JndiLocalSettingsConfigModel> associatedJndi)
  {
    this.associatedJndi = associatedJndi;
  }

}
