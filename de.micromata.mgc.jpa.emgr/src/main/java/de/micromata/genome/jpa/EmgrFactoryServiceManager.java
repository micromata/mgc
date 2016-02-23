package de.micromata.genome.jpa;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;

import de.micromata.genome.jpa.spi.EmgrFactoryServiceProvider;

/**
 * Service Manager for Logging.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author lado GENOME-1689
 *
 */
public class EmgrFactoryServiceManager
{

  /** The Constant LOG. */
  private static final Logger LOG = Logger.getLogger(EmgrFactoryServiceManager.class);

  /** The default instance. */
  private static EmgrFactoryServiceManager DEFAULT_INSTANCE = new EmgrFactoryServiceManager();

  /** The instance. */
  private static EmgrFactoryServiceManager INSTANCE;

  /** The emgr factory service. */
  private EmgrFactoryService emgrFactoryService = new EmgrFactoryServiceImpl();

  static {
    try {
      ServiceLoader<EmgrFactoryServiceProvider> loader = ServiceLoader.load(EmgrFactoryServiceProvider.class);
      if (loader.iterator().hasNext() == true) {
        EmgrFactoryServiceProvider lps = loader.iterator().next();
        INSTANCE = lps.getEmgrFactoryServiceManager();
      } else {
        INSTANCE = new EmgrFactoryServiceManager();
      }
    } catch (Exception ex) {
      LOG.fatal("Unable to load LoggingServiceManager: " + ex.getMessage(), ex);
      throw ex;
    }
  }

  /**
   * Gets the.
   *
   * @return the emgr factory service manager
   */
  public static EmgrFactoryServiceManager get()
  {
    if (INSTANCE == null) {
      return DEFAULT_INSTANCE;
    }
    return INSTANCE;
  }

  /**
   * Checks if is initialized.
   *
   * @return true, if is initialized
   */
  public static boolean isInitialized()
  {
    return true;
  }

  /**
   * Gets the emgr factory service.
   *
   * @return the emgr factory service
   */
  public EmgrFactoryService getEmgrFactoryService()
  {
    return emgrFactoryService;
  }

  /**
   * Sets the emgr factory service.
   *
   * @param emgrFactoryService the new emgr factory service
   */
  public void setEmgrFactoryService(EmgrFactoryService emgrFactoryService)
  {
    this.emgrFactoryService = emgrFactoryService;
  }

}
