package de.micromata.mgc.javafx.launcher.gui;

import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;

/**
 * Wrapps a configuration tab information
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TabConfig
{
  /**
   * Sorting prio
   */
  public int prio = 50;
  public Class<? extends AbstractConfigTabController<?>> tabControlerClass;

  public LocalSettingsConfigModel configModel;

  public TabConfig()
  {

  }

  public TabConfig(Class<? extends AbstractConfigTabController<?>> tabControlerClass,
      LocalSettingsConfigModel configModel, int prio)
  {
    this(tabControlerClass, configModel);
    this.prio = prio;
  }

  public TabConfig(Class<? extends AbstractConfigTabController<?>> tabControlerClass,
      LocalSettingsConfigModel configModel)
  {
    this.tabControlerClass = tabControlerClass;
    this.configModel = configModel;
  }
}