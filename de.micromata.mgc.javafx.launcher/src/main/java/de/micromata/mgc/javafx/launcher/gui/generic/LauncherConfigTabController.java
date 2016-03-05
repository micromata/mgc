package de.micromata.mgc.javafx.launcher.gui.generic;

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
  private CheckBox startServerAtStartup;

  @FXML
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
    model.setStartServerOnStartup(Boolean.toString(startServerAtStartup.isSelected()));
    model.setStartBrowserOnStartup(Boolean.toString(startBrowserOnStartup.isSelected()));

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
    startServerAtStartup.setSelected(model.isStartServerOnStartup());
    startBrowserOnStartup.setSelected(model.isStartBrowserOnStartup());
  }

  @Override
  public String getTabTitle()
  {
    return "Launcher";
  }

}
