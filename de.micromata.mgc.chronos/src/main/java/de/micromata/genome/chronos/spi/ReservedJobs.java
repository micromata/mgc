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

import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;

/**
 * Jobs which start in soon future. Used by the Dispatcher implementation
 *
 * Problem: Job, which has a very high nodebinding timeout are kept in this cache for this long time.
 *
 * @author roger
 */
public class ReservedJobs
{

  /**
   * The jobs by started.
   */
  private List<TriggerJobDO> jobsByStarted = new ArrayList<TriggerJobDO>();

  /**
   * The jobs by pk.
   */
  private final Map<Long, TriggerJobDO> jobsByPk = new HashMap<Long, TriggerJobDO>();

  /**
   * Adds the reserved job.
   *
   * @param job the job
   */
  public synchronized void addReservedJob(final TriggerJobDO job)
  {
    Validate.notNull(job.getPk(), "job.getPk() is not set in addReservedJob");
    if (jobsByPk.containsKey(job.getPk()) == true) {
      return;
    }
    Validate.notNull(job.getScheduler(), "job.getSchedulerName() is not set in addReservedJob");
    jobsByPk.put(job.getPk(), job);
    jobsByStarted.add(job);
    Collections.sort(jobsByStarted, new JobsByStartedComparator());
  }

  /**
   * The type Jobs by started comparator.
   */
  public class JobsByStartedComparator implements Comparator<TriggerJobDO>
  {
    @Override
    public int compare(final TriggerJobDO o1, final TriggerJobDO o2)
    {
      final long t1 = o1.getNextFireTime().getTime();
      final long t2 = o2.getNextFireTime().getTime();
      if (t1 > t2) {
        return 1;
      }
      if (t1 < t2) {
        return -1;
      }
      return 0;
    }
  }

  /**
   * Gets jobs by next fire time iterator.
   *
   * @return the jobs by next fire time iterator
   */
  public Iterator<TriggerJobDO> getJobsByNextFireTimeIterator()
  {
    return jobsByStarted.iterator();
  }

  /**
   * Removes the job.
   *
   * @param it the it
   * @param job the job
   */
  public void removeJob(final Iterator<TriggerJobDO> it, final TriggerJobDO job)
  {
    jobsByPk.remove(job.getPk());
    it.remove();
  }

  /**
   * Sets reserved jobs.
   *
   * @param jobs the jobs
   */
  public synchronized void setReservedJobs(final List<TriggerJobDO> jobs)
  {
    jobsByPk.clear();
    jobsByStarted.clear();
    jobsByStarted.addAll(jobs);
    Collections.sort(jobsByStarted, new JobsByStartedComparator());
    for (final TriggerJobDO job : jobs) {
      Validate.notNull(job.getScheduler(), "job.getSchedulerName() is not set in addReservedJob");
      Validate.notNull(job.getPk(), "job.getPk() is not set in addReservedJob");
      jobsByPk.put(job.getPk(), job);
    }
  }

  /**
   * Gets jobs by started.
   *
   * @return the jobs by started
   */
  public List<TriggerJobDO> getJobsByStarted()
  {
    return jobsByStarted;
  }

}
