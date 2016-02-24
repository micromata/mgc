package de.micromata.mgc.javafx.launcher.gui.jetty;

import org.apache.commons.lang.StringUtils;

import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import de.micromata.mgc.jettystarter.JettyConfigModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyConfigTabController extends AbstractConfigTabController<JettyConfigModel>
{
  @FXML
  private TextField serverPort;
  @FXML
  private TextField serverContextPath;

  @FXML
  private TextField publicUrl;

  @Override
  public void initializeWithModel(JettyConfigModel model)
  {
    fromModel(model);
    serverPort.textProperty().addListener((tc, oldVal, newVal) -> {
      String oldUrl = publicUrl.getText();
      String sc = serverContextPath.getText();
      String tu = "http://localhost:" + oldVal + sc;
      if (StringUtils.equals(oldUrl, tu) == true) {
        String nu = "http://localhost:" + newVal + sc;
        publicUrl.setText(nu);
      }
    });
  }

  @Override
  public void toModel(JettyConfigModel config)
  {
    config.setServerPort(serverPort.getText());
    config.setServerContextPath(serverContextPath.getText());
    config.setPublicUrl(publicUrl.getText());

  }

  @Override
  public void fromModel(JettyConfigModel config)
  {
    serverPort.setText(config.getServerPort());
    serverContextPath.setText(config.getServerContextPath());
    publicUrl.setText(config.getPublicUrl());

  }

  @Override
  public String getTabTitle()
  {
    return "Jetty Webserver";
  }

}
