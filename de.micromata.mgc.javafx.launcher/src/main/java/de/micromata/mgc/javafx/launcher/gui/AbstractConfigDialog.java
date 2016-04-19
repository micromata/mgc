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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.runtime.GenericsUtils;
import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.FXCssUtil;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.FXGuiUtils;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.ValMessageEvent;
import de.micromata.mgc.javafx.feedback.FeedbackPanel;
import de.micromata.mgc.javafx.feedback.FeedbackPanelEvents;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Configuration of a launcher.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractConfigDialog<M extends LocalSettingsConfigModel>extends AbstractModelController<M>
{
  private static final Logger LOG = Logger.getLogger(AbstractConfigDialog.class);
  /**
   * Preferred Scene Width.
   */
  public static final int PREF_WIDTH = 700;

  /**
   * Preferred Scene Height.
   */
  public static final int PREF_HEIGHT = 600;

  protected AbstractMainWindow<M> mainWindow;
  @FXML
  private AnchorPane mainAnchorPane;
  @FXML
  private Pane mainPane;
  @FXML
  private HBox buttonPane;
  @FXML
  private TabPane configurationTabs;

  protected List<AbstractConfigTabController<?>> tabController = new ArrayList<>();

  protected M configModel;

  protected abstract List<TabConfig> getConfigurationTabs();

  @Override
  public void initializeWithModel()
  {
    configModel = model;
    addCss();
    ControllerService cv = ControllerService.get();
    List<TabConfig> tabs = getConfigurationTabs();
    tabs.sort((e1, e2) -> Integer.compare(e1.prio, e2.prio));
    for (TabConfig tabc : tabs) {
      Pair<Pane, ? extends AbstractConfigTabController<?>> wc = cv.loadControllerControl(tabc.tabControlerClass,
          Pane.class, this);
      createTab(tabc.configModel, wc);
    }
    AnchorPane.setTopAnchor(mainPane, 5.0);
    AnchorPane.setRightAnchor(mainPane, 5.0);
    AnchorPane.setLeftAnchor(mainPane, 5.0);
    AnchorPane.setBottomAnchor(mainPane, 50.0);

    AnchorPane.setTopAnchor(configurationTabs, 5.0);
    AnchorPane.setRightAnchor(configurationTabs, 5.0);
    AnchorPane.setLeftAnchor(configurationTabs, 5.0);
    AnchorPane.setBottomAnchor(configurationTabs, 5.0);

    //    AnchorPane.setTopAnchor(buttonHBox, 5.0);
    AnchorPane.setRightAnchor(buttonPane, 5.0);
    AnchorPane.setLeftAnchor(buttonPane, 5.0);
    AnchorPane.setBottomAnchor(buttonPane, 5.0);

  }

  private Tab createTab(LocalSettingsConfigModel configModel, Pair<Pane, ? extends AbstractConfigTabController<?>> wc)
  {
    AbstractConfigTabController<?> contrl = wc.getSecond();
    contrl.setConfigDialog(this);

    Pane tabPane = wc.getFirst();

    contrl.setTabPane(tabPane);
    Tab tabB = new Tab();

    AnchorPane tabContentPane = new AnchorPane();
    tabContentPane.setMaxHeight(Integer.MAX_VALUE);
    //    tabContentPane.setPrefHeight(500);
    FeedbackPanel feedback = new FeedbackPanel();

    feedback.setPrefHeight(100);
    feedback.setMinHeight(100);
    FXEvents.get().addEventHandler(this, feedback, FeedbackPanelEvents.CLEAR, event -> {
      feedback.clearMessages();
    });
    contrl.setFeedback(feedback);
    tabContentPane.getChildren().add(tabPane);
    tabContentPane.getChildren().add(feedback);
    AnchorPane.setTopAnchor(tabPane, 2.0);
    AnchorPane.setRightAnchor(tabPane, 0.0);
    AnchorPane.setLeftAnchor(tabPane, 0.0);
    AnchorPane.setBottomAnchor(tabPane, 70.0);
    AnchorPane.setBottomAnchor(feedback, 0.0);

    tabB.setContent(tabContentPane);
    AnchorPane.setTopAnchor(tabContentPane, 0.0);
    AnchorPane.setRightAnchor(tabContentPane, 0.0);
    AnchorPane.setLeftAnchor(tabContentPane, 0.0);
    AnchorPane.setBottomAnchor(tabContentPane, 0.0);

    Node scrollPane = tabPane.getChildren().get(0);
    AnchorPane.setTopAnchor(scrollPane, 0.0);
    AnchorPane.setRightAnchor(scrollPane, 0.0);
    AnchorPane.setLeftAnchor(scrollPane, 0.0);
    AnchorPane.setBottomAnchor(scrollPane, 0.0);

    configurationTabs.getTabs().add(tabB);
    tabController.add(contrl);
    contrl.setTab(tabB);
    ((ModelController) contrl).setModel(configModel);
    contrl.initializeWithModel();
    contrl.addToolTips();
    tabB.setText(contrl.getTabTitle());
    contrl.registerValMessageReceivers();

    return tabB;
  }

  public boolean removeTab(String id)
  {
    for (Tab tab : configurationTabs.getTabs()) {
      if (StringUtils.equals(tab.getId(), id) == true) {
        configurationTabs.getTabs().remove(tab);
        return true;
      }
    }
    return false;
  }

  public AbstractConfigTabController findConfigTabById(String id)
  {
    for (Tab tab : configurationTabs.getTabs()) {
      if (StringUtils.equals(tab.getId(), id) == true) {
        TabPane tabPane = tab.getTabPane();

      }
    }
    return null;
  }

  public <M extends LocalSettingsConfigModel> boolean addTab(Class<? extends AbstractConfigTabController<M>> controller,
      M model,
      String id, String title)
  {
    ControllerService cv = ControllerService.get();

    Pair<Pane, ? extends AbstractConfigTabController<M>> wc = cv.loadControllerControl(controller, Pane.class, this,
        id);
    Tab tab = createTab(model, wc);
    tab.setId(id);
    tab.setText(title);
    return true;
  }

  protected void addCss()
  {
    ObservableList<String> list = getParent().getScene().getStylesheets();
    if (list.indexOf(FXCssUtil.CSS) == -1) {
      list.add(FXCssUtil.CSS);
    }
    list = getScene().getStylesheets();
    if (list.indexOf(FXCssUtil.CSS) == -1) {
      list.add(FXCssUtil.CSS);
    }
  }

  public void closeDialog()
  {
    stage.hide();
  }

  @FXML
  private void onCancel(ActionEvent event)
  {
    mainWindow.reloadConfig();
    closeDialog();
  }

  @FXML
  private void onSave(ActionEvent event)
  {
    toModel();
    FXGuiUtils.resetErroneousFields(this);
    ValContext ctx = new ValContext();
    FXEvents.get().fireEvent(new FeedbackPanelEvents(FeedbackPanelEvents.CLEAR));
    configModel.validate(ctx);
    ctx.translateMessages(MgcLauncher.get().getApplication().getTranslateService());
    if (ctx.hasErrors() == true) {
      mapValidationMessagesToGui(ctx);
      return;
    } else {
      mainWindow.getApplication().storeConfig(ctx, configModel);
      if (ctx.hasErrors() == true) {
        mapValidationMessagesToGui(ctx);
        return;
      }
      mainWindow.getApplication().reInit();
      closeDialog();
    }
  }

  @Override
  public void toModel()
  {
    if (model instanceof CastableLocalSettingsConfigModel) {
      CastableLocalSettingsConfigModel cmc = (CastableLocalSettingsConfigModel) model;
      for (AbstractConfigTabController ctl : tabController) {
        Class<? extends CastableLocalSettingsConfigModel> cf = (Class<? extends CastableLocalSettingsConfigModel>) GenericsUtils
            .getClassGenericTypeFromSuperClass(
                ctl.getClass(), 0, CastableLocalSettingsConfigModel.class);
        LocalSettingsConfigModel res = cmc.castTo(cf);
        ctl.toModel();
      }
    } else {
      throw new IllegalArgumentException(getClass().getName() + ".toModel() has to be implemented");
    }
  }

  @Override
  public void fromModel()
  {
    if (model instanceof CastableLocalSettingsConfigModel) {
      CastableLocalSettingsConfigModel cmc = (CastableLocalSettingsConfigModel) model;
      for (AbstractConfigTabController ctl : tabController) {
        Class<? extends LocalSettingsConfigModel> cf = (Class<? extends LocalSettingsConfigModel>) GenericsUtils
            .getClassGenericTypeFromSuperClass(
                ctl.getClass(), 0, LocalSettingsConfigModel.class);
        LocalSettingsConfigModel res = cmc.castTo(cf);
        ctl.setModel(res);
        ctl.fromModel();
      }
    } else {
      throw new IllegalArgumentException(getClass().getName() + ".fromModel() has to be implemented");
    }
  }

  @Override
  public void addToFeedback(ValMessage msg)
  {
    LOG.error("addToFeedback Not implemented");
  }

  @Override
  public void mapValidationMessagesToGui(ValContext ctx)
  {
    for (AbstractConfigTabController<?> tc : tabController) {
      tc.clearTabErros();
    }
    for (ValMessage msg : ctx.getMessages()) {
      FXEvents.get().fireEvent(new ValMessageEvent(msg));

    }
    for (ValMessage msg : ctx.getMessages()) {
      if (msg.isConsumed() == false) {
        GLog.warn(GenomeLogCategory.Configuration,
            "ValMessage unconsumed: " + (msg.getI18nkey() != null ? msg.getI18nkey() : msg.getMessage()));
      }
    }
  }

}
