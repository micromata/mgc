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

package de.micromata.mgc.javafx.launcher.gui.generic;

import de.micromata.genome.util.runtime.config.AbstractTextConfigFileConfigModel;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import de.micromata.mgc.javafx.launcher.gui.DialogBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TextConfigTabController extends AbstractConfigTabController<AbstractTextConfigFileConfigModel>
{
  @FXML
  private VBox rowVBox;

  private String title;

  @Override
  public String getTabTitle()
  {
    return title;
  }

  @Override
  public void initializeWithModel()
  {
    title = model.getTitle();
    DialogBuilder builder = dialogBuilder();
    DialogBuilder row = builder.addLabeledRow("FileName");

    row.addTextField("fileName", model.getFileName());
    row = builder.addRow();
    //    row.addTextArea("content", model.getContent());
    TextArea area = new TextArea();
    area.setPrefWidth(600);
    area.setMinWidth(Region.USE_COMPUTED_SIZE);
    area.setMaxWidth(Double.MAX_VALUE);
    area.setMinHeight(350);
    area.setId("content");
    area.setText(model.getContent());

    row.getParentPane().getChildren().add(area);

  }

  @Override
  public void fromModel()
  {
    DialogBuilder builder = dialogBuilder();
    builder.getTextInputControlById("content").setText(model.getContent());
  }

  @Override
  public void toModel()
  {
    DialogBuilder builder = dialogBuilder();
    model.setContent(builder.getTextInputControlById("content").getText());

  }

  @Override
  protected DialogBuilder dialogBuilder()
  {
    DialogBuilder ret = super.dialogBuilder();
    ret.setParentPane(rowVBox);
    return ret;
  }
}
