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
