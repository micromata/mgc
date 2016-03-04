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
