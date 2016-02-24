package de.micromata.genome.jpa.spi;

import de.micromata.genome.jpa.EmgrFactoryServiceManager;

/**
 * Provides a LoggingServiceManager.
 *
 * If non is provided use the default LoggingServiceManager.
 *
 * This Service should be make available via the Java ServiceLoader.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface EmgrFactoryServiceProvider
{

  /**
   * Gets the logging.
   *
   * @return the logging
   */
  EmgrFactoryServiceManager getEmgrFactoryServiceManager();
}
