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

package de.micromata.genome.logging.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.config.LsLoggingService;
import de.micromata.genome.logging.config.LsLoggingService.LsLogConfigurationDescription;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;

/**
 * Configure LogConfiguration by LocalSettings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLogConfigurationLocalSettingsConfigModel extends BaseLogConfigurationLocalSettingsConfigModel
{
  private BaseLogConfigurationLocalSettingsConfigModel nested;

  public static List<LsLogConfigurationDescription> getAvailableServices()
  {
    List<LsLogConfigurationDescription> ret = new ArrayList<>();
    ServiceLoader<LsLoggingService> loader = ServiceLoader.load(LsLoggingService.class);
    for (LsLoggingService lgs : loader) {
      ret.addAll(lgs.getLsLogConfigurationImpls());
    }
    return ret;
  }

  public static LsLogConfigurationDescription findByTypeId(String typeId)
  {
    List<LsLogConfigurationDescription> services = getAvailableServices();
    Optional<LsLogConfigurationDescription> optional = services.stream().filter(e -> e.typeId().equals(typeId))
        .findFirst();
    if (optional.isPresent() == false) {
      return null;
    }
    return optional.get();
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    LsLogConfigurationDescription desc = findByTypeId(getTypeId());
    if (desc == null) {
      return;
    }
    nested = desc.getConfigModel();
    nested.fromLocalSettings(localSettings);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    if (nested != null) {
      nested.setPrefix(getKeyPrefix());
      return nested.toProperties(writer);
    }
    return writer;
  }

  @Override
  public LogConfigurationDAO createLogConfigurationDAO()
  {
    return nested.createLogConfigurationDAO();
  }

}
