/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   29.03.2008
// Copyright Micromata 29.03.2008
//
/////////////////////////////////////////////////////////////////////////////
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
