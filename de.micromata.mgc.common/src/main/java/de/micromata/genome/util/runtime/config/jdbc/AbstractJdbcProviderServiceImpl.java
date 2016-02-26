package de.micromata.genome.util.runtime.config.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractJdbcProviderServiceImpl implements JdbProviderService
{
  protected String name;
  protected String driverName;

  public AbstractJdbcProviderServiceImpl(String name, String driverName)
  {
    this.name = name;
    this.driverName = driverName;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getJdbcDriver()
  {
    return driverName;
  }

  @Override
  public boolean requiredUser()
  {
    return true;
  }

  @Override
  public boolean requiresPass()
  {
    return true;
  }

  @Override
  public boolean tryConnect(JdbcLocalSettingsConfigModel model, ValContext ctx)
  {
    try {
      connect(model, ctx);
      return true;
    } catch (ClassNotFoundException e) {
      ctx.directError("driver", "Cannot find db driver: " + model.getDrivername());
      return false;
    } catch (SQLException e) {
      ctx.directError("", "Cannot create connection: " + e.getMessage());
      SQLException ne = e.getNextException();
      if (ne != null && ne != e) {
        ctx.directError("", ne.getMessage(), ne);
      } else {
        ctx.directError("", e.getMessage(), e);
      }
      return false;
    }
  }

  protected void connect(JdbcLocalSettingsConfigModel model, ValContext ctx) throws ClassNotFoundException, SQLException
  {
    Class.forName(model.getDrivername());
    try (Connection con = DriverManager.getConnection(model.getUrl(), model.getUsername(), model.getPassword())) {
      try (Statement stmt = con.createStatement()) {
        ctx.directInfo("", "Created DB Connection....");
      }
    }
  }
}
