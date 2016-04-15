package de.micromata.genome.chronos;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;

import de.micromata.genome.chronos.manager.DefaultChronosConfigurationServiceImpl;
import de.micromata.genome.chronos.manager.SchedulerDAO;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ChronosServiceManager
{
  private static final Logger LOG = Logger.getLogger(ChronosServiceManager.class);
  private static ChronosServiceManager DEFAULT_INSTANCE = new ChronosServiceManager();
  private static ChronosServiceManager INSTANCE;

  static {
    try {
      ServiceLoader<ChronosServiceManagerProvider> loader = ServiceLoader.load(ChronosServiceManagerProvider.class);
      if (loader.iterator().hasNext() == true) {
        ChronosServiceManagerProvider lps = loader.iterator().next();
        INSTANCE = lps.getChronosServiceManager();
      } else {
        INSTANCE = new ChronosServiceManager();
      }
    } catch (Exception ex) {
      LOG.fatal("Unable to load ChronosServiceManager: " + ex.getMessage(), ex);
      throw ex;
    }
  }

  public static ChronosServiceManager get()
  {
    if (INSTANCE == null) {
      return DEFAULT_INSTANCE;
    }
    return INSTANCE;
  }

  private SchedulerDAO schedulerDAO;

  private ChronosConfigurationService chronosConfigurationService = new DefaultChronosConfigurationServiceImpl();

  public SchedulerDAO getSchedulerDAO()
  {
    return schedulerDAO;
  }

  public void setSchedulerDAO(SchedulerDAO schedulerDAO)
  {
    this.schedulerDAO = schedulerDAO;
  }

  public ChronosConfigurationService getChronosConfigurationService()
  {
    return chronosConfigurationService;
  }

  public void setChronosConfigurationService(ChronosConfigurationService chronosConfigurationService)
  {
    this.chronosConfigurationService = chronosConfigurationService;
  }

}
