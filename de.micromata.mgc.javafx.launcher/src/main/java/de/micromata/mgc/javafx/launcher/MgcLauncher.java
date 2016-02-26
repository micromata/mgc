package de.micromata.mgc.javafx.launcher;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;
import de.micromata.mgc.javafx.launcher.gui.SystemTrayMenu;
import de.micromata.mgc.javafx.launcher.gui.generic.LauncherLocalSettingsConfigModel;
import de.micromata.mgc.launcher.MgcApplication;
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

  public static MgcLauncher<?> get()
  {
    return INSTANCE;
  }

  protected MgcLauncher(MgcApplication<M> application, Class<? extends AbstractMainWindow<M>> mainWindowClass)
  {
    this.application = application;
    this.mainWindowClass = mainWindowClass;
    INSTANCE = this;
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

    if (noWindow(args) == true) {
      checkConfiguration();
      launchCli(args);
    } else {
      MgcJfxApplication.launch(MgcJfxApplication.class, args);
    }
  }

  public static boolean checkConfiguration()
  {
    LocalSettingsConfigModel configuraiton = sapplication.getConfigModel();
    ValContext ctx = new ValContext();
    configuraiton.validate(ctx);
    ctx.translateMessages(sapplication.getTranslateService());
    for (ValMessage msg : ctx.getMessages()) {
      GLog.logValMessage(GenomeLogCategory.System, msg);
    }
    return ctx.hasErrors() == false;
  }

  private void launchCli(String[] args)
  {
    // TODO this blocks. 
    // TODO check if kill  gracefully stop the app.
    application.start(args);

  }

  static LauncherLocalSettingsConfigModel getLauncherConfig()
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

      stage.setTitle("GWiki");
      stage.setWidth(1150);
      stage.setHeight(700);
      Pair<Parent, ? extends AbstractMainWindow<?>> lc = ControllerService.get().loadControl(sMainWindowClass,
          Parent.class);
      mainWindow = lc.getSecond();
      Parent parent = lc.getFirst();
      mainWindow.setStage(stage);
      Scene scene = new Scene(parent);
      stage.setScene(scene);
      mainWindow.setParent(parent);

      mainWindow.initialize(sapplication);

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
      boolean configOk = checkConfiguration();
      new SystemTrayMenu(stage).createAndShowGUI();
      showDummyWindow();
      if (configOk == true && config.isStartServerOnStartup() == true) {
        sapplication.start(originalMainArgs);
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
}
