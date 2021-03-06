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
  protected String id;
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

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setId(String id)
  {
    this.id = id;
  }

}
