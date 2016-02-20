package de.micromata.mgc.javafx.launcher.gui.generic;

import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.AbstractConfigDialog;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;

public class GenericMainWindow<M extends CastableLocalSettingsConfigModel>
    extends AbstractMainWindow<CastableLocalSettingsConfigModel>
{

  @Override
  protected Class<? extends AbstractConfigDialog<CastableLocalSettingsConfigModel>> getConfigurationDialogControlerClass()
  {
    return GenericConfigurationDialog.class;
  }

}
