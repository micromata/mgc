package de.micromata.mgc.application.webserver.config;

import org.apache.commons.lang.StringUtils;

import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Config dialog for Jetty.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyConfigTabController extends AbstractConfigTabController<JettyConfigModel>
{
  @FXML
  private TextField port;
  @FXML
  private TextField contextPath;

  @FXML
  private TextField publicUrl;

  @FXML
  private CheckBox serverEnableJmx;
  @FXML
  private CheckBox serverRequestLoggingEnabled;

  @FXML
  private CheckBox sslEnabled;

  @FXML
  private Pane sslPane;
  private Node sslChildPane;
  @FXML
  private TextField sslPort;

  @FXML
  private CheckBox sslOnly;

  @FXML
  private TextField sslKeystorePath;
  @FXML
  private TextField sslKeystorePassword;
  @FXML
  private TextField sslKeyManagerPassword;
  @FXML
  private TextField trustStorePath;
  @FXML
  private TextField trustStorePassword;
  @FXML
  private TextField sslCertAlias;

  @Override
  public void initializeWithModel()
  {
    sslChildPane = sslPane.getChildren().get(0);
    fromModel();
    port.textProperty().addListener((tc, oldVal, newVal) -> {
      String oldUrl = publicUrl.getText();
      String sc = contextPath.getText();
      String tu = "http://localhost:" + oldVal + sc;
      if (StringUtils.equals(oldUrl, tu) == true) {
        String nu = "http://localhost:" + newVal + sc;
        publicUrl.setText(nu);
      }
    });
    sslEnabled.setOnAction(event -> {
      onSslEnabled(sslEnabled.isSelected());
    });

    onSslEnabled(model.isSslEnabled());
  }

  private void onSslEnabled(boolean enabled)
  {
    sslPane.setVisible(enabled);
    if (enabled == true) {
      if (sslPane.getChildren().isEmpty() == true) {
        sslPane.getChildren().add(sslChildPane);
      }
    } else {
      if (sslPane.getChildren().isEmpty() == false) {
        sslPane.getChildren().remove(0);
      }
    }

  }

  @Override
  public void toModel()
  {
    model.setPort(port.getText());
    model.setContextPath(contextPath.getText());
    model.setPublicUrl(publicUrl.getText());
    model.setServerEnableJmx(serverEnableJmx.isSelected());
    model.setServerRequestLoggingEnabled(serverRequestLoggingEnabled.isSelected());

    model.setSslEnabled(sslEnabled.isSelected());
    model.setSslPort(sslPort.getText());
    model.setSslOnly(sslOnly.isSelected());
    model.setSslKeystorePath(sslKeystorePath.getText());
    model.setSslKeystorePassword(sslKeystorePassword.getText());
    model.setSslKeyManagerPassword(sslKeyManagerPassword.getText());
    model.setTrustStorePath(trustStorePath.getText());
    model.setTrustStorePassword(trustStorePassword.getText());
    model.setSslCertAlias(sslCertAlias.getText());
  }

  @Override
  public void fromModel()
  {
    port.setText(model.getPort());
    contextPath.setText(model.getContextPath());
    publicUrl.setText(model.getPublicUrl());
    serverEnableJmx.setSelected(model.isServerEnableJmx());
    serverRequestLoggingEnabled.setSelected(model.isServerRequestLoggingEnabled());

    sslEnabled.setSelected(model.isSslEnabled());
    sslPort.setText(model.getSslPort());
    sslOnly.setSelected(model.isSslOnly());
    sslKeystorePath.setText(model.getSslKeystorePath());
    sslKeystorePassword.setText(model.getSslKeystorePassword());
    sslKeyManagerPassword.setText(model.getSslKeyManagerPassword());
    trustStorePath.setText(model.getTrustStorePath());
    trustStorePassword.setText(model.getTrustStorePassword());
    sslCertAlias.setText(model.getSslCertAlias());

  }

  @Override
  public String getTabTitle()
  {
    return "Jetty Webserver";
  }

}
