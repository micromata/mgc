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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderService;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.SystemService;
import de.micromata.mgc.javafx.feedback.ValMessageResultBox;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Configuration tab for a jdbc connection.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JdbcConfigTabController extends AbstractConfigTabController<JdbcLocalSettingsConfigModel>
{
  @FXML
  private ComboBox<String> drivername;

  @FXML
  private Button testJdbcButton;
  @FXML
  @ModelGuiField
  private TextField url;

  @FXML
  @ModelGuiField
  private TextField username;
  @FXML
  @ModelGuiField
  private TextField password;

  private List<JdbProviderService> availableDrivers;

  @FXML
  @ModelGuiField
  private CheckBox extendedSettings;

  @FXML
  private Pane extendedPane;
  private Node extendedChildPane;

  @FXML
  @ModelGuiField
  private TextField maxActive;
  @FXML
  @ModelGuiField
  private TextField intialSize;
  @FXML
  @ModelGuiField
  private TextField maxWait;
  @FXML
  @ModelGuiField
  private TextField maxIdle;
  @FXML
  @ModelGuiField
  private TextField minIdle;
  @FXML
  @ModelGuiField
  private CheckBox defaultAutoCommit;
  @FXML
  @ModelGuiField
  private TextField defaultCatalog;
  @FXML
  @ModelGuiField
  private TextField validationQuery;
  @FXML
  @ModelGuiField
  private TextField validationQueryTimeout;

  private String selectedJdbcConnectionTypeId;

  @Override
  public void initializeWithModel()
  {
    extendedChildPane = extendedPane.getChildren().get(0);
    availableDrivers = SystemService.get().getJdbcDrivers();
    List<String> items = new ArrayList<>();

    for (JdbProviderService sb : availableDrivers) {
      items.add(sb.getName());
    }
    drivername.setItems(FXCollections.observableArrayList(items));
    drivername.setOnAction(event -> {
      JdbProviderService dd = getSelectedDriver();
      if (dd != null) {
        url.setText(dd.getSampleUrl(model.getName()));
      }
    });
    extendedSettings.setOnAction(event -> {
      onShowExtended(extendedSettings.isSelected());
    });
    fromModel();
    testJdbcButton.setOnAction(event -> {
      toModel();
      ValContext valContext = new ValContext().createSubContext(model, null);
      model.validate(valContext);
      ValMessageResultBox.showResultBox(valContext, "Connection Databse", "Result of Connecting DB");

    });
    onShowExtended(extendedSettings.isSelected());
  }

  private void onShowExtended(boolean show)
  {
    extendedPane.setVisible(show);
    if (show == true) {
      if (extendedPane.getChildren().isEmpty() == true) {
        extendedPane.getChildren().add(extendedChildPane);
      }
    } else {
      if (extendedPane.getChildren().isEmpty() == false) {
        extendedPane.getChildren().remove(0);
      }
    }

  }

  @Override
  public void fromModel()
  {
    String typeId = model.getJdbcConntextionTypeId();
    JdbProviderService js = null;
    if (StringUtils.isNotBlank(typeId) == true) {
      js = SystemService.get().findJdbDriverById(typeId);
    }
    if (js == null) {
      js = SystemService.get().findJdbDriverByJdbcDriver(model.getDrivername());
    }
    if (js != null) {
      drivername.setValue(js.getName());
      if (StringUtils.isBlank(model.getUrl()) == true) {
        url.setText(js.getSampleUrl(model.getName()));
      }

    }
    super.fromModel();
  }

  JdbProviderService getSelectedDriver()
  {
    return SystemService.get().findJdbDriverByName(drivername.getValue());
  }

  @Override
  public void toModel()
  {
    JdbProviderService sel = getSelectedDriver();
    if (sel != null) {
      model.setDrivername(sel.getJdbcDriver());
      model.setJdbcConntextionTypeId(sel.getId());
    }
    super.toModel();
  }

  @Override
  public String getTabTitle()
  {
    return "JDBC";
  }

}
