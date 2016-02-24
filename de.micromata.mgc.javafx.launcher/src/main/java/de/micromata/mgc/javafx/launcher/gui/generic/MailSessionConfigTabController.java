package de.micromata.mgc.javafx.launcher.gui.generic;

import de.micromata.genome.util.runtime.config.MailSessionLocalSettingsConfigModel;
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
  private TextField standardEmailSender;
  @FXML
  private CheckBox emailEnabled;
  @FXML
  private TextField emailHost;
  @FXML
  private TextField emailPort;
  @FXML
  private CheckBox emailAuthEnabled;
  @FXML
  private TextField emailAuthUser;

  @FXML
  private TextField emailAuthPass;

  @Override
  public void initializeWithModel(MailSessionLocalSettingsConfigModel model)
  {
    emailEnabled.setOnAction(event -> setEmailEnabled(emailEnabled.isSelected()));
    fromModel(model);
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
  public void fromModel(MailSessionLocalSettingsConfigModel model)
  {
    standardEmailSender.setText(model.getStandardEmailSender());
    emailEnabled.setSelected(model.isEmailEnabled());
    emailHost.setText(model.getEmailHost());
    emailPort.setText(model.getEmailPort());
    emailAuthEnabled.setSelected(model.isEmailAuthEnabled());
    emailAuthUser.setText(model.getEmailAuthUser());
    emailAuthPass.setText(model.getEmailAuthPass());
  }

  @Override
  public void toModel(MailSessionLocalSettingsConfigModel model)
  {
    model.setStandardEmailSender(standardEmailSender.getText());
    model.setEmailEnabled(emailEnabled.isSelected());
    model.setEmailHost(emailHost.getText());
    model.setEmailPort(emailPort.getText());
    model.setEmailAuthEnabled(emailAuthEnabled.isSelected());
    model.setEmailAuthUser(emailAuthUser.getText());
    model.setEmailAuthPass(emailAuthPass.getText());
  }

  @Override
  public String getTabTitle()
  {

    return "Email Server";
  }

}
