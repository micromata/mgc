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
