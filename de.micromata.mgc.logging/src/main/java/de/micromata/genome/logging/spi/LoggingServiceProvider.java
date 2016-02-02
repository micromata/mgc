package de.micromata.genome.logging.spi;

import de.micromata.genome.logging.LoggingServiceManager;

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
public interface LoggingServiceProvider
{

  /**
   * Gets the logging.
   *
   * @return the logging
   */
  LoggingServiceManager getLoggingServiceManager();
}
