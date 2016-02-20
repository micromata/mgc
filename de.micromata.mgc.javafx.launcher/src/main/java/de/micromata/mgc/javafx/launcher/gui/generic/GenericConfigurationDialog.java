package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
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

}
