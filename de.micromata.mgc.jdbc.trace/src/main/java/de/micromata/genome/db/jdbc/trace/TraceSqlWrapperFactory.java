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

package de.micromata.genome.db.jdbc.trace;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

import de.micromata.genome.db.jdbc.wrapper.ConnectionWrapper;
import de.micromata.genome.db.jdbc.wrapper.DataSourceWrapper;
import de.micromata.genome.db.jdbc.wrapper.SqlWrapperFactory;

/**
 * A factory for creating TraceSqlWrapper objects.
 *
 * @author roger
 */
public class TraceSqlWrapperFactory implements SqlWrapperFactory
{

  @Override
  public CallableStatement createCallableStatement(ConnectionWrapper connection, CallableStatement preparedStatement)
  {
    return new TraceCallableStatement((TraceConnection) connection, preparedStatement);
  }

  @Override
  public Connection createConnection(DataSourceWrapper dataSource, String userName, Connection connection)
  {
    return new TraceConnection((TraceDataSource) dataSource, userName, connection);
  }

  @Override
  public DataSource createDataSource(DataSource dataSource)
  {
    return new TraceDataSource(dataSource);
  }

  @Override
  public PreparedStatement createPreparedStatement(ConnectionWrapper connection, String sql,
      PreparedStatement preparedStatement)
  {
    return new TracePreparedStatement((TraceConnection) connection, sql, preparedStatement);
  }

  @Override
  public Statement createStatement(ConnectionWrapper connection, Statement statement)
  {
    return new TraceStatement((TraceConnection) connection, statement);
  }

}
