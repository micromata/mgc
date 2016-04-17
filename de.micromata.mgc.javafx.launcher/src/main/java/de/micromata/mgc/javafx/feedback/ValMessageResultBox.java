package de.micromata.mgc.javafx.feedback;

import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Utility to show a dialog with the ValContext.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValMessageResultBox
{
  public static void showResultBox(ValContext ctx, String title, String titleMessage)
  {
    AlertType type = AlertType.INFORMATION;

    if (ctx.hasErrors() == true) {
      type = AlertType.ERROR;
    } else if (ctx.hasWarnings()) {
      type = AlertType.WARNING;
    }
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(titleMessage);
    alert.setContentText("Ooops, there was an error!");
    VBox vbox = new VBox();
    String firstMessage = null;
    for (ValMessage msg : ctx.getMessages()) {
      Label label = new Label(msg.getValState().name() + ": " + msg.getMessage());
      vbox.getChildren().add(label);
      if (firstMessage == null) {
        firstMessage = msg.getValState().name() + ": " + msg.getMessage();
      }
    }
    if (firstMessage != null) {
      alert.setContentText(firstMessage);
    }
    alert.getDialogPane().setExpandableContent(vbox);
    alert.showAndWait();
  }
}
