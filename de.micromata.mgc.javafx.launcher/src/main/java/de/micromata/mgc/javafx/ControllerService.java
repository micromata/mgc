
package de.micromata.mgc.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.lang.Validate;

import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.gui.AbstractController;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class provides basic methods for loading FXMLs that are defined with {@link FXMLFile}. Load your FXMLs if you
 * need extra functionality like sending messages to Nodes of controllers or if you need access to the stage where the
 * controller is part of.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class ControllerService
{

  /**
   * Singleton instance.
   */
  private static ControllerService INSTANCE;

  /**
   * Private constructor because singleton.
   */
  private ControllerService()
  {
    // NOOP
  }

  /**
   * Instance getter.
   * 
   * @return the singleton instance.
   */
  public static synchronized ControllerService get()
  {
    if (INSTANCE == null) {
      INSTANCE = new ControllerService();
    }

    return INSTANCE;
  }

  public void runInToolkitThread(final Runnable runnable)
  {

    if (Platform.isFxApplicationThread() == true) {
      runnable.run();
      return;
    }

    Platform.runLater(runnable);
  }

  public <W extends Node, C> Pair<W, C> loadControl(Class<C> controlToLoad, Class<W> widget)
  {
    String file;
    FXMLFile fxml = controlToLoad.getAnnotation(FXMLFile.class);
    if (fxml != null) {
      file = fxml.file();
    } else {
      file = "/fxml/" + controlToLoad.getSimpleName() + ".fxml";

    }
    Pair<W, C> load = loadScene(file, widget, controlToLoad);
    return load;
  }

  public <M, W extends Node, C extends ModelController<M>> Pair<W, C> loadControlWithModel(Class<C> controlToLoad,
      Class<W> widget, M model)
  {
    Pair<W, C> ret = loadControl(controlToLoad, widget);
    ret.getSecond().initializeWithModel(model);
    return ret;
  }

  public static <T extends Node, C> Pair<T, C> loadScene(String path, Class<T> nodeClass, Class<C> controlerClass)
  {
    try (InputStream is = ControllerService.class.getResourceAsStream(path)) {
      if (is == null) {
        throw new IllegalArgumentException("Canot find fxml file: " + path);
      }
    } catch (IOException ex) {
      throw new IllegalArgumentException("Canot find fxml file: " + path);
    }
    ResourceBundle resbundle = I18NTranslations
        .asResourceBundle(MgcLauncher.get().getApplication().getTranslateService());
    FXMLLoader fxmlLoader = new FXMLLoader(ControllerService.class.getResource(path), resbundle);
    try {
      Object loaded = fxmlLoader.load();
      Object controler = fxmlLoader.getController();
      Validate.notNull(loaded);
      Validate.notNull(controler);
      return Pair.make((T) loaded, (C) controler);

    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  public <M, C extends AbstractController<M>> C loadAsDialog(AbstractMainWindow<?> mainWindow, Class<C> controllerClass,
      String dialogTitle)
  {
    Pair<Pane, C> pair = loadControl(controllerClass, Pane.class);
    Stage stage = new Stage();
    stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
      stage.hide();
      e.consume();
    });
    Pane root = pair.getFirst();
    C controller = pair.getSecond();
    Scene s = new Scene(root);//, AbstractConfigDialog.PREF_WIDTH, AbstractConfigDialog.PREF_HEIGHT
    controller.setParent(root);
    controller.setScene(s);
    controller.setStage(stage);
    stage.setScene(s);
    stage.initModality(Modality.APPLICATION_MODAL);
    //stage.setResizable(false);
    stage.setTitle(dialogTitle);
    return controller;
  }

  public <M, C extends AbstractController<M>> C loadAsWindow(AbstractMainWindow<?> mainWindow, Class<C> controllerClass,
      String dialogTitle)
  {
    Pair<Pane, C> pair = loadControl(controllerClass, Pane.class);
    Stage stage = new Stage();
    stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
      stage.hide();
      e.consume();
    });
    Pane root = pair.getFirst();
    C controller = pair.getSecond();
    Scene s = new Scene(root);//, AbstractConfigDialog.PREF_WIDTH, AbstractConfigDialog.PREF_HEIGHT
    controller.setParent(root);
    controller.setScene(s);
    controller.setStage(stage);
    stage.setScene(s);
    //stage.setResizable(false);
    stage.setTitle(dialogTitle);
    return controller;
  }

}
