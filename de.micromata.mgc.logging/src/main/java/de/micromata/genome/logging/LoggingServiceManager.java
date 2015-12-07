package de.micromata.genome.logging;

import de.micromata.genome.logging.spi.log4j.Log4JLogConfigurationDAOImpl;
import de.micromata.genome.logging.spi.log4j.Log4JLogging;
import de.micromata.genome.stats.NullStatsDAOImpl;
import de.micromata.genome.stats.StatsDAO;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingServiceManager
{
  private static LoggingServiceManager INSTANCE = new LoggingServiceManager();

  private Logging logging = new Log4JLogging();;
  private LogConfigurationDAO logConfigurationDAO = new Log4JLogConfigurationDAOImpl();
  private LogginContextService loggingContextService = new LoggingContextServiceDefaultImpl();
  private StatsDAO statsDAO = new NullStatsDAOImpl();

  public static LoggingServiceManager get()
  {
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

  public LogginContextService getLoggingContextService()
  {
    return loggingContextService;
  }

  public void setLoggingContextService(LogginContextService loggingContextService)
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
