package de.micromata.mgc.email.launcher;

import de.micromata.mgc.email.MailReceiverLocalSettingsConfigModel;
import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * GUI controller for MailReceiverLocalSettingsConfigModel.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmailReceiverController extends AbstractConfigTabController<MailReceiverLocalSettingsConfigModel>
{

  @FXML
  @ModelGuiField
  ChoiceBox<String> protocol;

  @FXML
  @ModelGuiField
  TextField hostname;
  @FXML
  @ModelGuiField
  TextField port;

  @FXML
  @ModelGuiField
  TextField username;
  @FXML
  @ModelGuiField
  TextField password;

  @Override
  public void initializeWithModel()
  {
    protocol.setItems(FXCollections.observableArrayList("imaps"));
    super.initializeWithModel();
  }

  @Override
  public String getTabTitle()
  {
    return "Mailbox";
  }

}
