//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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

  @ALocalSettingsPath(defaultValue = "Normal",
      comment = "Window state of the Launcher Main window. Valid values are Normal, Minimized, SystemTrayOnly")
  private String windowSettings;
  @ALocalSettingsPath(defaultValue = "false", comment = "Starting application also starts server")
  private String startServerAtStartup;

  @ALocalSettingsPath(defaultValue = "false", comment = "Starting the server will open a browser window")
  private String startBrowserOnStartup;

  @ALocalSettingsPath(defaultValue = "false", comment = "Enable LF5 Viewer in Main window")
  private String enableLF5;

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

  public String getEnableLF5()
  {
    return enableLF5;
  }

  public void setEnableLF5(String enableLF5)
  {
    this.enableLF5 = enableLF5;
  }

  public boolean isEnableLF5()
  {
    return Boolean.getBoolean(enableLF5);
  }
}
