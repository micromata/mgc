package de.micromata.genome.logging.config;

import de.micromata.genome.logging.spi.LogConfigurationDAOWrapper;
import de.micromata.genome.logging.spi.LsLogConfigurationLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Initialize from configuraiton and delegates LogConfiguration to nested target.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLogConfigurationDAOImpl extends LogConfigurationDAOWrapper
{
  protected void init()
  {
    init(LocalSettings.get());
  }

  protected void init(LocalSettings ls)
  {
    LsLogConfigurationLocalSettingsConfigModel cfgModel = new LsLogConfigurationLocalSettingsConfigModel();
    cfgModel.fromLocalSettings(ls);
    setTarget(cfgModel.createLogConfigurationDAO());
  }
}
