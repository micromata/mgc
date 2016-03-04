package de.micromata.mgc.javafx.launcher.gui;

import java.net.URL;
import java.util.ResourceBundle;

import de.micromata.genome.util.i18n.OptionalTranslationProvider;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.ValMessageEvent;
import de.micromata.mgc.javafx.feedback.FeedbackPanel;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <M>
 */
public abstract class AbstractConfigTabController<M extends LocalSettingsConfigModel>extends AbstractModelController<M>
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

  @Override
  public void registerValMessageReceivers()
  {
    super.registerValMessageReceivers();
    FXEvents.get().addEventHandler(this, getThisNode(), ValMessageEvent.MESSAGE_EVENT_TYPE, event -> {
      //      if (event.getMessage().getReference() != null
      //          && type.isAssignableFrom(event.getMessage().getReference().getClass()) == true) {
      if (isMessageReceiver(event.getMessage()) == true) {
        markTabWithError(event.getMessage());
      }
    });
  }

  protected boolean isMessageReceiver(ValMessage msg)
  {
    return msg.getReference() == getModel();
  }

  protected DialogBuilder dialogBuilder()
  {
    DialogBuilder builder = new DialogBuilder((Pane) getThisNode());
    builder.setTranslation(new OptionalTranslationProvider(MgcLauncher.get().getApplication().getTranslateService()));
    return builder;
  }

  public void clearTabErros()
  {
    getTab().setStyle("");
  }

  public void markTabWithError(ValMessage msg)
  {
    if (msg.getValState().isErrorOrWorse() == true) {
      getTab().setStyle("-fx-background-color: red");
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
