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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Collection of performance elements.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PerfGroupElement extends PerfElement
{
  private static final long serialVersionUID = -5872967587677275761L;

  private Map<String, PerfElement> details = new TreeMap<String, PerfElement>();

  protected void copyTo(PerfGroupElement pg)
  {
    super.copyTo(pg);
    for (Map.Entry<String, PerfElement> me : details.entrySet()) {
      pg.details.put(me.getKey(), (PerfElement) me.getValue().clone());
    }
  }

  @Override
  public Object clone()
  {
    PerfGroupElement pg = new PerfGroupElement();
    copyTo(pg);
    return pg;
  }

  public void addTime(long time, String point, long wait)
  {
    super.addTime(time);
    super.addWait(wait);
    PerfElement d = details.get(point);
    if (d == null) {
      d = new PerfElement();
      details.put(point, d);
    }
    d.addTime(time);
    d.addWait(wait);
  }

  public Set<Map.Entry<String, PerfElement>> getDetailsEntries()
  {
    return details.entrySet();
  }

  public Map<String, PerfElement> getDetails()
  {
    return details;
  }

  public void setDetails(Map<String, PerfElement> details)
  {
    this.details = details;
  }
}
