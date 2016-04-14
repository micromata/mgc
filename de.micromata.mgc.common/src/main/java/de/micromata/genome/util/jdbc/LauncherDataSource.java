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
