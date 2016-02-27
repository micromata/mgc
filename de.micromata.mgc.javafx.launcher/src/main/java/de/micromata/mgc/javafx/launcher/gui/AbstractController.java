package de.micromata.mgc.javafx.launcher.gui;

import org.apache.log4j.Logger;

import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.feedback.FeedbackPanel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractController<M>
{
  private static final Logger LOG = Logger.getLogger(AbstractController.class);
  protected Parent parent;
  protected Scene scene;
  protected Stage stage;

  protected void registerValMessageReceiver(Node node, FeedbackPanel feedback, Class<?> referenceType, String property)
  {
    FXEvents.get().registerValMessageReceiver(this, node, referenceType, property);
  }

  public void addToFeedback(ValMessage msg)
  {
    LOG.warn("No feedbackPanel defined in " + getClass().getSimpleName() + "; " + msg.getI18nkey());
  }

  public Parent getParent()
  {
    return parent;
  }

  public void setParent(Parent parent)
  {
    this.parent = parent;
  }

  public Scene getScene()
  {
    return scene;
  }

  public void setScene(Scene scene)
  {
    this.scene = scene;
  }

  public Stage getStage()
  {
    return stage;
  }

  public void setStage(Stage stage)
  {
    this.stage = stage;
  }

}
