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

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.config.LoggingWithFallbackLocalSettingsConfigModel;
import de.micromata.genome.logging.config.LsLoggingLocalSettingsConfigModel;
import de.micromata.genome.logging.config.LsLoggingService.LsLoggingDescription;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import de.micromata.mgc.javafx.launcher.gui.AbstractModelController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingConfigTabController extends AbstractConfigTabController<LsLoggingLocalSettingsConfigModel>
{
  @FXML
  private Pane passToLog4JPane;
  @FXML
  private CheckBox log4JToGenomeLogging;

  @FXML
  private ComboBox<LsLoggingDescription> loggingType;

  @FXML
  private Label descriptionLabel;
  @FXML
  private Pane logContentPanel;

  private List<LsLoggingDescription> loggingDescriptions;
  private ModelController<BaseLoggingLocalSettingsConfigModel> detailController;
  private JdbcConfigTabController jdbcController;

  public boolean isFallback()
  {
    return StringUtils.equals(getId(), "fallbacklogtab");
  }

  @Override
  public void initializeWithModel()
  {

    if (isFallback() == true) {
      passToLog4JPane.setVisible(false);
    }

    loggingDescriptions = LsLoggingLocalSettingsConfigModel.getAvailableServices();
    //    List<String> list = loggingDescriptions.stream().map(e -> e.name()).collect(Collectors.toList());
    loggingType.setItems(FXCollections.observableArrayList(loggingDescriptions));
    loggingType.setOnAction(event -> onChangeLogType());
    fromModel();
    onChangeLogType();
    if (jdbcController != null) {
      JdbcLocalSettingsConfigModel jdbcconfig = model.getJdbcConfig();
      if (jdbcconfig != null) {
        jdbcController.fromModel();
      }
    }
  }

  private void onChangeLogType()
  {
    jdbcController = null;
    detailController = null;

    LsLoggingDescription lsLogdesc = loggingType.getValue();
    if (lsLogdesc == null) {
      return;
    }
    descriptionLabel.setText(lsLogdesc.description());
    BaseLoggingLocalSettingsConfigModel configModel = lsLogdesc.getConfigModel();
    model.setNested(configModel);
    configModel.fromLocalSettings(LocalSettings.get());
    logContentPanel.getChildren().clear();
    Class<? extends ModelController<BaseLoggingLocalSettingsConfigModel>> ctlClass = GenericConfigurationDialog
        .findForConfig(configModel);
    ControllerService controllerService = ControllerService.get();
    if (ctlClass != null) {

      Pair<Pane, ? extends ModelController<BaseLoggingLocalSettingsConfigModel>> wc = controllerService
          .loadControlWithModel(ctlClass, Pane.class, configModel, this);

      detailController = wc.getSecond();
      if (detailController instanceof AbstractModelController) {
        AbstractModelController<?> actl = (AbstractModelController) detailController;
        actl.setScene(getScene());
        actl.setStage(getStage());
      }
      if (detailController instanceof AbstractConfigTabController) {
        AbstractConfigTabController actl = (AbstractConfigTabController) detailController;
        actl.setConfigDialog(getConfigDialog());

      }

      detailController.initializeWithModel();
      if (detailController instanceof AbstractConfigTabController) {
        AbstractConfigTabController actl = (AbstractConfigTabController) detailController;
        actl.addToolTips();
      }
      logContentPanel.getChildren().add(wc.getFirst());
    }

    JdbcLocalSettingsConfigModel jdbcconf = configModel.getJdbcConfig();
    if (jdbcconf != null) {
      Pair<Pane, JdbcConfigTabController> pjdbc = controllerService
          .loadControlWithModel(JdbcConfigTabController.class, Pane.class, jdbcconf, this);
      jdbcController = pjdbc.getSecond();
      jdbcController.setTab(getTab());
      jdbcController.setFeedback(getFeedback());

      logContentPanel.getChildren().add(pjdbc.getFirst());
    }
    if (model.getNested() instanceof LoggingWithFallbackLocalSettingsConfigModel) {
      LoggingWithFallbackLocalSettingsConfigModel lfcm = (LoggingWithFallbackLocalSettingsConfigModel) model
          .getNested();
      if (lfcm.getFallbackConfig() == null) {
        lfcm.setFallbackConfig(new LsLoggingLocalSettingsConfigModel("genome.logging.fallback"));
      }
      addFallbackLogging(lfcm.getFallbackConfig());
    } else {
      removeFallbackLogging();
    }
  }

  protected void removeFallbackLogging()
  {
    if (isFallback() == true) {
      return;
    }
    configDialog.removeTab("fallbacklogtab");
  }

  protected void addFallbackLogging(LsLoggingLocalSettingsConfigModel model)
  {
    if (isFallback() == true) {
      return;
    }

    configDialog.addTab(LoggingConfigTabController.class, model, "fallbacklogtab", "Fallbacklog");

  }

  @Override
  public void fromModel()
  {
    log4JToGenomeLogging.setSelected(model.isLog4JToGenomeLogging());
    String typeId = model.getTypeId();
    Optional<LsLoggingDescription> optf = loggingDescriptions.stream().filter(e -> e.typeId().equals(typeId))
        .findFirst();
    if (optf.isPresent() == true) {
      loggingType.setValue(optf.get());
    }
  }

  @Override
  public void toModel()
  {
    model.setLog4JToGenomeLogging(Boolean.toString(log4JToGenomeLogging.isSelected()));
    LsLoggingDescription cd = loggingType.getValue();
    if (cd == null) {
      return;
    }
    model.setTypeId(cd.typeId());
    model.getNested().setTypeId(cd.typeId());
    //    modelObject.setNested(cd.getConfigModel());
    if (detailController != null) {
      model.setNested(detailController.getModel());
      model.getNested().setTypeId(cd.typeId());
      detailController.toModel();
    }
    if (jdbcController != null) {
      JdbcLocalSettingsConfigModel jdbconfig = model.getNested().getJdbcConfig();
      if (jdbconfig != null) {
        jdbcController.toModel();
      }
    }

  }

  @Override
  protected boolean isMessageReceiver(ValMessage msg)
  {
    if (super.isMessageReceiver(msg) == true) {
      return true;
    }
    if (jdbcController != null) {
      JdbcLocalSettingsConfigModel jdbcconfig = jdbcController.getModel();
      if (jdbcconfig != null && jdbcconfig == msg.getReference()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getTabTitle()
  {
    return "Logging";
  }

}
