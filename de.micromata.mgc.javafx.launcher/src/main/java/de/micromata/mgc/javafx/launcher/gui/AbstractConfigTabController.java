package de.micromata.mgc.javafx.launcher.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.i18n.OptionalTranslationProvider;
import de.micromata.genome.util.matcher.CommonMatchers;
import de.micromata.genome.util.runtime.GenericsUtils;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.ValMessageEvent;
import de.micromata.mgc.javafx.feedback.FeedbackPanel;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <M>
 */
public abstract class AbstractConfigTabController<M extends LocalSettingsConfigModel>extends AbstractController<M>
    implements Initializable, ModelController<M>
{
  protected AbstractConfigDialog<?> configDialog;
  protected Tab tab;
  protected Pane tabPane;
  /**
   * Feedback messages.
   */
  @FXML
  protected FeedbackPanel feedback;

  public abstract String getTabTitle();

  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {

  }

  @Override
  public void addToFeedback(ValMessage msg)
  {
    feedback.addMessage(msg);
    msg.consume();
  }

  protected void registerValMessage(String fieldName)
  {
    Class<?> type = GenericsUtils.getClassGenericTypeFromSuperClass(getClass(), 0, LocalSettingsConfigModel.class);
    Node node = (Node) PrivateBeanUtils.readField(this, fieldName);
    FXEvents.get().registerValMessageReceiver(this, node, type, fieldName);
  }

  protected void registerValMessageReceivers()
  {
    Class<?> type = GenericsUtils.getClassGenericTypeFromSuperClass(getClass(), 0, LocalSettingsConfigModel.class);
    List<Field> fields = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC), FieldMatchers.assignableTo(Control.class)));
    for (Field field : fields) {
      Control ctl = (Control) PrivateBeanUtils.readField(this, field);
      FXEvents.get().registerValMessageReceiver(this, ctl, type, field.getName());
    }
    FXEvents.get().addEventHandler(this, tabPane, ValMessageEvent.MESSAGE_EVENT_TYPE, event -> {
      if (event.getMessage().getReference() != null
          && type.isAssignableFrom(event.getMessage().getReference().getClass()) == true) {
        markTabWithError(event.getMessage());
      }
    });
  }

  protected DialogBuilder dialogBuilder()
  {
    DialogBuilder builder = new DialogBuilder(tabPane);
    builder.setTranslation(new OptionalTranslationProvider(MgcLauncher.get().getApplication().getTranslateService()));
    return builder;
  }

  public void clearTabErros()
  {
    tab.setStyle("");
  }

  public void markTabWithError(ValMessage msg)
  {
    if (msg.getValState().isErrorOrWorse() == true) {
      tab.setStyle("-fx-background-color: red");
    }
  }

  public Pane getTabPane()
  {
    return tabPane;
  }

  public void setTabPane(Pane tabPane)
  {
    this.tabPane = tabPane;
  }

  public AbstractConfigDialog<?> getConfigDialog()
  {
    return configDialog;
  }

  public void setConfigDialog(AbstractConfigDialog<?> configDialog)
  {
    this.configDialog = configDialog;
  }

  public FeedbackPanel getFeedback()
  {
    return feedback;
  }

  public void setFeedback(FeedbackPanel feedback)
  {
    this.feedback = feedback;
  }

  public Tab getTab()
  {
    return tab;
  }

  public void setTab(Tab tab)
  {
    this.tab = tab;
  }

}
