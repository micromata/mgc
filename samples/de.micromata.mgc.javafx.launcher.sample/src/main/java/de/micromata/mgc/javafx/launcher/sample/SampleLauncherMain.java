package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.gui.AbstractMainWindow;
import de.micromata.mgc.javafx.launcher.gui.generic.GenericMainWindow;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLauncherMain extends MgcLauncher<SampleLocalSettingsConfigModel>
{
  public static void main(String[] args)
  {
    SampleLauncherMain el = new SampleLauncherMain();
    el.launch(args);
  }

  public SampleLauncherMain()
  {
    super(new SampleLauncherApplication(),
        (Class<? extends AbstractMainWindow<SampleLocalSettingsConfigModel>>) GenericMainWindow.class);
  }
}
