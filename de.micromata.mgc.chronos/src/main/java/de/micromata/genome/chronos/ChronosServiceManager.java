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

package de.micromata.genome.chronos;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;

import de.micromata.genome.chronos.manager.DefaultChronosConfigurationServiceImpl;
import de.micromata.genome.chronos.manager.RAMSchedulerDAOImpl;
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

  private SchedulerDAO schedulerDAO = new RAMSchedulerDAOImpl();

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
