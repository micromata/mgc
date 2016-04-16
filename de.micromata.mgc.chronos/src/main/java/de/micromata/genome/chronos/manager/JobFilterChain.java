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

package de.micromata.genome.chronos.manager;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.chronos.spi.JobRunner;

/**
 * The Class JobFilterChain.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JobFilterChain
{

  /**
   * The job runner.
   */
  private JobRunner jobRunner;

  /**
   * The scheduler dao.
   */
  private SchedulerDAO schedulerDAO;

  /**
   * The filters.
   */
  private List<JobRunnerFilter> filters = new ArrayList<JobRunnerFilter>();

  /**
   * The index.
   */
  private int index;

  /**
   * Do filter.
   *
   * @return the object
   * @throws Exception the exception
   */
  public Object doFilter() throws Exception
  {
    if (index >= filters.size()) {
      return schedulerDAO.invokeJob(jobRunner);
    }
    ++index;
    return filters.get(index - 1).filter(this);
  }

  /**
   * Gets the job runner.
   *
   * @return the job runner
   */
  public JobRunner getJobRunner()
  {
    return jobRunner;
  }

  /**
   * Sets the job runner.
   *
   * @param jobRunner the new job runner
   */
  public void setJobRunner(JobRunner jobRunner)
  {
    this.jobRunner = jobRunner;
  }

  /**
   * Gets the scheduler dao.
   *
   * @return the scheduler dao
   */
  public SchedulerDAO getSchedulerDAO()
  {
    return schedulerDAO;
  }

  /**
   * Sets the scheduler dao.
   *
   * @param schedulerDAO the new scheduler dao
   */
  public void setSchedulerDAO(SchedulerDAO schedulerDAO)
  {
    this.schedulerDAO = schedulerDAO;
  }

  /**
   * Gets the filters.
   *
   * @return the filters
   */
  public List<JobRunnerFilter> getFilters()
  {
    return filters;
  }

  /**
   * Sets the filters.
   *
   * @param filters the new filters
   */
  public void setFilters(List<JobRunnerFilter> filters)
  {
    this.filters = filters;
  }

  /**
   * Gets the index.
   *
   * @return the index
   */
  public int getIndex()
  {
    return index;
  }

  /**
   * Sets the index.
   *
   * @param index the new index
   */
  public void setIndex(int index)
  {
    this.index = index;
  }
}
