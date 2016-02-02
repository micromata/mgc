package de.micromata.genome.logging;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;

import de.micromata.genome.logging.spi.LoggingServiceProvider;
import de.micromata.genome.logging.spi.log4j.Log4JLogConfigurationDAOImpl;
import de.micromata.genome.logging.spi.log4j.Log4JLogging;
import de.micromata.genome.stats.NullStatsDAOImpl;
import de.micromata.genome.stats.StatsDAO;

/**
 * Service Manager for Logging.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingServiceManager
{
  private static final Logger LOG = Logger.getLogger(LoggingServiceManager.class);

  private static LoggingServiceManager DEFAULT_INSTANCE = new LoggingServiceManager();
  private static LoggingServiceManager INSTANCE;

  private Logging logging = new Log4JLogging();

  private LogConfigurationDAO logConfigurationDAO = new Log4JLogConfigurationDAOImpl();

  private LoggingContextService loggingContextService = new LoggingContextServiceDefaultImpl();

  private StatsDAO statsDAO = new NullStatsDAOImpl();

  static {
    try {
      ServiceLoader<LoggingServiceProvider> loader = ServiceLoader.load(LoggingServiceProvider.class);
      if (loader.iterator().hasNext() == true) {
        LoggingServiceProvider lps = loader.iterator().next();
        INSTANCE = lps.getLoggingServiceManager();
      } else {
        INSTANCE = new LoggingServiceManager();
      }
    } catch (Exception ex) {
      LOG.fatal("Unable to load LoggingServiceManager: " + ex.getMessage(), ex);
      throw ex;
    }
  }

  public static LoggingServiceManager get()
  {
    if (INSTANCE == null) {
      return DEFAULT_INSTANCE;
    }
    return INSTANCE;
  }

  public static boolean isInitialized()
  {
    return true;
  }

  public Logging getLogging()
  {
    return logging;
  }

  public void setLogging(Logging logging)
  {
    this.logging = logging;
  }

  public LogConfigurationDAO getLogConfigurationDAO()
  {
    return logConfigurationDAO;
  }

  public void setLogConfigurationDAO(LogConfigurationDAO logConfigurationDAO)
  {
    this.logConfigurationDAO = logConfigurationDAO;
  }

  public LoggingContextService getLoggingContextService()
  {
    return loggingContextService;
  }

  public void setLoggingContextService(LoggingContextService loggingContextService)
  {
    this.loggingContextService = loggingContextService;
  }

  public StatsDAO getStatsDAO()
  {
    return statsDAO;
  }

  public void setStatsDAO(StatsDAO statsDAO)
  {
    this.statsDAO = statsDAO;
  }

}
