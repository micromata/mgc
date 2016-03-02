package de.micromata.genome.logging.spi;

import java.util.List;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;

/**
 * Service has to registered by the JRE ServiceLoader infrastructure.
 * 
 * A LsLogging can be read/write its konfiguration in the local settings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LsLoggingService
{
  static public interface LsLoggingDescription
  {
    String id();

    String name();

    String description();

    Logging createLogging();

    LocalSettingsConfigModel getConfigModel();

  }

  List<LsLoggingDescription> getLsLoggingImpls();
}
