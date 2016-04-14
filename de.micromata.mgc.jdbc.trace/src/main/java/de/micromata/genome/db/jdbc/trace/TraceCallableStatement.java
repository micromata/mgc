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

import de.micromata.genome.db.jdbc.wrapper.CallableStatementWrapper;

/**
 * The Class TraceCallableStatement.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TraceCallableStatement extends CallableStatementWrapper
{

  /**
   * The trace connection.
   */
  private TraceConnection traceConnection;

  /**
   * Instantiates a new trace callable statement.
   */
  public TraceCallableStatement()
  {
  }

  /**
   * Instantiates a new trace callable statement.
   *
   * @param traceConnection the trace connection
   * @param nestedStatement the nested statement
   */
  public TraceCallableStatement(TraceConnection traceConnection, CallableStatement nestedStatement)
  {
    super(nestedStatement);
    this.traceConnection = traceConnection;
  }

  // TODO overwrite implement methods

  public TraceConnection getTraceConnection()
  {
    return traceConnection;
  }

  public void setTraceConnection(TraceConnection traceConnection)
  {
    this.traceConnection = traceConnection;
  }
}
