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

import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import de.micromata.mgc.javafx.launcher.gui.generic.LauncherLocalSettingsConfigModel.WindowSettings;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LauncherConfigTabController extends AbstractConfigTabController<LauncherLocalSettingsConfigModel>
{
  @FXML
  private RadioButton normal;
  @FXML
  private RadioButton minimized;
  @FXML
  private RadioButton systemTray;

  private ToggleGroup windowGroup = new ToggleGroup();

  @FXML
  @ModelGuiField
  private CheckBox startServerAtStartup;

  @FXML
  @ModelGuiField
  private CheckBox startBrowserOnStartup;

  @Override
  public void initializeWithModel()
  {
    fromModel();
    normal.setToggleGroup(windowGroup);
    minimized.setToggleGroup(windowGroup);
    systemTray.setToggleGroup(windowGroup);

  }

  @Override
  public void toModel()
  {
    if (systemTray.isSelected()) {
      model.setWindowSettings(WindowSettings.SystemTrayOnly.name());
    } else if (minimized.isSelected()) {
      model.setWindowSettings(WindowSettings.Minimized.name());
    } else {
      model.setWindowSettings(WindowSettings.Normal.name());
    }
    super.toModel();

  }

  @Override
  public void fromModel()
  {
    if (WindowSettings.SystemTrayOnly.name().equals(model.getWindowSettings()) == true) {
      systemTray.setSelected(true);
    } else if (WindowSettings.Minimized.name().equals(model.getWindowSettings()) == true) {
      minimized.setSelected(true);
    } else {
      normal.setSelected(true);
    }
    super.fromModel();
  }

  @Override
  public String getTabTitle()
  {
    return "Launcher";
  }

}
