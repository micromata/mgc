package de.micromata.mgc.javafx.launcher.gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.event.MgcEventRegistries;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.FXCssUtil;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.SystemService;
import de.micromata.mgc.javafx.launcher.MgcApplicationStartStopToEventListener;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.MgcLauncherEvent;
import de.micromata.mgc.javafx.launcher.gui.lf5.Lf5MainWindowController;
import de.micromata.mgc.javafx.logging.LoggingController;
import de.micromata.mgc.launcher.MgcApplication;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractMainWindow<M extends LocalSettingsConfigModel>extends AbstractController<M>
    implements Initializable
{
  private static final Logger LOG = Logger.getLogger(AbstractMainWindow.class);
  protected Stage stage;
  protected Parent parent;
  private MgcApplication<M> application;
  @FXML
  private Button startServerButton;
  @FXML
  private Button stopServerButton;

  @FXML
  private Button openBrowser;

  @FXML
  private Pane loggingPane;

  @FXML
  private LoggingController loggingController;

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {

  }

  public void initialize(MgcApplication<M> application)
  {
    this.application = application;
    addCss();
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

    FXEvents.get().addEventHandler(this, stopServerButton, MgcLauncherEvent.APP_STARTED, event -> {
      startServerButton.setDisable(true);
      stopServerButton.setDisable(false);
    });
    FXEvents.get().addEventHandler(this, stopServerButton, MgcLauncherEvent.APP_STOPPED, event -> {
      startServerButton.setDisable(false);
      stopServerButton.setDisable(true);
    });
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

    LocalSettings.reset();
    LocalSettingsEnv.reset();
    if (LocalSettings.localSettingsExists() == false) {
      loggingController.warn("GWiki is not configured.");
      return;
    }
    application.start(MgcLauncher.originalMainArgs);

  }

  public void stopServer()
  {
    application.stop();

  }

  public boolean isServerRunning()
  {
    return application.isRunning();
  }

  protected abstract Class<? extends AbstractConfigDialog<M>> getConfigurationDialogControlerClass();

  protected Pair<Pane, ? extends AbstractConfigDialog<M>> loadConfigDialog()
  {
    Class<? extends AbstractConfigDialog<M>> controlToLoad = getConfigurationDialogControlerClass();

    Pair<Pane, ? extends AbstractConfigDialog<M>> ret = ControllerService.get().loadControl(controlToLoad,
        Pane.class);
    return ret;
  }

  @FXML
  private void openConfigDialog(ActionEvent event)
  {
    Pair<Pane, ? extends AbstractConfigDialog<M>> load = loadConfigDialog();
    Pane root = load.getKey();
    AbstractConfigDialog<M> controller = load.getValue();
    controller.mainWindow = this;

    Stage stage = new Stage();
    stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
      controller.closeDialog();
      e.consume();
    });

    //    controller.setOwningStage(stage);
    Scene s = new Scene(root, AbstractConfigDialog.PREF_WIDTH, AbstractConfigDialog.PREF_HEIGHT);

    controller.setParent(root);
    controller.setScene(s);
    controller.setStage(stage);
    stage.setScene(s);
    stage.initModality(Modality.APPLICATION_MODAL);
    //stage.setResizable(false);
    stage.setTitle("Configuration");
    controller.initializeWithModel(application.getConfigModel());
    stage.show();

  }

  @FXML
  private void closeApplication(ActionEvent event)
  {

    if (application != null) {
      application.stop();
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
    if (Lf5MainWindowController.CONTROLERINSTANCE != null) {
      Lf5MainWindowController.CONTROLERINSTANCE.show();
      return;
    }
    Lf5MainWindowController controller = ControllerService.get()
        .loadAsWindow(this, Lf5MainWindowController.class, "About");
    controller.initWithApplication(application);
    controller.getStage().show();
  }

  private void launchBrowser()
  {
    String puburl = LocalSettings.get().get("gwiki.public.url");
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
    controller.initWithApplication(application);
    controller.getStage().show();
  }

  public MgcApplication<M> getApplication()
  {
    return application;
  }

  public LoggingController getLoggingController()
  {
    return loggingController;
  }

  @Override
  public void setStage(Stage stage)
  {
    this.stage = stage;
  }

  @Override
  public Stage getStage()
  {
    return stage;
  }

  @Override
  public Parent getParent()
  {
    return parent;
  }

  @Override
  public void setParent(Parent parent)
  {
    this.parent = parent;
  }

}
