package de.micromata.mgc.javafx.launcher.sample;

import org.apache.commons.codec.Charsets;

import de.micromata.genome.logging.config.LsLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.AbstractTextConfigFileConfigModel;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;
import de.micromata.mgc.email.MailReceiverLocalSettingsConfigModel;
import de.micromata.mgc.javafx.launcher.gui.generic.LauncherLocalSettingsConfigModel;

/**
 * The configuration model.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLocalSettingsConfigModel extends AbstractCompositLocalSettingsConfigModel
{
  /**
   * want to habe a launcher configuration.
   */
  private LauncherLocalSettingsConfigModel launcherConfig = new LauncherLocalSettingsConfigModel();
  /**
   * Configuration for the jetty itself.
   */
  private JettyConfigModel jettyConfig = new JettyConfigModel();

  /**
   * We want to make useage of the MGC Logging.
   */
  private LsLoggingLocalSettingsConfigModel loggerConfig = new LsLoggingLocalSettingsConfigModel();
  /**
   * Embededd log4j configuration inside the launchers configuration dialog.
   */
  private AbstractTextConfigFileConfigModel log4jConfig = new AbstractTextConfigFileConfigModel("Log4J",
      "log4j.properties", Charsets.ISO_8859_1);

  private MailReceiverLocalSettingsConfigModel mailReceiverConfig = new MailReceiverLocalSettingsConfigModel();

  private SampleConfigModel sampleConfig = new SampleConfigModel();
}
