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

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.micromata.genome.logging.LogCategory;

/**
 * POJO holding performance stats.
 * 
 * @author roger@micromata.de
 * 
 */
public class PerfStatsDO extends TypeStatsDO implements Cloneable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 3939678356853026113L;

  /**
   * The perf elements.
   */
  private Map<String, PerfGroupElement> perfElements = new TreeMap<String, PerfGroupElement>();

  /**
   * The all perfs.
   */
  private PerfElement allPerfs = new PerfElement();

  @Override
  public Object clone()
  {
    PerfStatsDO p = new PerfStatsDO();
    for (Map.Entry<String, PerfGroupElement> me : perfElements.entrySet()) {
      p.perfElements.put(me.getKey(), (PerfGroupElement) me.getValue().clone());
    }
    p.allPerfs = (PerfElement) allPerfs.clone();
    return p;
  }

  public Set<Map.Entry<String, PerfGroupElement>> getStatsEntries()
  {
    return perfElements.entrySet();
  }

  /**
   * Adds the perf element.
   *
   * @param cat the cat
   * @param point the point
   * @param millis the millis
   * @param wait the wait
   */
  public void addPerfElement(LogCategory cat, String point, long millis, long wait)
  {
    setLastDate(new Date());
    String cs = cat.name();
    PerfGroupElement el = perfElements.get(cs);
    if (el == null) {
      el = new PerfGroupElement();
      perfElements.put(cs, el);
    }
    el.addTime(millis, point, wait);
    allPerfs.addTime(millis);
    allPerfs.addWait(wait);
  }

  public PerfElement getAllPerfs()
  {
    return allPerfs;
  }

  public void setAllPerfs(PerfElement allPerfs)
  {
    this.allPerfs = allPerfs;
  }

}
