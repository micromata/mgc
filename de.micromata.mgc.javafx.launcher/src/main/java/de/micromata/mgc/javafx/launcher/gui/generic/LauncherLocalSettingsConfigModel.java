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
  @ALocalSettingsPath(defaultValue = "false", comment = "Starting application also starts server")
  private String startServerAtStartup;

  @ALocalSettingsPath(defaultValue = "false", comment = "Starting the server will open a browser window")
  private String startBrowserOnStartup;

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
    return "true".equals(startServerAtStartup);
  }

  public String getStartServerOnStartup()
  {
    return startServerAtStartup;
  }

  public void setStartServerOnStartup(String startServerOnStartup)
  {
    this.startServerAtStartup = startServerOnStartup;
  }

  public String getStartBrowserOnStartup()
  {
    return startBrowserOnStartup;
  }

  public void setStartBrowserOnStartup(String startBrowserOnStartup)
  {
    this.startBrowserOnStartup = startBrowserOnStartup;
  }

  public boolean isStartBrowserOnStartup()
  {
    return "true".equals(startBrowserOnStartup);
  }

  public void setStartBrowserOnStartup(boolean startBrowserOnStartup)
  {
    this.startBrowserOnStartup = Boolean.toString(startBrowserOnStartup);
  }
}
