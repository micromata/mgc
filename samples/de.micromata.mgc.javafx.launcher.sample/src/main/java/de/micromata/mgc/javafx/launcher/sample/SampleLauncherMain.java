package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.gui.generic.GenericMainWindow;

/**
 * Sample Main class to start the application.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLauncherMain extends MgcLauncher<SampleLocalSettingsConfigModel>
{
  /**
   * Launch via standard java main.
   * 
   * @param args passed from command line.
   */
  public static void main(String[] args)
  {
    SampleLauncherMain el = new SampleLauncherMain();
    el.launch(args);
  }

  public SampleLauncherMain()
  {
    // configure the Launcher with application and the main JavaFX window.
    super(new SampleLauncherApplication(), (Class) GenericMainWindow.class);
  }
}
