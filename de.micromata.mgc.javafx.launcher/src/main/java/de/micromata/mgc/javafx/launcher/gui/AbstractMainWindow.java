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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.event.MgcEventRegistries;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.LocalSettingsService;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.application.MgcApplication;
import de.micromata.mgc.application.MgcApplicationStartStopStatus;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.FXCssUtil;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.SystemService;
import de.micromata.mgc.javafx.SystemService.OsType;
import de.micromata.mgc.javafx.launcher.MgcApplicationStartStopToEventListener;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.MgcLauncherEvent;
import de.micromata.mgc.javafx.launcher.gui.generic.LauncherLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.lf5.Lf5MainWindowController;
import de.micromata.mgc.javafx.launcher.gui.lf5.MgcLf5Appender;
import de.micromata.mgc.javafx.logging.LoggingController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractMainWindow<M extends LocalSettingsConfigModel>
    extends AbstractModelController<MgcApplication<M>>
    implements Initializable
{
  private static final Logger LOG = Logger.getLogger(AbstractMainWindow.class);

  @FXML
  protected ImageView mainWindowLogo;
  @FXML
  protected Menu launchMenu;

  @FXML
  protected Button startServerButton;
  @FXML
  protected Button stopServerButton;

  @FXML
  protected Button helpButton;
  @FXML
  protected Button openBrowser;

  @FXML
  protected Pane loggingPane;

  @FXML
  protected LoggingController loggingController;

  @FXML
  protected MenuItem hideWindowMenu;

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {

  }

  @Override
  public void initializeWithModel()
  {
    Thread currentThread = Thread.currentThread();
    currentThread.setUncaughtExceptionHandler(model.getUncaughtExceptionHandler());
    addCss();
    LauncherLocalSettingsConfigModel config = MgcLauncher.getLauncherConfig();
    if (config.isEnableLF5() == false) {
      launchMenu.setVisible(false);
    }
    if (SystemService.get().getOsType() != OsType.Windows) {
      hideWindowMenu.setVisible(false);
    }
    stage.setOnCloseRequest(event -> {
      if (SystemService.get().getOsType() != OsType.Windows) {
        event.consume();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(model.getTranslateService().translate("mgc.launcher.gui.quitconfirmation.title"));
        alert.setHeaderText(model.getTranslateService().translate("mgc.launcher.gui.quitconfirmation.header"));
        alert.setContentText(model.getTranslateService().translate("mgc.launcher.gui.quitconfirmation.message"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
          closeApplication(null);
        }
      }
    });

    startServerButton.setOnAction(e -> {
      startServer();
    });
    stopServerButton.setOnAction(e -> {
      stopServer();
    });
    stopServerButton.setDisable(true);
    boolean runnin = MgcLauncher.get().getApplication().isRunning();
    startServerButton.setDisable(runnin);
    stopServerButton.setDisable(runnin == false);
    openBrowser.setDisable(runnin == false);
    addStartServerEventHandler();
    addStopServerEventHandler();

    openBrowser.setOnAction(e -> {
      launchBrowser();
    });
    loggingPane.widthProperty().addListener(new ChangeListener<Number>()
    {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth)
      {
        loggingController.adjustWidth(newSceneWidth.doubleValue());
      }
    });
    loggingPane.heightProperty().addListener(new ChangeListener<Number>()
    {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth)
      {
        loggingController.adjustHeight(newSceneWidth.doubleValue());
      }
    });
    loggingController.adjustHeight(loggingPane.getHeight());
    loggingController.adjustWidth(loggingPane.getWidth());
    MgcEventRegistries.getEventInstanceRegistry().registerListener(new MgcApplicationStartStopToEventListener());
    String helpUrl = getModel().getApplicationInfo().getHelpUrl();
    if (StringUtils.isBlank(helpUrl) == true) {
      helpButton.setVisible(false);
    } else {
      helpButton.setOnAction(event -> {
        SystemService.get().openUrlInBrowser(helpUrl);
      });
    }
    FXEvents.get().addEventHandler(this, stopServerButton, MgcLauncherEvent.APP_STARTED, event -> {

      if (config.isStartBrowserOnStartup() == true) {
        launchBrowser();
      }
    });
  }

  protected void addStartServerEventHandler()
  {
    FXEvents.get().addEventHandler(this, stopServerButton, MgcLauncherEvent.APP_STARTED, event -> {
      startServerButton.setDisable(true);
      stopServerButton.setDisable(false);
      openBrowser.setDisable(false);
    });
  }

  protected void addStopServerEventHandler()
  {
    FXEvents.get().addEventHandler(this, stopServerButton, MgcLauncherEvent.APP_STOPPED, event -> {
      startServerButton.setDisable(false);
      stopServerButton.setDisable(true);
      openBrowser.setDisable(true);
    });
  }

  @Override
  public void fromModel()
  {

  }

  @Override
  public void toModel()
  {

  }

  protected void addCss()
  {
    stage.getScene().getStylesheets().add(FXCssUtil.CSS);
  }

  @Override
  public void addToFeedback(ValMessage msg)
  {
    LOG.warn("Mainwindow Has no feedbackpanel");
  }

  public void startServer()
  {

    LocalSettingsService.reset();
    LocalSettingsEnv.reset();
    if (LocalSettings.localSettingsExists() == false) {
      loggingController.warn("Application is not configured.");
      return;
    }
    startServerButton.setDisable(true);

    MgcLauncher.get().getServerExecPool().submit(() -> {
      M configModel = getApplication().getConfigModel();
      configModel.fromLocalSettings(LocalSettings.get());

      MgcApplicationStartStopStatus res = model.start(MgcLauncher.originalMainArgs);
      if (res == MgcApplicationStartStopStatus.StartError) {
        startServerButton.setDisable(false);
        openBrowser.setDisable(true);
      }

    });

  }

  public void stopServer()
  {
    stopServerButton.setDisable(true);

    MgcLauncher.get().getServerExecPool().submit(() -> {
      model.stop();
    });

  }

  public boolean isServerRunning()
  {
    return model.isRunning();
  }

  protected abstract Class<? extends AbstractConfigDialog<M>> getConfigurationDialogControlerClass();

  protected Pair<Pane, ? extends AbstractConfigDialog<M>> loadConfigDialog()
  {
    Class<? extends AbstractConfigDialog<M>> controlToLoad = getConfigurationDialogControlerClass();

    Pair<Pane, ? extends AbstractConfigDialog<M>> ret = ControllerService.get().loadControlWithModelNewScene(
        controlToLoad, Pane.class, model.getConfigModel(), this);
    return ret;
  }

  @FXML
  private void openConfigDialog(ActionEvent event)
  {
    Pair<Pane, ? extends AbstractConfigDialog<M>> load = loadConfigDialog();
    Pane root = load.getKey();
    AbstractConfigDialog<M> controller = load.getValue();
    controller.mainWindow = this;

    controller.getStage().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
      controller.closeDialog();
      e.consume();
    });

    controller.getStage().initModality(Modality.APPLICATION_MODAL);
    controller.getStage().setWidth(800);
    controller.getStage().setHeight(600);

    controller.getStage().setTitle("Configuration");
    controller.getStage().show();

  }

  @FXML
  private void closeApplication(ActionEvent event)
  {

    if (model != null) {
      model.stop();
    }
    Platform.exit();
    System.exit(0); // NOSONAR    System.exit(...) and Runtime.getRuntime().exit(...) should not be called" Main app exit.
  }

  @FXML
  private void hideWindow(ActionEvent event)
  {
    stage.hide();

  }

  @FXML
  private void openLogLF5(ActionEvent event)
  {

    if (MgcLf5Appender.initialized == false) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Log4J Viewer");
      alert.setHeaderText("No MgcLf5Appender found");
      alert.setContentText(
          "To activate Logs to shown in the LF5 window, you have to register the Appender in the log4j.properties configuration:\n"
              + "\n"
              + "Sample:\n"
              + "log4j.rootCategory=DEBUG, A1, F1, LF5\n"
              + "...\n"
              + "log4j.appeender.LF5.Threshold=INFO\r\n"
              + "log4j.appender.LF5=de.micromata.mgc.javafx.launcher.gui.lf5.MgcLf5Appender");
      alert.showAndWait();
    }
    if (Lf5MainWindowController.CONTROLERINSTANCE != null) {
      Lf5MainWindowController.CONTROLERINSTANCE.show();
      return;
    }
    Lf5MainWindowController controller = ControllerService.get()
        .loadAsWindow(this, Lf5MainWindowController.class, model, "About");
    controller.initializeWithModel();
    controller.getStage().show();

  }

  private void launchBrowser()
  {
    String puburl = getApplication().getPublicUrl();
    if (StringUtils.isBlank(puburl) == true) {
      loggingController.error("No public url configured");
      return;
    }
    SystemService.get().openUrlInBrowser(puburl);
  }

  protected Class<? extends AboutDialogController> getAboutDialogControllerClass()
  {
    return AboutDialogController.class;
  }

  @FXML
  protected void showAboutDialog(ActionEvent event)
  {
    AboutDialogController controller = ControllerService.get()
        .loadAsDialog(this, getAboutDialogControllerClass(), "About");
    controller.setModel(model);
    controller.initializeWithModel();
    controller.getStage().show();
  }

  public MgcApplication<M> getApplication()
  {
    return model;
  }

  public LoggingController getLoggingController()
  {
    return loggingController;
  }

  public void reloadConfig()
  {
    getApplication().loadConfigModel();

  }

}
