package de.micromata.genome.util.runtime.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

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
  /**
   * Has to be set outside.
   */
  private String name;
  /**
   * If set, the datasource will be registered as jndi name.
   */
  private String jndiName;

  public JdbcLocalSettingsConfigModel(String name, String comment)
  {
    super(comment);
    this.name = name;
  }

  public JdbcLocalSettingsConfigModel(String name, String comment, String jndiName)
  {
    super(comment);
    this.name = name;
    this.jndiName = jndiName;
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
    // TODO RK sowas umsetzen?
    //    String url = jdbcUrl;
    //    if (StandaloneDatabases.LOCAL_DERBY.getDriver().equals(jdbcDriver) == true) {
    //      url = jdbcUrl + ";create=true";
    //    }

    if (ctx.hasLocalError() == true) {
      return;
    }

    checkDbUrl(ctx, drivername, url, username, password);
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
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    writer = super.toProperties(writer);
    writer.put("db.ds." + name + ".name", name);
    if (StringUtils.isBlank(jndiName) == true) {
      return writer;
    }
    LocalSettingsWriter sw = writer.newSection("JNDI for Datasource " + name);
    sw.put("jndi.bind." + name + ".target", jndiName);
    sw.put("jndi.bind." + name + ".type", "DataSource");
    sw.put("jndi.bind." + name + ".source", name);
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

  public String getJndiName()
  {
    return jndiName;
  }

  public void setJndiName(String jndiName)
  {
    this.jndiName = jndiName;
  }
}
