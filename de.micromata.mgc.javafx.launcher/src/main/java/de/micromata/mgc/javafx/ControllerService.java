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

package de.micromata.mgc.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.lang.Validate;

import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigDialog;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;
import de.micromata.mgc.javafx.launcher.gui.AbstractModelController;
import de.micromata.mgc.javafx.launcher.gui.Controller;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

  public <W extends Node, C extends Controller> Pair<W, C> loadControllerControl(Class<C> controlToLoad,
      Class<W> widget, Controller parentController)
  {
    return loadControllerControl(controlToLoad, widget, parentController, null);
  }

  public <W extends Node, C extends Controller> Pair<W, C> loadControllerControl(Class<C> controlToLoad,
      Class<W> widget, Controller parentController, String id)
  {
    Pair<W, C> ret = loadControl(controlToLoad, widget);
    C controller = ret.getSecond();
    controller.setThisNode(ret.getFirst());
    controller.setParent((Parent) parentController.getThisNode());
    ret.getSecond().setScene(parentController.getScene());
    ret.getSecond().setStage(parentController.getStage());
    ret.getSecond().setId(id);
    return ret;
  }

  public <M, W extends Parent, C extends ModelController<M>> Pair<W, C> loadControlWithModelNewScene(
      Class<C> controlToLoad,
      Class<W> widget, M model, Controller parentController)
  {
    Pair<W, C> ret = loadControlWithModel(controlToLoad, widget, model, parentController);
    C controller = ret.getSecond();
    W wig = ret.getFirst();
    Stage stage = new Stage();
    //    controller.setOwningStage(stage);
    Scene s = new Scene(wig, AbstractConfigDialog.PREF_WIDTH, AbstractConfigDialog.PREF_HEIGHT);
    controller.setScene(s);
    controller.setStage(stage);
    stage.setScene(s);
    s.getStylesheets().add(FXCssUtil.CSS);
    return ret;
  }

  public <M, W extends Node, C extends ModelController<M>> Pair<W, C> loadControlWithModel(Class<C> controlToLoad,
      Class<W> widget, M model, Controller parentController)
  {
    Pair<W, C> ret = loadControllerControl(controlToLoad, widget, parentController);
    C controller = ret.getSecond();
    controller.setModel(model);
    controller.initializeWithModel();
    controller.registerValMessageReceivers();
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

  public <M, C extends AbstractModelController<M>> C loadAsDialog(AbstractMainWindow<?> mainWindow,
      Class<C> controllerClass,
      String dialogTitle)
  {
    Pair<Pane, C> pair = loadControllerControl(controllerClass, Pane.class, mainWindow);
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

  public <M, C extends AbstractModelController<M>> C loadAsWindow(AbstractMainWindow<?> mainWindow,
      Class<C> controllerClass, M model, String dialogTitle)
  {
    Pair<Pane, C> pair = loadControlWithModel(controllerClass, Pane.class, model, mainWindow);
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
