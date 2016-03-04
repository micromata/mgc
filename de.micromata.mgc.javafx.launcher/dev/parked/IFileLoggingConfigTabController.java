package de.micromata.mgc.javafx.launcher.gui.generic;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.spi.ifiles.IFileLoggingLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IFileLoggingConfigTabController extends AbstractConfigTabController<IFileLoggingLocalSettingsConfigModel>
{
  @FXML
  private Button selectDirButton;

  @FXML
  private TextField logPath;

  @FXML
  private TextField sizeLimit;

  @FXML
  private TextField baseFileName;

  @Override
  public void initializeWithModel()
  {
    fromModel();
    selectDirButton.setOnAction(e -> {
      DirectoryChooser fileChooser = new DirectoryChooser();
      fileChooser.setInitialDirectory(getVerifiedLogPath());
      File res = fileChooser.showDialog(getConfigDialog().getStage());
      if (res != null) {
        logPath.setText(res.getAbsolutePath());
      }
    });
  }

  private File getVerifiedLogPath()
  {
    File file = getDefaultLogPath();
    if (file.exists() == true && file.isDirectory() == true) {
      return file;
    }
    if (file.exists() == false && file.getParentFile().exists() == true) {
      boolean created = file.mkdirs();
      if (created == true) {
        return file;
      }
    }
    return new File(".");
  }

  private File getDefaultLogPath()
  {
    if (StringUtils.isBlank(logPath.getText()) == true) {
      return new File("logs");
    }
    return new File(logPath.getText());
  }

  @Override
  public void fromModel()
  {
    logPath.setText(model.getLogPath());
    sizeLimit.setText(model.getSizeLimit());
    baseFileName.setText(model.getBaseFileName());

  }

  @Override
  public void toModel()
  {
    model.setLogPath(logPath.getText());
    model.setSizeLimit(sizeLimit.getText());
    model.setBaseFileName(baseFileName.getText());
  }

  @Override
  public String getTabTitle()
  {
    return "IFileLogging";
  }

}
