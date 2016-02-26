package de.micromata.genome.util.runtime.config.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Just an dery stores file on local file system.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmbeddedDerbyJdbcProviderServiceImpl extends AbstractJdbcProviderServiceImpl
{
  public EmbeddedDerbyJdbcProviderServiceImpl()
  {
    super("Embedded Derby", "org.apache.derby.jdbc.EmbeddedDriver");
  }

  @Override
  public String getSampleUrl(String appName)
  {
    return "jdbc:derby:" + appName + "-derby";
  }

  @Override
  protected void connect(JdbcLocalSettingsConfigModel model, ValContext ctx) throws ClassNotFoundException, SQLException
  {
    Class.forName(model.getDrivername());
    try {
      try (Connection con = DriverManager.getConnection(model.getUrl(), model.getUsername(), model.getPassword())) {
        try (Statement stmt = con.createStatement()) {
          ctx.directInfo("", "Created DB Connection....");
        }
      }
    } catch (SQLException ex) {
      // retry to create db
      try (Connection con = DriverManager.getConnection(model.getUrl() + ";create=true", model.getUsername(),
          model.getPassword())) {
        try (Statement stmt = con.createStatement()) {
          ctx.directInfo("", "Created DB Connection....");
        }
      }
    }
  }
}
