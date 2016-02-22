package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.MailSessionLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.TabConfig;
import de.micromata.mgc.javafx.launcher.gui.jetty.JettyConfigTabController;
import de.micromata.mgc.jettystarter.JettyConfigModel;

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
    JettyConfigModel jettyConfig = configModel.castTo(JettyConfigModel.class);
    if (jettyConfig != null) {
      ret.add(new TabConfig(JettyConfigTabController.class, jettyConfig));
    }

    LauncherLocalSettingsConfigModel launcherConfig = configModel.castTo(LauncherLocalSettingsConfigModel.class);
    if (launcherConfig != null) {
      ret.add(new TabConfig(LauncherConfigTabController.class, launcherConfig));
    }

    MailSessionLocalSettingsConfigModel emailConfig = configModel.castTo(MailSessionLocalSettingsConfigModel.class);
    if (emailConfig != null) {
      ret.add(new TabConfig(MailSessionConfigTabController.class, emailConfig));
    }
    return ret;
  }

}
