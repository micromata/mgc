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

package de.micromata.genome.chronos.spi;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Specialized Threadpool executor.
 *
 * @author roger
 */
public class SchedulerThreadPoolExecutor extends ThreadPoolExecutor
{

  /**
   * The scheduler impl.
   */
  private SchedulerImpl schedulerImpl;

  /**
   * Instantiates a new scheduler thread pool executor.
   *
   * @param corePoolSize the core pool size
   * @param maximumPoolSize the maximum pool size
   * @param keepAliveTime the keep alive time
   * @param unit the unit
   * @param workQueue the work queue
   * @param handler the handler
   */
  public SchedulerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
      BlockingQueue<Runnable> workQueue, SchedulerImpl handler)
  {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    schedulerImpl = handler;
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r)
  {
    super.beforeExecute(t, r);

  }

  @Override
  protected void afterExecute(Runnable r, Throwable t)
  {
    super.afterExecute(r, t);
    schedulerImpl.getDispatcher().wakeup();
  }

}
