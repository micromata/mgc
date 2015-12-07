/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   21.03.2008
// Copyright Micromata 21.03.2008
//
/////////////////////////////////////////////////////////////////////////////
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
