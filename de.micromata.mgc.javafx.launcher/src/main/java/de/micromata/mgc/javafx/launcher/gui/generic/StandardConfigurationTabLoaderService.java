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

import de.micromata.genome.logging.config.LsLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.AbstractTextConfigFileConfigModel;
import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.HibernateSchemaConfigModel;
import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.MailSessionLocalSettingsConfigModel;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class StandardConfigurationTabLoaderService implements ConfigurationTabLoaderService
{

  @Override
  public List<TabConfig> getTabsByConfiguration(CastableLocalSettingsConfigModel configModel)
  {
    List<TabConfig> ret = new ArrayList<>();
    LauncherLocalSettingsConfigModel launcherConfig = configModel
        .castToForConfigDialog(LauncherLocalSettingsConfigModel.class);
    if (launcherConfig != null) {
      ret.add(new TabConfig(LauncherConfigTabController.class, launcherConfig));
    }

    MailSessionLocalSettingsConfigModel emailConfig = configModel
        .castToForConfigDialog(MailSessionLocalSettingsConfigModel.class);
    if (emailConfig != null) {
      ret.add(new TabConfig(MailSessionConfigTabController.class, emailConfig));
    }
    JdbcLocalSettingsConfigModel jdbc = configModel.castToForConfigDialog(JdbcLocalSettingsConfigModel.class);
    if (jdbc != null) {
      ret.add(new TabConfig(JdbcConfigTabController.class, jdbc));
    }
    HibernateSchemaConfigModel hibernateConfig = configModel.castToForConfigDialog(HibernateSchemaConfigModel.class);
    if (hibernateConfig != null) {
      ret.add(new TabConfig(HibernateSchemaConfigTabController.class, hibernateConfig));
    }
    List<AbstractTextConfigFileConfigModel> textConfigs = configModel
        .castToForConfigDialogCollect(AbstractTextConfigFileConfigModel.class);
    for (AbstractTextConfigFileConfigModel textConfig : textConfigs) {
      ret.add(new TabConfig(TextConfigTabController.class, textConfig));
    }
    LsLoggingLocalSettingsConfigModel logging = configModel
        .castToForConfigDialog(LsLoggingLocalSettingsConfigModel.class);
    if (logging != null) {
      ret.add(new TabConfig(LoggingConfigTabController.class, logging));
    }
    return ret;
  }

  @Override
  public <T extends LocalSettingsConfigModel> Class<? extends ModelController<T>> findTabForConfig(T model)
  {
    //    if (model instanceof IFileLoggingLocalSettingsConfigModel) {
    //      return (Class) IFileLoggingConfigTabController.class;
    //    }
    return null;
  }

}
