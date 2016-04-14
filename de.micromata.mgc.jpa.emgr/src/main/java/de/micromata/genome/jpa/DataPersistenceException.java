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

package de.micromata.genome.jpa;

/**
 * Will be thrown in case of data errors.
 * 
 * Typically to many characters for column.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class DataPersistenceException extends JpaPersistenceException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -998801803461138060L;

  /**
   * Instantiates a new data persistence exception.
   *
   * @param msg the msg
   * @param sql the sql
   * @param sqlState the sql state
   * @param cause the cause
   */
  public DataPersistenceException(String msg, String sql, String sqlState, RuntimeException cause)
  {
    super(msg, sql, sqlState, cause);
  }

}
