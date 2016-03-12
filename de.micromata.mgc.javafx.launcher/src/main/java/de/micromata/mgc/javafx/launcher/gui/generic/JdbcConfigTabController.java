package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderService;
import de.micromata.mgc.javafx.ModelGuiField;
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
 * Configuration tab for a jdbc connection.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JdbcConfigTabController extends AbstractConfigTabController<JdbcLocalSettingsConfigModel>
{
  @FXML
  private ComboBox<String> drivername;

  @FXML
  @ModelGuiField
  private TextField url;

  @FXML
  @ModelGuiField
  private TextField username;
  @FXML
  @ModelGuiField
  private TextField password;

  private List<JdbProviderService> availableDrivers;

  @FXML
  @ModelGuiField
  private CheckBox extendedSettings;

  @FXML
  private Pane extendedPane;
  private Node extendedChildPane;

  @FXML
  @ModelGuiField
  private TextField maxActive;
  @FXML
  @ModelGuiField
  private TextField intialSize;
  @FXML
  @ModelGuiField
  private TextField maxWait;
  @FXML
  @ModelGuiField
  private TextField maxIdle;
  @FXML
  @ModelGuiField
  private TextField minIdle;
  @FXML
  @ModelGuiField
  private CheckBox defaultAutoCommit;
  @FXML
  @ModelGuiField
  private TextField defaultCatalog;
  @FXML
  @ModelGuiField
  private TextField validationQuery;
  @FXML
  @ModelGuiField
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

    for (JdbProviderService desc : availableDrivers) {
      if (StringUtils.equals(model.getDrivername(), desc.getJdbcDriver()) == true) {
        drivername.setValue(desc.getName());
        if (StringUtils.isBlank(model.getUrl()) == true) {
          url.setText(desc.getSampleUrl(model.getName()));
        }
      }
    }
    super.fromModel();
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
    super.toModel();
  }

  @Override
  public String getTabTitle()
  {
    return "JDBC";
  }

}
