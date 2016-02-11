package de.micromata.genome.jpa.trace;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;

import de.micromata.genome.db.jdbc.trace.TraceConfig;
import de.micromata.genome.db.jdbc.trace.TraceConnection;
import de.micromata.genome.db.jdbc.trace.TraceDataSource;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.util.matcher.EveryMatcher;

/**
 * Traces JDBC statements in Hibernate.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracedDatasourceConnectionProviderImpl extends DatasourceConnectionProviderImpl
{
  @Override
  public Connection getConnection() throws SQLException
  {
    Connection con = super.getConnection();
    if (con instanceof TraceConnection) {
      return con;
    }
    TraceConfig tc = new TraceConfig();
    tc.setLogCategory(GenomeLogCategory.Database);
    tc.setLogLevel(LogLevel.Trace);
    tc.setEnableLogging(true);
    tc.setEnableStats(true);
    tc.setLogArguments(true);
    tc.setLogAtCommit(false);
    tc.setLogPreparedStatement(true);
    tc.setLogRolledBack(true);
    tc.setLogSqlLiteralStatement(true);
    tc.setLogFilterMatcher(new EveryMatcher<String>());
    TraceDataSource traceDataSource = new TraceDataSource(getDataSource(), tc);
    setDataSource(traceDataSource);
    con = super.getConnection();
    return con;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.hibernate.service.jdbc.connections.internal.DatasourceConnectionProviderImpl#closeConnection(java.sql.
   * Connection)
   */
  @Override
  public void closeConnection(Connection conn) throws SQLException
  {
    super.closeConnection(conn);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.hibernate.service.jdbc.connections.internal.DatasourceConnectionProviderImpl#supportsAggressiveRelease()
   */
  @Override
  public boolean supportsAggressiveRelease()
  {
    return super.supportsAggressiveRelease();
  }
}
