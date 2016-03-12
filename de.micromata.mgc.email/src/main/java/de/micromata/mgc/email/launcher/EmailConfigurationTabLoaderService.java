package de.micromata.mgc.email.launcher;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.mgc.email.MailReceiverLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;
import de.micromata.mgc.javafx.launcher.gui.generic.ConfigurationTabLoaderService;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmailConfigurationTabLoaderService implements ConfigurationTabLoaderService
{

  @Override
  public List<TabConfig> getTabsByConfiguration(CastableLocalSettingsConfigModel configModel)
  {
    List<TabConfig> ret = new ArrayList<>();

    MailReceiverLocalSettingsConfigModel config = configModel
        .castToForConfigDialog(MailReceiverLocalSettingsConfigModel.class);
    if (config != null) {
      ret.add(new TabConfig(EmailReceiverController.class, config));
    }

    return ret;
  }

}
