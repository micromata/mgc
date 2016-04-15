/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   09.01.2008
// Copyright Micromata 09.01.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.manager;

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.spi.jdbc.Stringifiable;

/**
 * Definition of a job.
 * 
 * Used in context.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public class JobBeanDefinition implements JobDefinition, Stringifiable
{

  /**
   * The bean name.
   */
  private String beanName;

  /**
   * The job name.
   */
  private String jobName;

  /**
   * If true, this job should exists once per node.
   * 
   * Will be used to start autoJobs.
   */
  private boolean onePerNode = false;

  /**
   * The job definition.
   */
  private JobDefinition jobDefinition;

  /**
   * The scheduler name.
   */
  private String schedulerName;

  /**
   * The trigger definition.
   */
  private String triggerDefinition;

  /**
   * Used to defined job arguments.
   */
  private String jobArgument;

  @Override
  public FutureJob getInstance()
  {
    return jobDefinition.getInstance();
  }

  @Override
  public String asString()
  {
    return JobBeanDefinition.class.getName() + ":" + beanName + "; " + jobName;
  }

  public JobDefinition getJobDefinition()
  {
    return jobDefinition;
  }

  public void setJobDefinition(JobDefinition jobDefinition)
  {
    this.jobDefinition = jobDefinition;
  }

  public String getBeanName()
  {
    return beanName;
  }

  public void setBeanName(String beanName)
  {
    this.beanName = beanName;
  }

  public String getSchedulerName()
  {
    return schedulerName;
  }

  public void setSchedulerName(String schedulerName)
  {
    this.schedulerName = schedulerName;
  }

  public String getTriggerDefinition()
  {
    return triggerDefinition;
  }

  public void setTriggerDefinition(String triggerDefinition)
  {
    this.triggerDefinition = triggerDefinition;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName(String jobName)
  {
    this.jobName = jobName;
  }

  public boolean isOnePerNode()
  {
    return onePerNode;
  }

  public void setOnePerNode(boolean onePerNode)
  {
    this.onePerNode = onePerNode;
  }

  public String getJobArgument()
  {
    return jobArgument;
  }

  public void setJobArgument(String jobArgument)
  {
    this.jobArgument = jobArgument;
  }

}
