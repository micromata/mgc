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

import java.sql.SQLException;
import java.sql.Savepoint;

import de.micromata.genome.util.runtime.CallableX;

/**
 * Traces SQL statements.
 * 
 * @author roger
 * 
 */
public interface SqlTracer
{
  /**
   * Execute a statement.
   * 
   * @param sqlTraced the traced sql
   * @param sql the sql as string
   * @param callable the callable
   * @return the sql result
   * @throws SQLException when a sql error happened
   */
  public <T> T executeWrapped(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable) throws SQLException;

  /**
   * Execute a prepareStatement/Call
   *
   * @param sqlTraced the traced sql
   * @param sql the sql as string
   * @param callable the callable
   * @return the sql result
   * @throws SQLException when a sql error happened
   */
  public <T> T executePreparedWrapped(SqlTraced sqlTraced, String sql, CallableX<T, SQLException> callable) throws SQLException;

  /**
   * Doing a rollback.
   * 
   * @param connection the connection
   * @param savepoint the save point to rollback to
   */
  public void rollback(TraceConnection connection, Savepoint savepoint);

  /**
   * Doing a commit.
   * 
   * @param connection the connection
   */
  public void commit(TraceConnection connection) throws SQLException;

}
