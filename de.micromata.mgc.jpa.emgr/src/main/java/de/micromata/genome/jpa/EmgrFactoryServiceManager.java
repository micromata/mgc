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
