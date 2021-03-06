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
 * One Entry with Performance information.
 * 
 * @author roger@micromata.de
 * 
 */
public class PerfElement implements Serializable, Cloneable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -3633001509141331386L;

  /**
   * The count.
   */
  protected long count = 0;

  /**
   * The min time.
   */
  protected long minTime = -1;

  /**
   * The max time.
   */
  protected long maxTime = 0;

  /**
   * The sum time.
   */
  protected long sumTime = 0;

  /**
   * The sum wait.
   */
  protected long sumWait = 0;

  /**
   * The neun perc.
   */
  // TODO (Rx) rrk (minor) wie dies berechnen
  private long neunPerc = 0;

  @Override
  public Object clone()
  {
    PerfElement p = new PerfElement();
    copyTo(p);
    return p;
  }

  /**
   * Copy to.
   *
   * @param p the p
   */
  protected void copyTo(PerfElement p)
  {
    p.count = count;
    p.minTime = minTime;
    p.maxTime = maxTime;
    p.sumTime = sumTime;
    p.sumWait = sumWait;
  }

  /**
   * Adds the time.
   *
   * @param time the time
   */
  public void addTime(long time)
  {
    ++count;
    sumTime += time;
    if (maxTime < time) {
      maxTime = time;
    }
    if (minTime == -1 || minTime > time) {
      minTime = time;
    }
  }

  /**
   * Adds the wait.
   *
   * @param time the time
   */
  public void addWait(long time)
  {
    sumWait += time;
  }

  public long getAverage()
  {
    if (count == 0) {
      return 0;
    }
    return sumTime / count;
  }

  public long getCount()
  {
    return count;
  }

  public void setCount(long count)
  {
    this.count = count;
  }

  public long getMaxTime()
  {
    return maxTime;
  }

  public void setMaxTime(long maxTime)
  {
    this.maxTime = maxTime;
  }

  public long getMinTime()
  {
    return minTime;
  }

  public void setMinTime(long minTime)
  {
    this.minTime = minTime;
  }

  public long getNeunPerc()
  {
    return neunPerc;
  }

  public void setNeunPerc(long neunPerc)
  {
    this.neunPerc = neunPerc;
  }

  public long getSumTime()
  {
    return sumTime;
  }

  public void setSumTime(long sumTime)
  {
    this.sumTime = sumTime;
  }

  public long getSumWait()
  {
    return sumWait;
  }

  public void setSumWait(long sumWait)
  {
    this.sumWait = sumWait;
  }
}
