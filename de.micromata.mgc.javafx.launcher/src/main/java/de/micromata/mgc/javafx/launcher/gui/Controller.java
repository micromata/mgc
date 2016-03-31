package de.micromata.mgc.javafx.launcher.gui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for JavaFX.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface Controller
{
  public String getId();

  public void setId(String id);

  public Node getThisNode();

  public void setThisNode(Node node);

  public Parent getParent();

  void setParent(Parent parent);

  public Scene getScene();

  public void setScene(Scene scene);

  public Stage getStage();

  public void setStage(Stage stage);
}
