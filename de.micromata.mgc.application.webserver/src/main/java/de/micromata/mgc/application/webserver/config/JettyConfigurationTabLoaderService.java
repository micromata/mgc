package de.micromata.mgc.application.webserver.config;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;
import de.micromata.mgc.javafx.launcher.gui.generic.ConfigurationTabLoaderService;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyConfigurationTabLoaderService implements ConfigurationTabLoaderService
{

  @Override
  public List<TabConfig> getTabsByConfiguration(CastableLocalSettingsConfigModel configModel)
  {
    List<TabConfig> ret = new ArrayList<>();
    JettyConfigModel jettyConfig = configModel.castToForConfigDialog(JettyConfigModel.class);
    if (jettyConfig != null) {
      ret.add(new TabConfig(JettyConfigTabController.class, jettyConfig));
    }
    return ret;
  }

}
