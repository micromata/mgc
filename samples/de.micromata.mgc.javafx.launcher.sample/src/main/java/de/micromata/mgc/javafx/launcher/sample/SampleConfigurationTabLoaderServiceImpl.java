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
