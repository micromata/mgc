package de.micromata.mgc.javafx.launcher.gui;

import java.util.Optional;

import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class LauncherService
{
  static LauncherService INSTANCE = new LauncherService();

  public static LauncherService get()
  {
    return INSTANCE;
  }

  public void showConfirmDialog(String title, String message, Runnable okAction)
  {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(message);
    alert.setContentText("");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      okAction.run();
    }
  }

  public void showAboutDialog()
  {

  }

  public void shutdown()
  {
    MgcLauncher.get().getApplication().stop((application, status, msg) -> {

    });
    Platform.exit();
    System.exit(0);
  }
}
