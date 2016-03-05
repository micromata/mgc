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
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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

  @FXML
  private CheckBox extendedSettings;

  @FXML
  private Pane extendedPane;
  private Node extendedChildPane;

  @FXML
  private TextField maxActive;
  @FXML
  private TextField intialSize;
  @FXML
  private TextField maxWait;
  @FXML
  private TextField maxIdle;
  @FXML
  private TextField minIdle;
  @FXML
  private CheckBox defaultAutoCommit;
  @FXML
  private TextField defaultCatalog;
  @FXML
  private TextField validationQuery;
  @FXML
  private TextField validationQueryTimeout;

  @Override
  public void initializeWithModel()
  {
    extendedChildPane = extendedPane.getChildren().get(0);
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
    extendedSettings.setOnAction(event -> {
      onShowExtended(extendedSettings.isSelected());
    });
    fromModel();

    onShowExtended(extendedSettings.isSelected());
  }

  private void onShowExtended(boolean show)
  {
    extendedPane.setVisible(show);
    if (show == true) {
      if (extendedPane.getChildren().isEmpty() == true) {
        extendedPane.getChildren().add(extendedChildPane);
      }
    } else {
      if (extendedPane.getChildren().isEmpty() == false) {
        extendedPane.getChildren().remove(0);
      }
    }

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
    extendedSettings.setSelected(model.isExtendedSettings());
    maxActive.setText(model.getMaxActive());
    intialSize.setText(model.getIntialSize());
    maxWait.setText(model.getMaxWait());
    maxIdle.setText(model.getMaxIdle());
    minIdle.setText(model.getMinIdle());
    defaultAutoCommit.setSelected(model.isDefaultAutoCommit());
    defaultCatalog.setText(model.getDefaultCatalog());
    validationQuery.setText(model.getValidationQuery());
    validationQueryTimeout.setText(model.getValidationQueryTimeout());
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
    model.setExtendedSettings(extendedSettings.isSelected());
    model.setMaxActive(maxActive.getText());
    model.setIntialSize(intialSize.getText());
    model.setMaxWait(maxWait.getText());
    model.setMaxIdle(maxIdle.getText());
    model.setMinIdle(minIdle.getText());
    model.setDefaultAutoCommit(defaultAutoCommit.isSelected());
    model.setDefaultCatalog(defaultCatalog.getText());
    model.setValidationQuery(validationQuery.getText());
    model.setValidationQueryTimeout(validationQueryTimeout.getText());
  }

  @Override
  public String getTabTitle()
  {
    return "JDBC";
  }

}
