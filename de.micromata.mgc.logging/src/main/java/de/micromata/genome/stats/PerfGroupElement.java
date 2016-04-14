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
