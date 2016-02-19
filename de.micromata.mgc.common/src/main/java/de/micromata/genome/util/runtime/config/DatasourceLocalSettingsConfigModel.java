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
public class DatasourceLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  @ALocalSettingsPath(key = "drivername", comment = "JDBC Java class")
  private String drivername;
  @ALocalSettingsPath(key = "username", comment = "Database user")
  private String username;

  @ALocalSettingsPath(key = "password", comment = "Database password for given user")
  private String password;

  @ALocalSettingsPath(key = "url", comment = "JDBC url to connect to DB")
  private String url;
  @ALocalSettingsPath(key = "name", comment = "Name of the data soruce")
  private String name;

  public DatasourceLocalSettingsConfigModel(String name, String comment)
  {
    super(comment);
    this.name = name;
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
  public String buildKey(String key)
  {
    return "db.ds." + name + "." + key;
  }

}
