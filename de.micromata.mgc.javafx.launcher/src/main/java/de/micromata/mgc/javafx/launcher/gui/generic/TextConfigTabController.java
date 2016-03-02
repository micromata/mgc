package de.micromata.mgc.javafx.launcher.gui.generic;

import de.micromata.genome.util.runtime.config.AbstractTextConfigFileConfigModel;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import de.micromata.mgc.javafx.launcher.gui.DialogBuilder;
import javafx.fxml.FXML;
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
  public void initializeWithModel(AbstractTextConfigFileConfigModel model)
  {
    title = model.getTitle();
    DialogBuilder builder = dialogBuilder();
    DialogBuilder row = builder.addLabeledRow("FileName");

    row.addTextField("fileName", model.getFileName());
    row = builder.addRow();
    row.addTextArea("content", model.getContent());

  }

  @Override
  public void fromModel(AbstractTextConfigFileConfigModel modelObject)
  {
    DialogBuilder builder = dialogBuilder();
    builder.getTextInputControlById("content").setText(modelObject.getContent());
  }

  @Override
  public void toModel(AbstractTextConfigFileConfigModel modelObject)
  {
    DialogBuilder builder = dialogBuilder();
    modelObject.setContent(builder.getTextInputControlById("content").getText());

  }

  @Override
  protected DialogBuilder dialogBuilder()
  {
    DialogBuilder ret = super.dialogBuilder();
    ret.setParentPane(rowVBox);
    return ret;
  }
}