package de.micromata.mgc.javafx.launcher.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Configuration of a launcher.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractConfigDialog<M extends LocalSettingsConfigModel>extends AbstractController<M>
    implements ModelController<M>
{
  private static final Logger LOG = Logger.getLogger(AbstractConfigDialog.class);
  /**
   * Preferred Scene Width.
   */
  public static final int PREF_WIDTH = 700;

  /**
   * Preferred Scene Height.
   */
  public static final int PREF_HEIGHT = 500;

  protected AbstractMainWindow<M> mainWindow;
  @FXML
  private TabPane configurationTabs;

  protected List<AbstractConfigTabController<?>> tabController = new ArrayList<>();

  protected M configModel;

  protected abstract List<TabConfig> getConfigurationTabs();

  @Override
  public void initializeWithModel(M model)
  {
    configModel = model;
    addCss();
    ControllerService cv = ControllerService.get();
    List<TabConfig> tabs = getConfigurationTabs();
    tabs.sort((e1, e2) -> Integer.compare(e1.prio, e2.prio));
    for (TabConfig tabc : tabs) {
      Pair<Pane, ? extends AbstractConfigTabController<?>> wc = cv.loadControl(tabc.tabControlerClass, Pane.class);
      AbstractConfigTabController<?> contrl = wc.getSecond();
      contrl.setConfigDialog(this);
      contrl.setTabPane(wc.getFirst());
      Tab tabB = new Tab();

      tabB.setText(contrl.getTabTitle());
      VBox vbox = new VBox();
      FeedbackPanel feedback = new FeedbackPanel();
      FXEvents.get().addEventHandler(this, feedback, FeedbackPanelEvents.CLEAR, event -> {
        feedback.clearMessages();
      });
      contrl.setFeedback(feedback);
      vbox.getChildren().add(wc.getFirst());
      vbox.getChildren().add(feedback);
      tabB.setContent(vbox);

      configurationTabs.getTabs().add(tabB);
      tabController.add(contrl);
      contrl.setTab(tabB);
      contrl.initWithModel(tabc.configModel);
      contrl.registerValMessageReceivers();
    }
  }

  protected void addCss()
  {
    stage.getScene().getStylesheets().add(FXCssUtil.CSS);
  }

  public void closeDialog()
  {
    stage.hide();
  }

  @FXML
  private void onCancel(ActionEvent event)
  {
    closeDialog();
  }

  @FXML
  private void onSave(ActionEvent event)
  {
    toModel(configModel);
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
  public void toModel(M modelObject)
  {
    if (modelObject instanceof CastableLocalSettingsConfigModel) {
      CastableLocalSettingsConfigModel cmc = (CastableLocalSettingsConfigModel) modelObject;
      for (AbstractConfigTabController ctl : tabController) {
        Class<? extends CastableLocalSettingsConfigModel> cf = (Class<? extends CastableLocalSettingsConfigModel>) GenericsUtils
            .getClassGenericTypeFromSuperClass(
                ctl.getClass(), 0, CastableLocalSettingsConfigModel.class);
        LocalSettingsConfigModel res = cmc.castTo(cf);
        ctl.toModel(res);
      }
    } else {
      throw new IllegalArgumentException(getClass().getName() + ".toModel() has to be implemented");
    }
  }

  @Override
  public void fromModel(M modelObject)
  {
    if (modelObject instanceof CastableLocalSettingsConfigModel) {
      CastableLocalSettingsConfigModel cmc = (CastableLocalSettingsConfigModel) modelObject;
      for (AbstractConfigTabController ctl : tabController) {
        Class<? extends LocalSettingsConfigModel> cf = (Class<? extends LocalSettingsConfigModel>) GenericsUtils
            .getClassGenericTypeFromSuperClass(
                ctl.getClass(), 0, LocalSettingsConfigModel.class);
        LocalSettingsConfigModel res = cmc.castTo(cf);
        ctl.fromModel(res);
      }
    } else {
      throw new IllegalArgumentException(getClass().getName() + ".fromModel() has to be implemented");
    }
  }

  @Override
  public void addToFeedback(ValMessage msg)
  {
    System.err.println("Not yet implemented");
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
        LOG.error("ValMessage unconsumed: " + msg.getI18nkey());
      }
    }
  }

}
