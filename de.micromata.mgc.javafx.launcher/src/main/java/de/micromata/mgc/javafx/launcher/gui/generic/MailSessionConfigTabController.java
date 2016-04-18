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

import java.util.Optional;

import de.micromata.genome.util.runtime.config.MailSessionLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.feedback.ValMessageResultBox;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

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

  @ModelGuiField
  @FXML
  private ChoiceBox<String> encryption;

  @FXML
  @ModelGuiField
  private CheckBox emailAuthEnabled;
  @FXML
  @ModelGuiField
  private TextField emailAuthUser;

  @FXML
  @ModelGuiField
  private TextField emailAuthPass;
  @FXML
  private Button sendTestEmailButton;

  @Override
  public void initializeWithModel()
  {
    encryption.setItems(FXCollections.observableArrayList(model.getAvailableProtocols()));

    emailEnabled.setOnAction(event -> setEmailEnabled(emailEnabled.isSelected()));
    fromModel();
    setEmailEnabled(emailEnabled.isSelected());
    sendTestEmailButton.setOnAction(event -> {
      toModel();
      ValContext ctx = new ValContext().createSubContext(getModel(), null);
      model.validate(ctx);
      if (ctx.hasErrors() == true) {
        getConfigDialog().mapValidationMessagesToGui(ctx);
        return;
      }
      TextInputDialog dialog = new TextInputDialog("");
      dialog.setTitle("Send Test Mail");
      dialog.setHeaderText("Send a test email with the configured SMPT server.");
      dialog.setContentText("Please enter your receiver email:");
      Optional<String> result = dialog.showAndWait();
      if (result.isPresent() == false) {
        return;
      }

      EmailSendTester sendtester = new EmailSendTester(ctx, result.get(), standardEmailSender.getText());
      sendtester.testSendEmail(model);
      mapValidationMessagesToGui(ctx);
      ValMessageResultBox.showResultBox(ctx, "Sending email", "Result of sending Email");
    });

  }

  protected void setEmailEnabled(boolean enabled)
  {
    emailHost.setDisable(enabled == false);
    emailPort.setDisable(enabled == false);
    emailAuthEnabled.setDisable(enabled == false);
    emailAuthUser.setDisable(enabled == false);
    emailAuthPass.setDisable(enabled == false);
    sendTestEmailButton.setDisable(enabled == false);
  }

  @Override
  public String getTabTitle()
  {

    return "Email Server";
  }

}
