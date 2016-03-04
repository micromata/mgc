package de.micromata.genome.logging.config;

import java.util.List;

import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;

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
    String typeId();

    @Override
    String toString();

    String description();

    BaseLoggingLocalSettingsConfigModel getConfigModel();

  }

  List<LsLoggingDescription> getLsLoggingImpls();
}
