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

package de.micromata.genome.stats;

import javax.servlet.http.HttpServletRequest;

import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.LoggingServiceManager;

/**
 * Vereinfachtes statisches Interface fuer Stats.
 *
 * @author roger@micromata.de
 */
public class Stats
{

  /**
   * Instantiates a new stats.
   */
  public Stats()
  {
  }

  /**
   * Adds the performance.
   *
   * @param category the category
   * @param pointName the point name
   * @param millis the millis
   * @param wait the wait
   */
  public static void addPerformance(LogCategory category, String pointName, long millis, long wait)
  {
    if (LoggingServiceManager.isInitialized()) {
      LoggingServiceManager.get().getStatsDAO().addPerformance(category, pointName, millis, wait);
    }
  }

  /**
   * Adds the performance.
   *
   * @param category the category
   * @param pointName the point name
   * @param millis the millis
   */
  public static void addPerformance(LogCategory category, String pointName, long millis)
  {
    if (LoggingServiceManager.isInitialized() == true) {
      LoggingServiceManager.get().getStatsDAO().addPerformance(category, pointName, millis, 0);
    }
  }

  /**
   * Adds the logging.
   *
   * @param lwe the lwe
   */
  public static void addLogging(LogWriteEntry lwe)
  {
    if (LoggingServiceManager.isInitialized() == true) {
      LoggingServiceManager.get().getStatsDAO().addLogging(lwe);
    }
  }

  /**
   * Adds the request.
   *
   * @param req the req
   * @param millis the millis
   */
  public static void addRequest(HttpServletRequest req, long millis)
  {
    if (LoggingServiceManager.isInitialized() == true) {
      LoggingServiceManager.get().getStatsDAO().addRequest(req, millis);
    }

  }

}
