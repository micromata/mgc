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
    MgcLauncher.get().getApplication().stop();
    Platform.exit();
    System.exit(0);
  }
}
