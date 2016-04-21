//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.mgc.application.webserver.config;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;
import de.micromata.mgc.javafx.feedback.ValMessageResultBox;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
  private ChoiceBox<String> listenHost;
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

  @FXML
  private Button generateSslCert;

  @Override
  public void initializeWithModel()
  {

    sslChildPane = sslPane.getChildren().get(0);
    fromModel();
    List<String> hosts = getListenHosts();
    hosts.add(0, "");
    listenHost.setItems(FXCollections.observableArrayList(hosts));
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
    generateSslCert.setOnAction(event -> {
      createSsl();
    });
  }

  private List<String> getListenHosts()
  {
    List<String> ret = new ArrayList<>();
    try {
      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        if (n.isUp() == false) {
          continue;
        }
        Enumeration ee = n.getInetAddresses();
        while (ee.hasMoreElements()) {
          InetAddress i = (InetAddress) ee.nextElement();
          //          ret.add(i.getHostName());
          ret.add(i.getHostAddress());
          //          System.out.println(i.getHostAddress());
        }
      }
    } catch (Exception ex) {
      ValMessage vm = new ValMessage(ValState.Error, "");
      vm.setMessage("Error retriving hosts: " + ex.getMessage());
      addToFeedback(vm);
    }
    return ret;
  }

  private void createSsl()
  {
    ValContext ctx = new ValContext().createSubContext(getModel(), null);
    createSslValidate(ctx);
    if (ctx.hasMessages() == true) {
      clearTabErros();
      ValMessageResultBox.showResultBox(ctx, "Certificate creation failed", "");
      //      getConfigDialog().mapValidationMessagesToGui(ctx);
    }
  }

  private void createSslValidate(ValContext ctx)
  {
    File storePath = new File(".");
    if (StringUtils.isNotBlank(sslKeystorePath.getText()) == true) {
      String sp = sslKeystorePath.getText();
      if (sp.contains("/") == false && sp.contains("\\") == false) {
        if (StringUtils.isBlank(sp) == true) {
          storePath = new File(".");
        } else {
          storePath = new File(new File("."), sp);
        }
      } else {
        storePath = new File(sslKeystorePath.getText());
      }
    }
    if (storePath.getParentFile() == null || storePath.getParentFile().exists() == false) {
      ctx.directError("sslKeystorePath",
          "The parent path doesn't exists: " + (storePath.getParentFile() == null ? "null"
              : storePath.getParentFile().getAbsolutePath()));
      return;
    }
    if (storePath.exists() && storePath.isDirectory() == true) {
      ctx.directError("sslKeystorePath",
          "Please give an path to file: " + storePath.getAbsolutePath());
      return;
    }
    if (storePath.exists() == true) {
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Overwrite keystore");
      //      alert.setHeaderText("Overwrite the existant keystore file?");
      alert.setContentText("Overwrite the existant keystore file?");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() != ButtonType.OK) {
        return;
      }
    }
    if (StringUtils.length(sslKeystorePassword.getText()) < 6) {
      ctx.directError("sslKeystorePassword", "Please give a password for Keystore password with at least 6 characters");
      return;
    }
    if (StringUtils.isBlank(sslCertAlias.getText()) == true) {
      ctx.directError("sslCertAlias", "Please give a Alias for the key");
      return;
    }
    KeyTool.generateKey(ctx, storePath, sslKeystorePassword.getText(), sslCertAlias.getText());
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Certificate created");
    alert.setHeaderText(
        "Certificate created. You are using a self signed certificate, which should not be used in production.");
    alert.setContentText(
        "If you open the browser, will will receive a warning, that the certificate is not secure.\n"
            + "You have to accept the certificate to continue.");
    Optional<ButtonType> result = alert.showAndWait();
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
    model.setListenHost(listenHost.getValue());
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
    listenHost.setValue(model.getListenHost());
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
