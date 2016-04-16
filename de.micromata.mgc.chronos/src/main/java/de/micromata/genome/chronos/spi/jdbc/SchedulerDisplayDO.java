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

package de.micromata.genome.chronos.spi.jdbc;

/**
 * The Class SchedulerDisplayDO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SchedulerDisplayDO extends SchedulerDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5924549593570479121L;

  /**
   * The pool size.
   */
  private int poolSize;

  /**
   * The pool active.
   */
  private int poolActive;

  /**
   * The pool completed.
   */
  private long poolCompleted;

  /**
   * The pool waiting.
   */
  private int poolWaiting;

  /**
   * The pool task count.
   */
  private long poolTaskCount;

  /**
   * Instantiates a new scheduler display do.
   */
  public SchedulerDisplayDO()
  {//TODO rrk dieser Konstruktro hat gefehlt. Ist das korrekt ?
    super();
  }

  /**
   * Instantiates a new scheduler display do.
   *
   * @param sched the sched
   */
  public SchedulerDisplayDO(SchedulerDO sched)
  {
    super(sched);
  }

  public int getPoolActive()
  {
    return poolActive;
  }

  public void setPoolActive(int poolActive)
  {
    this.poolActive = poolActive;
  }

  public long getPoolCompleted()
  {
    return poolCompleted;
  }

  public void setPoolCompleted(long poolCompleted)
  {
    this.poolCompleted = poolCompleted;
  }

  public int getPoolSize()
  {
    return poolSize;
  }

  public void setPoolSize(int poolSize)
  {
    this.poolSize = poolSize;
  }

  public long getPoolTaskCount()
  {
    return poolTaskCount;
  }

  public void setPoolTaskCount(long poolTaskCount)
  {
    this.poolTaskCount = poolTaskCount;
  }

  public int getPoolWaiting()
  {
    return poolWaiting;
  }

  public void setPoolWaiting(int poolWaiting)
  {
    this.poolWaiting = poolWaiting;
  }
}
