package de.micromata.genome.util.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.SQLNestedException;

/**
 * Handles desktop database automatic creation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LauncherDataSource extends BasicDataSource
{
  @Override
  public Connection getConnection() throws SQLException
  {
    try {
      Connection con = super.getConnection();
      return con;
    } catch (SQLException ex) {
      if (ex instanceof SQLNestedException && ex.getCause() instanceof SQLException) {
        ex = (SQLException) ex.getCause();
      }
      if ("XJ004".equals(ex.getSQLState()) == true) {
        String orgurl = getUrl();
        try {
          setUrl(getUrl() + ";create=true");
          return super.getConnection();
        } finally {
          setUrl(orgurl);
        }
      }
      throw ex;
    }

  }
}
