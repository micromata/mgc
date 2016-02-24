package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.mgc.javafx.JdbcDriverDescription;
import de.micromata.mgc.javafx.SystemService;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JdbcConfigTabController extends AbstractConfigTabController<JdbcLocalSettingsConfigModel>
{
  @FXML
  private ComboBox<String> jdbcDriver;

  @FXML
  private TextField jdbcUrl;

  @FXML
  private TextField jdbcUser;
  @FXML
  private TextField jdbcPassword;
  private List<JdbcDriverDescription> availableDrivers;

  @Override
  public void initializeWithModel(JdbcLocalSettingsConfigModel model)
  {

    availableDrivers = SystemService.get().getJdbcDrivers();
    List<String> items = new ArrayList<>();

    for (JdbcDriverDescription sb : availableDrivers) {
      items.add(sb.getDescription());
    }
    jdbcDriver.setItems(FXCollections.observableArrayList(items));
    jdbcDriver.setOnAction(event -> {
      JdbcDriverDescription dd = getSelectedDriver();
      if (dd != null) {
        jdbcUrl.setText(dd.getSampleUrlForApp(model.getName()));
      }
    });
  }

  @Override
  public void fromModel(JdbcLocalSettingsConfigModel modelObject)
  {
    jdbcUrl.setText(modelObject.getUrl());
    jdbcUser.setText(modelObject.getUsername());
    jdbcPassword.setText(modelObject.getPassword());
    for (JdbcDriverDescription desc : availableDrivers) {
      if (StringUtils.equals(modelObject.getDrivername(), desc.getDriverClassName()) == true) {
        jdbcDriver.setValue(desc.getDescription());
        if (StringUtils.isBlank(modelObject.getUrl()) == true) {
          jdbcUrl.setText(desc.getSampleUrlForApp(modelObject.getName()));
        }
      }
    }

  }

  JdbcDriverDescription getSelectedDriver()
  {
    for (JdbcDriverDescription desc : availableDrivers) {
      if (desc.getDescription().equals(jdbcDriver.getValue()) == true) {
        return desc;
      }
    }
    return null;
  }

  @Override
  public void toModel(JdbcLocalSettingsConfigModel modelObject)
  {
    JdbcDriverDescription sel = getSelectedDriver();
    if (sel != null) {
      modelObject.setDrivername(sel.getDriverClassName());
    }
    modelObject.setUrl(jdbcUrl.getText());
    modelObject.setUsername(jdbcUser.getText());
    modelObject.setPassword(jdbcPassword.getText());

  }

  @Override
  public String getTabTitle()
  {
    return "JDBC";
  }

}
