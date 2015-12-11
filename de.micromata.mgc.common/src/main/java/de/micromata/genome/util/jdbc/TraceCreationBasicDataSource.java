package de.micromata.genome.util.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Traces the allocation stack traces, of the jdbc connections.
 * 
 * This is usefull to figure out which code is responsible for not closing the conntection.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TraceCreationBasicDataSource extends BasicDataSource
{
  private Map<Connection, StackTraceElement[]> allocatedStacks = new IdentityHashMap<>();

  @Override
  public Connection getConnection() throws SQLException
  {
    Connection con = super.getConnection();
    allocatedStacks.put(con, new Throwable().getStackTrace());
    ConnectionWrapper wrapped = new ConnectionWrapper(con)
    {

      @Override
      public void close() throws SQLException
      {
        super.close();
        allocatedStacks.remove(getNestedConnection());
      }

    };
    return wrapped;
  }

  /**
   * 
   * @return null if no open connections
   */
  public String getOpenConnectionsDump()
  {
    if (allocatedStacks.isEmpty() == true) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("\n Open Connections: " + allocatedStacks.size()).append(":\n");
    for (Map.Entry<Connection, StackTraceElement[]> me : allocatedStacks.entrySet()) {
      sb.append("\n").append(me.getKey()).append("\n");
      for (StackTraceElement se : me.getValue()) {
        sb.append("  ").append(se.toString()).append("\n");
      }
    }
    return sb.toString();

  }
}
