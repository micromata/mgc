package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.generic.LauncherLocalSettingsConfigModel;
import de.micromata.mgc.jettystarter.JettyConfigModel;

/*
 * A configuration Modell
 */
public class SampleLocalSettingsConfigModel extends AbstractCompositLocalSettingsConfigModel
{
  private LauncherLocalSettingsConfigModel launcherConfig = new LauncherLocalSettingsConfigModel();

  private JettyConfigModel jettyConfig = new JettyConfigModel();

}
