//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.logging;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;

import de.micromata.genome.logging.events.LoggingEventListenerRegistryService;
import de.micromata.genome.logging.events.LoggingEventListenerRegistryServiceImpl;
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

  private LoggingEventListenerRegistryService loggingEventListenerRegistryService = new LoggingEventListenerRegistryServiceImpl();

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

  public LoggingEventListenerRegistryService getLoggingEventListenerRegistryService()
  {
    return loggingEventListenerRegistryService;
  }

  public void setLoggingEventListenerRegistryService(
      LoggingEventListenerRegistryService loggingEventListenerRegistryService)
  {
    this.loggingEventListenerRegistryService = loggingEventListenerRegistryService;
  }

}
