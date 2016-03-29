package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigTabController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleConfigTabController extends AbstractConfigTabController<SampleConfigModel>
{
  /**
   * ModelGuiField: Map to SampleConfigModel.myField
   */
  @FXML
  @ModelGuiField
  private TextField myValue;

  @Override
  public String getTabTitle()
  {
    return "Sample";
  }
}
