package de.micromata.mgc.javafx.launcher.gui;

import java.net.URL;
import java.util.ResourceBundle;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.GenericsUtils;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.feedback.FeedbackPanel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class AbstractConfigTabController<M extends LocalSettingsConfigModel>extends AbstractController<M>
    implements Initializable, ModelController<M>
{
  protected AbstractConfigDialog<?> configDialog;
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
  }

  protected void registerValMessage(String fieldName)
  {
    Class<?> type = GenericsUtils.getClassGenericTypeFromSuperClass(getClass(), 0, LocalSettingsConfigModel.class);
    Node node = (Node) PrivateBeanUtils.readField(this, fieldName);
    FXEvents.get().registerValMessageReceiver(this, node, type, fieldName);
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

}
