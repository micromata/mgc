package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.Arrays;
import java.util.List;

import de.micromata.genome.util.runtime.config.HibernateSchemaConfigModel;
import de.micromata.genome.util.validation.ValContext;
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
  public void initializeWithModel(HibernateSchemaConfigModel model)
  {
    fromModel(model);
    List<String> values = Arrays.asList("update", "validate", "create", "create-drop");
    schemaUpdate.setItems(FXCollections.observableArrayList(values));
  }

  @Override
  public void fromModel(HibernateSchemaConfigModel modelObject)
  {
    schemaUpdate.setValue(modelObject.getSchemaUpdate());
    showSql.setSelected(modelObject.isShowSql());
    formatSql.setSelected(modelObject.isFormatSql());

  }

  @Override
  public void toModel(HibernateSchemaConfigModel modelObject)
  {
    modelObject.setSchemaUpdate(schemaUpdate.getValue());
    modelObject.setShowSql(showSql.isSelected());
    modelObject.setFormatSql(formatSql.isSelected());

  }

  @Override
  public void mapValidationMessagesToGui(ValContext ctx)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String getTabTitle()
  {
    return "Hibernate Persistence";
  }

}
