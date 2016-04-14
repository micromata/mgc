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

import java.util.Arrays;
import java.util.List;

import de.micromata.genome.util.runtime.config.HibernateSchemaConfigModel;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateSchemaConfigTabController extends AbstractConfigTabController<HibernateSchemaConfigModel>
{
  @FXML
  private ChoiceBox<String> schemaUpdate;
  @FXML
  private CheckBox showSql;
  @FXML
  private CheckBox formatSql;

  @Override
  public void initializeWithModel()
  {
    fromModel();
    List<String> values = Arrays.asList("update", "validate", "create", "create-drop");
    schemaUpdate.setItems(FXCollections.observableArrayList(values));
  }

  @Override
  public void fromModel()
  {
    schemaUpdate.setValue(model.getSchemaUpdate());
    showSql.setSelected(model.isShowSql());
    formatSql.setSelected(model.isFormatSql());

  }

  @Override
  public void toModel()
  {
    model.setSchemaUpdate(schemaUpdate.getValue());
    model.setShowSql(showSql.isSelected());
    model.setFormatSql(formatSql.isSelected());

  }

  @Override
  public String getTabTitle()
  {
    return "Hibernate Persistence";
  }

}
