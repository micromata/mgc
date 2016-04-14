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

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Wrapps support of a JDBC connection.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JdbProviderService
{
  /**
   * Name for Display in dropdowns.
   * 
   * @return
   */
  String getName();

  /**
   * Internal id.
   * 
   * @return
   */
  default String getId()
  {
    return getClass().getSimpleName();
  }

  /**
   * Class name of the jdbc driver.
   * 
   * @return
   */
  String getJdbcDriver();

  /**
   * Build a sample url by given appname.
   * 
   * @param appName
   * @return
   */
  String getSampleUrl(String appName);

  boolean requiredUser();

  boolean requiresPass();

  boolean tryConnect(JdbcLocalSettingsConfigModel model, ValContext ctx);
}
