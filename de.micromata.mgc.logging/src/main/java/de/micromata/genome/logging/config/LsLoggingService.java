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

package de.micromata.genome.logging.config;

import java.util.List;

import de.micromata.genome.logging.spi.BaseLogConfigurationLocalSettingsConfigModel;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;

/**
 * Service has to registered by the JRE ServiceLoader infrastructure.
 * 
 * A LsLogging can be read/write its konfiguration in the local settings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LsLoggingService
{
  public static interface LsLoggingDescription
  {
    String typeId();

    @Override
    String toString();

    String description();

    BaseLoggingLocalSettingsConfigModel getConfigModel();

  }

  public static interface LsLogConfigurationDescription
  {
    String typeId();

    @Override
    String toString();

    String description();

    BaseLogConfigurationLocalSettingsConfigModel getConfigModel();
  }

  List<LsLoggingDescription> getLsLoggingImpls();

  List<LsLogConfigurationDescription> getLsLogConfigurationImpls();
}
