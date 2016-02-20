package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.List;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;

/**
 * Use ServiceLoader to load services.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ConfigurationTabLoaderService
{
  List<TabConfig> getTabsByConfiguration(CastableLocalSettingsConfigModel configModel);
}
