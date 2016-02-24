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

  @Override
  public void initializeWithModel(LauncherLocalSettingsConfigModel model)
  {
    fromModel(model);
    normal.setToggleGroup(windowGroup);
    minimized.setToggleGroup(windowGroup);
    systemTray.setToggleGroup(windowGroup);

  }

  @Override
  public void toModel(LauncherLocalSettingsConfigModel modelObject)
  {
    if (systemTray.isSelected()) {
      modelObject.setWindowSettings(WindowSettings.SystemTrayOnly.name());
    } else if (minimized.isSelected()) {
      modelObject.setWindowSettings(WindowSettings.Minimized.name());
    } else {
      modelObject.setWindowSettings(WindowSettings.Normal.name());
    }
    modelObject.setStartServerOnStartup(Boolean.toString(startServerAtStartup.isSelected()));

  }

  @Override
  public void fromModel(LauncherLocalSettingsConfigModel modelObject)
  {
    if (WindowSettings.SystemTrayOnly.name().equals(modelObject.getWindowSettings()) == true) {
      systemTray.setSelected(true);
    } else if (WindowSettings.Minimized.name().equals(modelObject.getWindowSettings()) == true) {
      minimized.setSelected(true);
    } else {
      normal.setSelected(true);
    }
    startServerAtStartup.setSelected(modelObject.isStartServerOnStartup());
  }

  @Override
  public String getTabTitle()
  {
    return "Launcher";
  }

}
