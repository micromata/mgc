
package de.micromata.mgc.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.lang.Validate;

import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;
import de.micromata.mgc.javafx.launcher.gui.AbstractController;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
   * Actually loaded controllers.
   */
  private Map<Class<? extends ModelController<?>>, ModelController<?>> controllers = new HashMap<>();

  /**
   * Stores eventhandler methods of controllers (annotated with {@link FXML}).
   */
  private Map<Class<? extends ModelController<?>>, Set<Method>> controllerEventMethods = new HashMap<>();

  /**
   * If {@link #load(Class)} is called this field stores the Controller that is loaded by the load method. Is used to
   * check in {@link #initialize(URL, ResourceBundle)} if we initialize a nested controller and add this one manually to
   * {@link #controllers}.
   */
  private Class<?> currentlyLoading;

  /**
   * Nodes that want to be notified in case of an event.
   */
  private WeakHashMap<ModelController<?>, List<Node>> listeners = new WeakHashMap<>();

  /**
   * Stages to controllers.
   */
  private WeakHashMap<ModelController<?>, Stage> owningStages = new WeakHashMap<>();

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
    FXMLLoader fxmlLoader = new FXMLLoader(ControllerService.class.getResource(path));
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
    //    controller.setOwningStage(stage);
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

  // TODO RK review and delete following
  /**
   * Evaluates every validation message an sets {@link #CSS_CLASS_ERROR} add controls that matches the
   * {@link ValidationMessage#getProperty()}.
   * 
   * Matching is done in two ways: 1. {@link ValidationMessage#getProperty()} is equal to the controls field name in the
   * given controller. 2. the {@link ValidationMessageEvent} provides some nodes from where we can do a
   * {@link Node#lookup(String)}. {@link ValidationMessage#getProperty()} is used as the selector.
   * 
   * @param messages messages to compute.
   * @param controller controller to look for controls that may need to get marked as erroneous.
   * @param vme validation message.
   */
  // TODO RK
  //  private void markErroneousFields(List<ValidationMessage> messages, Controller<?> controller,
  //      ValidationMessageEvent vme)
  //  {
  //    for (ValidationMessage msg : messages) {
  //      // Actually that leads to duplicated Log-Messages at startup if the config dialog
  //      // is raised because the config isn't valid.
  //      PcLog.warn(msg.getMessage(), msg.getArgs());
  //      if (StringUtils.isBlank(msg.getProperty()) == true) {
  //        continue;
  //      }
  //      for (Node n : vme.getReceivers()) {
  //        Node lookup = n.lookup(FXGuiUtils.cssSafeSelectorId(msg.getProperty()));
  //        if (lookup != null) {
  //          FXCssUtil.replaceStyleClass(CSS_CLASS_VALID, CSS_CLASS_ERROR, lookup);
  //        }
  //      }
  //
  //      Field field = PrivateBeanUtils.findField(controller, msg.getProperty());
  //      if (field != null && Node.class.isAssignableFrom(field.getType()) == true) {
  //        Node node = (Node) PrivateBeanUtils.readField(controller, field);
  //        FXCssUtil.replaceStyleClass(CSS_CLASS_VALID, CSS_CLASS_ERROR, node);
  //      }
  //    }
  //  }

  /**
   * Same as {@link #markErroneousFields(List, Controller, ValidationMessageEvent)} only in the other direction.
   * 
   * @param controller controller to look for controls that need to get marked as valid.
   * @param vme validation message.
   */
  //  private void resetErroneousFields(Controller<?> controller, ValidationMessageEvent vme)
  //  {
  //    for (Node n : vme.getReceivers()) {
  //      Set<Node> lookup = n.lookupAll("." + CSS_CLASS_ERROR);
  //      for (Node f : lookup) {
  //        FXCssUtil.replaceStyleClass(CSS_CLASS_ERROR, CSS_CLASS_VALID, f);
  //      }
  //    }
  //
  //    Map<String, java.lang.Object> fields = PrivateBeanUtils.getAllNonStaticFields(controller);
  //    for (java.lang.Object field : fields.values()) {
  //      if (field == null) {
  //        continue;
  //      }
  //      if (Node.class.isAssignableFrom(field.getClass()) == true) {
  //        Node node = (Node) field;
  //        FXCssUtil.replaceStyleClass(CSS_CLASS_ERROR, CSS_CLASS_VALID, node);
  //      }
  //    }
  //  }

  /**
   * Return the owning stage.
   * 
   * @param controller controller from whom we like to get the stage.
   * @return the stage.
   */
  public Stage getOwningStage(ModelController<?> controller)
  {
    return owningStages.get(controller);
  }

  /**
   * Sets the owning stage.
   * 
   * @param owningStage the stage.
   * @param controller the controller that was created by the stage.
   */
  public void setOwningStage(Stage owningStage, ModelController<?> controller)
  {
    owningStages.put(controller, owningStage);
  }

  /**
   * Get the fxml file from an {@link FXMLFile} annotation.
   * 
   * @param controllerToLoad Controller to retrieve the fxml file for.
   * @return URL of the fxml file.
   */
  private static <C extends ModelController<?>> URL extractFXMLFile(Class<C> controllerToLoad)
  {
    FXMLFile fxmlFile = controllerToLoad.getAnnotation(FXMLFile.class);
    if (fxmlFile == null) {
      Class<? super C> superclass = controllerToLoad.getSuperclass();
      fxmlFile = superclass.getAnnotation(FXMLFile.class);
    }
    return ControllerService.class.getResource(fxmlFile.file());
  }

  /**
   * Runnable that dispatches the given event to the given controler.
   * 
   * @author Daniel (d.ludwig@micromata.de)
   *
   */
  private class ControllerEventRunnable implements Runnable
  {
    /**
     * Wants to receive the event.
     */
    private ModelController<?> controller;

    /**
     * The event.
     */
    private java.lang.Object event;

    /**
     * Constructor.
     * 
     * @param controller the controller.
     * @param event the event.
     */
    public ControllerEventRunnable(ModelController<?> controller, java.lang.Object event)
    {
      super();
      this.controller = controller;
      this.event = event;
    }

    /**
     * 
     * {@inheritDoc}
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
      Set<Method> eventSinks = controllerEventMethods.get(controller.getClass());
      if (eventSinks == null) {
        eventSinks = new HashSet<>();
        Method[] declaredMethods = controller.getClass().getDeclaredMethods();
        for (Method m : declaredMethods) {
          if (m.getAnnotation(FXML.class) == null) {
            continue;
          }

          eventSinks.add(m);
        }
        controllerEventMethods.put((Class<? extends ModelController<?>>) controller.getClass(), eventSinks);
      }

      for (Method m : eventSinks) {
        eventToMethodDispatch(m);
      }
    }

    /**
     * Calls the given controller event handler (the param).
     * 
     * @param m controller event handler.
     */
    private void eventToMethodDispatch(Method m)
    {
      Class<?>[] parameterTypes = m.getParameterTypes();
      if (parameterTypes == null || parameterTypes.length == 0) {
        return;
      }

      if (parameterTypes.length != 1) {
        throw new ControllerServiceException(
            "controller event handlers have to have exactly one parameter, the event parameter");
      }

      if (parameterTypes[0].equals(event.getClass())) {
        try {
          m.setAccessible(true);
          m.invoke(controller, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          // TODO PcLog.error(e, "pc.controller_event_error");
        }
      }
    }
  }

  /**
   * Default Exception if something went wrong inside ConrollerService.
   * 
   * @author Daniel (d.ludwig@micromata.de)
   *
   */
  public static class ControllerServiceException extends RuntimeException
  {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = 130508380008671336L;

    /**
     * Constructor.
     * 
     * @param message the message.
     */
    public ControllerServiceException(String message)
    {
      super(message);
    }
  }
}
