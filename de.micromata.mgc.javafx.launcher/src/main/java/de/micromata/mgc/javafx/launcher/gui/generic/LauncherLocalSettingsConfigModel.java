package de.micromata.mgc.javafx.launcher.gui.generic;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LauncherLocalSettingsConfigModel extends AbstractCompositLocalSettingsConfigModel
{
  public static enum WindowSettings
  {
    Normal, Minimized, SystemTrayOnly
  }

  @ALocalSettingsPath(defaultValue = "Normal")
  private String windowSettings;
  @ALocalSettingsPath(defaultValue = "false")
  private String startServerOnStartup;

  @Override
  public void validate(ValContext ctx)
  {
    super.validate(ctx);
  }

  public WindowSettings getWindowSettingsType()
  {
    for (WindowSettings ws : WindowSettings.values()) {
      if (ws.name().equals(windowSettings) == true) {
        return ws;
      }
    }
    return WindowSettings.Normal;
  }

  public String getWindowSettings()
  {
    return windowSettings;
  }

  public void setWindowSettings(String windowSettings)
  {
    this.windowSettings = windowSettings;
  }

  public boolean isStartServerOnStartup()
  {
    return "true".equals(startServerOnStartup);
  }

  public String getStartServerOnStartup()
  {
    return startServerOnStartup;
  }

  public void setStartServerOnStartup(String startServerOnStartup)
  {
    this.startServerOnStartup = startServerOnStartup;
  }

}