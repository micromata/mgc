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

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

import de.micromata.genome.stats.Stats;

/**
 * Sammelt Performance Daten.
 *
 * @author roger
 */
public class PerformanceCollector
{

  /**
   * The sampling count.
   */
  public static long SAMPLING_COUNT = 1000;

  /**
   * Sollen noch die einzelnen Perf-Eintraege in das Logging geschrieben werden.
   */
  public static boolean doLogPerformance = false;

  /**
   * The Class PerformanceElement.
   */
  public static class PerformanceElement
  {

    /**
     * The count.
     */
    int count = 0;

    /**
     * The min time.
     */
    long minTime = -1;

    /**
     * The max time.
     */
    long maxTime = 0;

    /**
     * The sum time.
     */
    long sumTime;

    /**
     * Adds the time.
     *
     * @param time the time
     * @return true, if successful
     */
    boolean addTime(long time)
    {
      ++count;
      sumTime += time;
      if (maxTime < time) {
        maxTime = time;
      }
      if (minTime == -1 || minTime > time) {
        minTime = time;
      }
      return count >= SAMPLING_COUNT;
    }

    long getAverage()
    {
      if (count == 0) {
        return 0;
      }
      return sumTime / count;
    }
  }

  /**
   * The data.
   */
  private static Map<String, PerformanceElement> data = new HashMap<String, PerformanceElement>();

  /**
   * Adds the.
   *
   * @param ptype the ptype
   * @param watch the watch
   */
  public static void add(PerformanceType ptype, StopWatch watch)
  {
    watch.stop();
    addIntern(ptype.name(), watch.getTime(), 0, ptype.getStatsCategory());
  }

  /**
   * Adds the.
   *
   * @param ptype the ptype
   * @param watch the watch
   * @param category the category
   */
  public static void add(String ptype, StopWatch watch, LogCategory category)
  {
    watch.stop();
    addIntern(ptype, watch.getTime(), 0, category);
  }

  /**
   * Adds the.
   *
   * @param ptype the ptype
   * @param watch the watch
   * @param category the category
   * @param waitTime the wait time
   */
  public static void add(String ptype, StopWatch watch, LogCategory category, long waitTime)
  {
    watch.stop();
    addIntern(ptype, watch.getTime(), waitTime, category);
  }

  /**
   * Adds the.
   *
   * @param sqlStatement the sql statement
   * @param watch the watch
   * @param dbConnection the db connection
   */
  public static void add(String sqlStatement, StopWatch watch, Connection dbConnection)
  {
    watch.stop();
    LogCategory cat = GenomeLogCategory.Database;
    addIntern(sqlStatement, watch.getTime(), 0, cat);
  }

  /**
   * Start performance log.
   *
   * @return the stop watch
   */
  public static StopWatch startPerformanceLog()
  {
    StopWatch s = new StopWatch();
    s.start();
    return s;
  }

  /**
   * Adds the intern.
   *
   * @param ptype the ptype
   * @param millis the millis
   * @param waitTime the wait time
   * @param category the category
   */
  private static void addIntern(String ptype, long millis, long waitTime, LogCategory category)
  {
    if (ptype == null) {
      return;
    }

    Stats.addPerformance(category, ptype, millis, waitTime);
    // TODO genome do this as a StatsDAO implementation
    if (doLogPerformance == false) {
      return;
    }

    PerformanceElement pe;
    PerformanceElement logPe = null;
    synchronized (PerformanceCollector.class) {
      if (data.containsKey(ptype) == false) {
        pe = new PerformanceElement();
        data.put(ptype, pe);
      } else {
        pe = data.get(ptype);
      }
      if (pe.addTime(millis) == true) {
        logPe = pe;
        data.put(ptype, new PerformanceElement());
      }
    }
    if (logPe != null) {
      writePerfLog(ptype, logPe);
    }
  }

  /**
   * Write perf log.
   *
   * @param ptype the ptype
   * @param logPe the log pe
   */
  private static void writePerfLog(String ptype, PerformanceElement logPe)
  {
    if (logPe == null) {
      return;
    }
    /**
     * @logging
     * @reason Für SQL, Requests und Aufrufe von externen Statistiken wird für alle 1000 Aufrufe eine Statistik geloggt.
     */
    GLog.note(GenomeLogCategory.PerformanceStat, "Performance for: " + ptype, //
        new LogAttribute(GenomeAttributeType.PerfType, ptype), //
        new LogAttribute(GenomeAttributeType.PerfAvgTime, Long.toString(logPe.getAverage())), //
        new LogAttribute(GenomeAttributeType.PerfMaxTime, Long.toString(logPe.maxTime)), //
        new LogAttribute(GenomeAttributeType.PerfMinTime, Long.toString(logPe.minTime)), //
        new LogAttribute(GenomeAttributeType.PerfSampleCount, Integer.toString(logPe.count)) //
    );

  }

  /**
   * schreibt alle gesammelten Performancedaten und setzt sie zurueck.
   */
  public static void resetPerformance()
  {
    synchronized (PerformanceCollector.class) {
      for (Map.Entry<String, PerformanceElement> e : data.entrySet()) {
        if (e.getValue().count == 0) {
          continue;
        }
        writePerfLog(e.getKey(), e.getValue());
        e.setValue(new PerformanceElement());
      }
    }
  }
}
