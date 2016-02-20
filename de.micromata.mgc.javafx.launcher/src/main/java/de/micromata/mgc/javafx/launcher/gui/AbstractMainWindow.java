package de.micromata.mgc.javafx.launcher.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.FXCssUtil;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.launcher.MgcApplicationStartStopToEventListener;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.MgcLauncherEvent;
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
    // TODO Auto-generated method stub

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

  }

  protected void addCss()
  {
    stage.getScene().getStylesheets().add(FXCssUtil.CSS);
  }

  @Override
  public void addToFeedback(ValMessage msg)
  {
    // TODO RK Auto-generated method stub

  }

  public void startServer()
  {
    MgcApplicationStartStopToEventListener listener = new MgcApplicationStartStopToEventListener();
    if (LocalSettings.localSettingsExists() == false) {
      loggingController.warn("GWiki is not configured.");
      return;
    }
    application.start(MgcLauncher.originalMainArgs, listener);
    //    application.start(new String[] {}, (application, status, msg) -> {
    //      {
    //        switch (status) {
    //          case StartError:
    //            if (msg.getException() != null) {
    //              msg.getException().printStackTrace();
    //            }
    //            loggingController.error(msg.getMessage());
    //            break;
    //          case StartNoConfiguration:
    //            loggingController.warn("GWiki is not configured.");
    //            break;
    //          case AlreadyRunning:
    //            loggingController.info("GWiki server already started.");
    //            break;
    //          case Success:
    //            startServerButton.setDisable(true);
    //            stopServerButton.setDisable(false);
    //            loggingController.info("GWiki server started.");
    //            break;
    //        }
    //  }

    //    });

  }

  public void stopServer()
  {
    MgcApplicationStartStopToEventListener listener = new MgcApplicationStartStopToEventListener();
    application.stop(listener);

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
    stage.setResizable(false);
    stage.setTitle("Configuration");
    controller.initializeWithModel(application.getConfigModel());
    stage.show();

  }

  @FXML
  private void closeApplication(ActionEvent event)
  {

    if (application != null) {
      application.stop((application, status, msg) -> {
      });
    }
    Platform.exit();
    System.exit(0); // NOSONAR    System.exit(...) and Runtime.getRuntime().exit(...) should not be called" Main app exit.
  }

  @FXML
  private void hideWindow(ActionEvent event)
  {
    stage.hide();

  }

  private void launchBrowser()
  {
    String puburl = LocalSettings.get().get("gwiki.public.url");
    if (StringUtils.isBlank(puburl) == true) {
      loggingController.error("No public url configured");
      return;
    }

    Desktop desktop = null;
    if (Desktop.isDesktopSupported()) {
      desktop = Desktop.getDesktop();
    }

    if (desktop != null) {
      try {
        desktop.browse(new URI(puburl));
      } catch (final IOException e) {
        loggingController.error("Can't launch browser: " + e.getMessage(), e);
      } catch (final URISyntaxException e) {
        loggingController.error("Can't launch browser: " + e.getMessage(), e);
      }
    }
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
