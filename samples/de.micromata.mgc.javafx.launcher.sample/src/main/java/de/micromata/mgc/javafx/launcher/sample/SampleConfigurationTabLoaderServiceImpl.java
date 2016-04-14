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

package de.micromata.mgc.javafx.launcher.sample;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;
import de.micromata.mgc.javafx.launcher.gui.generic.ConfigurationTabLoaderService;

public class SampleConfigurationTabLoaderServiceImpl implements ConfigurationTabLoaderService
{
  @Override
  public List<TabConfig> getTabsByConfiguration(CastableLocalSettingsConfigModel configModel)
  {
    List<TabConfig> ret = new ArrayList<>();
    SampleConfigModel sampleConfig = configModel.castToForConfigDialog(SampleConfigModel.class);
    if (sampleConfig != null) {
      ret.add(new TabConfig(SampleConfigTabController.class, sampleConfig));
    }
    return ret;
  }
}
