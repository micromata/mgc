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

import java.io.Serializable;

/**
 * POJO for holding Stats.
 * 
 * @author roger@micromata.de
 * 
 */
public class StatsDO implements Serializable, Cloneable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5464154130335924966L;

  /**
   * The log stats.
   */
  private LogStatsDO logStats = new LogStatsDO();

  /**
   * The perf stats.
   */
  private PerfStatsDO perfStats = new PerfStatsDO();

  public LogStatsDO getLogStats()
  {
    return logStats;
  }

  public void setLogStats(LogStatsDO logStats)
  {
    this.logStats = logStats;
  }

  public PerfStatsDO getPerfStats()
  {
    return perfStats;
  }

  public void setPerfStats(PerfStatsDO perfStats)
  {
    this.perfStats = perfStats;
  }

  @Override
  public Object clone()
  {
    StatsDO s = new StatsDO();
    s.logStats = (LogStatsDO) logStats.clone();
    s.perfStats = (PerfStatsDO) perfStats.clone();
    return s;
  }
}
