package de.micromata.mgc.javafx.launcher.gui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Abstract JavaFX controller
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AbstractController implements Controller
{
  protected Node thisNode;
  protected Parent parent;
  protected Scene scene;
  protected Stage stage;

  @Override
  public Node getThisNode()
  {
    return thisNode;
  }

  @Override
  public void setThisNode(Node thisNode)
  {
    this.thisNode = thisNode;
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

  @Override
  public Scene getScene()
  {
    return scene;
  }

  @Override
  public void setScene(Scene scene)
  {
    this.scene = scene;
  }

  @Override
  public Stage getStage()
  {
    return stage;
  }

  @Override
  public void setStage(Stage stage)
  {
    this.stage = stage;
  }
}
