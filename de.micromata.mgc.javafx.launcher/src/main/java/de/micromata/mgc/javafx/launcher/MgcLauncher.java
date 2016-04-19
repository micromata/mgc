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

package de.micromata.mgc.javafx.launcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.micromata.genome.util.runtime.Log4JInitializer;
import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.mgc.application.MgcApplication;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;
import de.micromata.mgc.javafx.launcher.gui.SystemTrayMenu;
import de.micromata.mgc.javafx.launcher.gui.generic.LauncherLocalSettingsConfigModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Launching applications.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcLauncher<M extends LocalSettingsConfigModel>
{
  static MgcLauncher<?> INSTANCE;
  private MgcApplication<M> application;

  static MgcApplication sapplication;
  static AbstractMainWindow<?> mainWindow;
  public static String[] originalMainArgs;
  private static boolean mainWindowExists = false;
  private Class<? extends AbstractMainWindow<M>> mainWindowClass;
  static Class<? extends AbstractMainWindow<?>> sMainWindowClass;
  private final ExecutorService serverExecPool = Executors.newFixedThreadPool(1);
  public static boolean noGui = false;

  public static MgcLauncher<?> get()
  {
    return INSTANCE;
  }

  protected MgcLauncher(MgcApplication<M> application, Class<? extends AbstractMainWindow<M>> mainWindowClass)
  {
    this.application = application;
    this.mainWindowClass = mainWindowClass;
    INSTANCE = this;
    application.initializeAfterConstruction();
  }

  private boolean noWindow(String[] args)
  {
    for (String arg : args) {
      if (arg.equals("-nogui") == true) {
        return true;
      }
    }
    return false;
  }

  public void launch(String[] args)
  {
    originalMainArgs = args;
    sMainWindowClass = mainWindowClass;
    sapplication = application;
    Log4JInitializer.initializeLog4J();
    if (noWindow(args) == true) {
      noGui = true;
      if (sapplication.checkConfiguration() == false) {
        return;
      }
      if (sapplication.initWithConfig() == false) {
        return;
      }
      launchCli(args);
    } else {
      noGui = false;
      MgcJfxApplication.launch(MgcJfxApplication.class, args);
    }
  }

  private void launchCli(String[] args)
  {
    application.start(args);
  }

  public static LauncherLocalSettingsConfigModel getLauncherConfig()
  {
    LauncherLocalSettingsConfigModel ret = null;
    LocalSettingsConfigModel cf = sapplication.getConfigModel();
    if (cf instanceof CastableLocalSettingsConfigModel) {
      ret = ((CastableLocalSettingsConfigModel) cf).castTo(LauncherLocalSettingsConfigModel.class);
    }
    if (ret != null) {
      return ret;
    }
    return new LauncherLocalSettingsConfigModel();
  }

  static public class MgcJfxApplication extends Application
  {
    @Override
    public void start(Stage stage)
    {
      LauncherLocalSettingsConfigModel config = getLauncherConfig();

      stage.setTitle(sapplication.getApplicationInfo().getName());
      stage.setWidth(1150);
      stage.setHeight(700);
      Pair<Parent, ? extends AbstractMainWindow<?>> lc = ControllerService.get().loadControl(sMainWindowClass,
          Parent.class);
      mainWindow = lc.getSecond();
      Parent parent = lc.getFirst();
      mainWindow.setThisNode(parent);
      mainWindow.setStage(stage);
      Scene scene = new Scene(parent);
      mainWindow.setScene(scene);
      stage.setScene(scene);
      mainWindow.setModel(sapplication);
      //      mainWindow.setParent(null);

      mainWindow.initializeWithModel();

      switch (config.getWindowSettingsType()) {
        case Minimized:
          stage.setIconified(true);
          stage.show();
          break;
        case SystemTrayOnly:
          break;
        case Normal:
          stage.show();
          break;
      }
      mainWindowExists = true;
      boolean configOk = false;
      if (sapplication.checkConfiguration() == true) {
        configOk = sapplication.initWithConfig();
      }
      new SystemTrayMenu(stage).createAndShowGUI();
      showDummyWindow();
      if (configOk == true && config.isStartServerOnStartup() == true) {
        MgcLauncher.get().getServerExecPool().submit(() -> {
          sapplication.start(originalMainArgs);
        });
      }
    }

    /**
     * Workaround to keep jfx tread running, even the main window is hidden.
     */
    private void showDummyWindow()
    {
      Stage dummyPopup = new Stage();
      dummyPopup.initModality(Modality.NONE);
      // set as utility so no iconification occurs
      dummyPopup.initStyle(StageStyle.UTILITY);
      // set opacity so the window cannot be seen
      dummyPopup.setOpacity(0d);
      // not necessary, but this will move the dummy stage off the screen
      final Screen screen = Screen.getPrimary();
      final Rectangle2D bounds = screen.getVisualBounds();
      dummyPopup.setX(bounds.getMaxX());
      dummyPopup.setY(bounds.getMaxY());
      // create/add a transparent scene
      final Group root = new Group();
      dummyPopup.setScene(new Scene(root, 1d, 1d, Color.TRANSPARENT));
      // show the dummy stage
      dummyPopup.show();
    }

  }

  public static void showMainWindow()
  {
    if (mainWindowExists == true) {
      Platform.runLater(() -> {
        mainWindow.getStage().show();
        mainWindow.getStage().setIconified(false);

      });
    }
  }

  public MgcApplication<M> getApplication()
  {
    return application;
  }

  public ExecutorService getServerExecPool()
  {
    return serverExecPool;
  }

}
