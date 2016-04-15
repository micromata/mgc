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
