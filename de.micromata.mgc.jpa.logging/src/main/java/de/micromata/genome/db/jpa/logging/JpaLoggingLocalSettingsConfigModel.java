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

package de.micromata.genome.db.jpa.logging;

import de.micromata.genome.logging.LoggingWithFallback;
import de.micromata.genome.logging.config.LoggingWithFallbackLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.JndiLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * JPA Logging with datasource and jdni.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaLoggingLocalSettingsConfigModel extends LoggingWithFallbackLocalSettingsConfigModel
{
  JdbcLocalSettingsConfigModel jdbcConfig;

  public JpaLoggingLocalSettingsConfigModel()
  {
    jdbcConfig = new JdbcLocalSettingsConfigModel("genomelog", "Logging Database",
        new JndiLocalSettingsConfigModel("genomelog", JndiLocalSettingsConfigModel.DataType.DataSource,
            "java:comp/env/genome/jdbc/dsLogging"));

  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    jdbcConfig.fromLocalSettings(localSettings);
  }

  @Override
  public void validate(ValContext ctx)
  {
    super.validate(ctx);
    jdbcConfig.validate(ctx);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    super.toProperties(writer);
    jdbcConfig.toProperties(writer);
    return writer;
  }

  @Override
  protected LoggingWithFallback createFallbackLogging()
  {
    return new GenomeJpaLoggingImpl();
  }

  @Override
  public JdbcLocalSettingsConfigModel getJdbcConfig()
  {
    return jdbcConfig;
  }

  public void setJdbcConfig(JdbcLocalSettingsConfigModel jdbcConfig)
  {
    this.jdbcConfig = jdbcConfig;
  }

}
