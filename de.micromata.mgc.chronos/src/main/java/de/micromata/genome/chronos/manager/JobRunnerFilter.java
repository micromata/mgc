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

/**
 * A global Filter which wrapps Chronos Job running.
 * 
 * @author roger@micromata.de
 * 
 */
public interface JobRunnerFilter
{

  /**
   * Filter.
   *
   * @param jobFilterCain the job filter cain
   * @return the object
   * @throws Exception the exception
   */
  public Object filter(JobFilterChain jobFilterCain) throws Exception;

  /**
   * Return a priority for executing filters Lower value is lower priority. The Filter will applied later.
   *
   * @return the priority
   */
  public int getPriority();
}
