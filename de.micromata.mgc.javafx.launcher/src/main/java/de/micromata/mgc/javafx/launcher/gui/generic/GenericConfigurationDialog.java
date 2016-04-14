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

package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigDialog;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class GenericConfigurationDialog extends AbstractConfigDialog<CastableLocalSettingsConfigModel>
{

  @Override
  protected List<TabConfig> getConfigurationTabs()
  {
    List<TabConfig> ret = new ArrayList<>();
    ServiceLoader<ConfigurationTabLoaderService> res = ServiceLoader.load(ConfigurationTabLoaderService.class);
    for (ConfigurationTabLoaderService cts : res) {
      List<TabConfig> lsit = cts.getTabsByConfiguration(configModel);
      ret.addAll(lsit);
    }
    return ret;
  }

  public static <M extends LocalSettingsConfigModel> Class<? extends ModelController<M>> findForConfig(M config)
  {
    ServiceLoader<ConfigurationTabLoaderService> res = ServiceLoader.load(ConfigurationTabLoaderService.class);
    for (ConfigurationTabLoaderService cts : res) {
      Class<? extends ModelController<M>> ctl = cts.findTabForConfig(config);
      if (ctl != null) {
        return ctl;
      }
    }
    return null;
  }

}
