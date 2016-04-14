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

package de.micromata.genome.db.jdbc.wrapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * A factory for creating SqlWrapper objects.
 *
 * @author roger
 */
public interface SqlWrapperFactory
{

  /**
   * Creates a new SqlWrapper object.
   *
   * @param dataSource the data source
   * @return the data source
   */
  public DataSource createDataSource(DataSource dataSource);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param dataSource the data source
   * @param userName the user name
   * @param connection the connection
   * @return the connection
   */
  public Connection createConnection(DataSourceWrapper dataSource, String userName, Connection connection);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param connection the connection
   * @param statement the statement
   * @return the statement
   */
  public Statement createStatement(ConnectionWrapper connection, Statement statement);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param connection the connection
   * @param sql the sql
   * @param preparedStatement the prepared statement
   * @return the prepared statement
   */
  public PreparedStatement createPreparedStatement(ConnectionWrapper connection, String sql,
      PreparedStatement preparedStatement);

  /**
   * Creates a new SqlWrapper object.
   *
   * @param connection the connection
   * @param preparedStatement the prepared statement
   * @return the callable statement
   */
  public CallableStatement createCallableStatement(ConnectionWrapper connection, CallableStatement preparedStatement);
}
