package de.micromata.mgc.javafx.launcher.gui.generic;

import de.micromata.genome.util.runtime.config.MailSessionLocalSettingsConfigModel;
import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MailSessionConfigTabController extends AbstractConfigTabController<MailSessionLocalSettingsConfigModel>
{
  @FXML
  @ModelGuiField
  private TextField standardEmailSender;
  @FXML
  @ModelGuiField
  private CheckBox emailEnabled;
  @FXML
  @ModelGuiField
  private TextField emailHost;
  @FXML
  @ModelGuiField
  private TextField emailPort;
  @FXML
  @ModelGuiField
  private CheckBox emailAuthEnabled;
  @FXML
  @ModelGuiField
  private TextField emailAuthUser;

  @FXML
  @ModelGuiField
  private TextField emailAuthPass;

  @Override
  public void initializeWithModel()
  {
    emailEnabled.setOnAction(event -> setEmailEnabled(emailEnabled.isSelected()));
    fromModel();
    setEmailEnabled(emailEnabled.isSelected());

  }

  protected void setEmailEnabled(boolean enabled)
  {
    emailHost.setDisable(enabled == false);
    emailPort.setDisable(enabled == false);
    emailAuthEnabled.setDisable(enabled == false);
    emailAuthUser.setDisable(enabled == false);
    emailAuthPass.setDisable(enabled == false);
  }

  @Override
  public String getTabTitle()
  {

    return "Email Server";
  }

}
