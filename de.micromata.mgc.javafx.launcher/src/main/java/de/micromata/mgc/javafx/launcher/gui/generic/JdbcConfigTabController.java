package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderService;
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
  private ComboBox<String> drivername;

  @FXML
  private TextField url;

  @FXML
  private TextField username;
  @FXML
  private TextField password;
  private List<JdbProviderService> availableDrivers;

  @Override
  public void initializeWithModel()
  {

    availableDrivers = SystemService.get().getJdbcDrivers();
    List<String> items = new ArrayList<>();

    for (JdbProviderService sb : availableDrivers) {
      items.add(sb.getName());
    }
    drivername.setItems(FXCollections.observableArrayList(items));
    drivername.setOnAction(event -> {
      JdbProviderService dd = getSelectedDriver();
      if (dd != null) {
        url.setText(dd.getSampleUrl(model.getName()));
      }
    });

    fromModel();
  }

  @Override
  public void fromModel()
  {
    url.setText(model.getUrl());

    username.setText(model.getUsername());
    password.setText(model.getPassword());
    for (JdbProviderService desc : availableDrivers) {
      if (StringUtils.equals(model.getDrivername(), desc.getJdbcDriver()) == true) {
        drivername.setValue(desc.getName());
        if (StringUtils.isBlank(model.getUrl()) == true) {
          url.setText(desc.getSampleUrl(model.getName()));
        }
      }
    }

  }

  JdbProviderService getSelectedDriver()
  {
    for (JdbProviderService desc : availableDrivers) {
      if (desc.getName().equals(drivername.getValue()) == true) {
        return desc;
      }
    }
    return null;
  }

  @Override
  public void toModel()
  {
    JdbProviderService sel = getSelectedDriver();
    if (sel != null) {
      model.setDrivername(sel.getJdbcDriver());
    }
    model.setUrl(url.getText());
    model.setUsername(username.getText());
    model.setPassword(password.getText());

  }

  @Override
  public String getTabTitle()
  {
    return "JDBC";
  }

}
