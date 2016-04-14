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

package de.micromata.genome.util.runtime.config.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Just an dery stores file on local file system.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmbeddedDerbyJdbcProviderServiceImpl extends AbstractJdbcProviderServiceImpl
{
  public EmbeddedDerbyJdbcProviderServiceImpl()
  {
    super("Embedded Derby", "org.apache.derby.jdbc.EmbeddedDriver");
  }

  @Override
  public String getSampleUrl(String appName)
  {
    return "jdbc:derby:" + appName + "-derby";
  }

  @Override
  protected void connect(JdbcLocalSettingsConfigModel model, ValContext ctx) throws ClassNotFoundException, SQLException
  {
    Class.forName(model.getDrivername());
    try {
      try (Connection con = DriverManager.getConnection(model.getUrl(), model.getUsername(), model.getPassword())) {
        try (Statement stmt = con.createStatement()) {
          ctx.directInfo("", "Created DB Connection....");
        }
      }
    } catch (SQLException ex) {
      // retry to create db
      try (Connection con = DriverManager.getConnection(model.getUrl() + ";create=true", model.getUsername(),
          model.getPassword())) {
        try (Statement stmt = con.createStatement()) {
          ctx.directInfo("", "Created DB Connection....");
        }
      }
    }
  }
}
