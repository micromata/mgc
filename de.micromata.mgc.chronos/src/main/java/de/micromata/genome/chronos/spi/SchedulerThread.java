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

/**
 * A Thread runned into the Scheduler/Dispatcher.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SchedulerThread extends Thread
{

  /**
   * The job id.
   */
  private long jobId;

  /**
   * Instantiates a new scheduler thread.
   */
  public SchedulerThread()
  {

  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param target the target
   * @param name the name
   */
  public SchedulerThread(Runnable target, String name)
  {
    super(target, name);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param target the target
   */
  public SchedulerThread(Runnable target)
  {
    super(target);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param name the name
   */
  public SchedulerThread(String name)
  {
    super(name);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param target the target
   * @param name the name
   * @param stackSize the stack size
   */
  public SchedulerThread(ThreadGroup group, Runnable target, String name, long stackSize)
  {
    super(group, target, name, stackSize);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param target the target
   * @param name the name
   */
  public SchedulerThread(ThreadGroup group, Runnable target, String name)
  {
    super(group, target, name);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param target the target
   */
  public SchedulerThread(ThreadGroup group, Runnable target)
  {
    super(group, target);
  }

  /**
   * Instantiates a new scheduler thread.
   *
   * @param group the group
   * @param name the name
   */
  public SchedulerThread(ThreadGroup group, String name)
  {
    super(group, name);
  }

  @Override
  public String toString()
  {
    return super.toString() + "; jobId: " + jobId;
  }

  public long getJobId()
  {
    return jobId;
  }

  public void setJobId(long jobId)
  {
    this.jobId = jobId;
  }

}
