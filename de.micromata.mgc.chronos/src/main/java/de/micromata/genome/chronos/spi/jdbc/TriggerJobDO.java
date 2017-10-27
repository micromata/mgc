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

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobDebugUtils;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.util.CronTrigger;
import de.micromata.genome.chronos.util.DelayTrigger;
import de.micromata.genome.chronos.util.TriggerJobUtils;

/**
 * Datenbank-Sicht auf einen Job mit dem entsprechenden Auslöser(Trigger).
 *
 * @author roger
 */
public class TriggerJobDO extends ChronosStdRecordDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 593615016974976998L;

  /**
   * The job store.
   */
  private JobStore jobStore;

  /**
   * The job definition string.
   */
  private String jobDefinitionString;

  /**
   * The job definition object.
   */
  private JobDefinition jobDefinitionObject;

  /**
   * The trigger defintion object.
   */
  private Trigger triggerDefintionObject;

  /**
   * The trigger defintion string.
   */
  private String triggerDefintionString;

  /**
   * The job name.
   */
  private String jobName;

  /**
   * The job argument string.
   */
  private String jobArgumentString;

  /**
   * The job argument object.
   */
  private Object jobArgumentObject;

  /**
   * The next fire time.
   */
  private Date nextFireTime;

  /**
   * The last scheduled time.
   */
  private Date lastScheduledTime;

  /**
   * The retry count.
   */
  private int retryCount = 0;

  /**
   * The scheduler.
   */
  private long scheduler;

  /**
   * The current result pk.
   */
  private Long currentResultPk = null;

  /**
   * The state.
   */
  private State state;

  /**
   * The host name.
   */
  private String hostName = StringUtils.EMPTY;

  /**
   * Instantiates a new trigger job do.
   */
  public TriggerJobDO()
  {

  }

  /**
   * Instantiates a new trigger job do.
   *
   * @param other the other
   */
  public TriggerJobDO(TriggerJobDO other)
  {
    this.jobStore = other.jobStore;
    this.jobDefinitionString = other.jobDefinitionString;
    this.jobName = other.jobName;
    this.jobDefinitionObject = other.jobDefinitionObject;
    this.triggerDefintionObject = other.triggerDefintionObject;
    this.triggerDefintionString = other.triggerDefintionString;
    this.pk = other.pk;
    this.jobArgumentString = other.jobArgumentString;
    this.jobArgumentObject = other.jobArgumentObject;
    this.nextFireTime = other.nextFireTime;
    this.lastScheduledTime = other.lastScheduledTime;
    this.retryCount = other.retryCount;
    this.scheduler = other.scheduler;
    this.currentResultPk = other.currentResultPk;
    this.state = other.state;
    this.hostName = other.hostName;
  }

  @Override
  public String toString()
  {
    final ToStringBuilder sb = new ToStringBuilder(this);
    sb.append("pk", pk);
    if (jobName != null) {
      sb.append("jobName", jobName);
    }
    sb.append("schedulerId", scheduler);
    sb.append("state", state);
    sb.append("trigger", getTriggerDefinition());
    sb.append("jobDefinition", getJobArgumentString());
    sb.append("nextFireTime", JobDebugUtils.dateToString(nextFireTime));
    return sb.toString();
  }

  /**
   * Increase retry count.
   */
  public void increaseRetryCount()
  {
    retryCount = retryCount + 1;
  }

  public void setJobArguments(final Object jobArguments)
  {
    jobArgumentObject = jobArguments;
    jobArgumentString = null;
  }

  public void setArgumentDefinitionString(final String argDef)
  {
    jobArgumentString = argDef;
    jobArgumentObject = null;
  }

  public String getArgumentDefinitionString()
  {
    if (jobArgumentString != null) {
      return jobArgumentString;
    }
    jobArgumentString = SerializationUtil.serialize(jobArgumentObject);
    return jobArgumentString;

  }

  public void setJobDefinition(final JobDefinition jobDefinition)
  {
    this.jobDefinitionObject = jobDefinition;
    this.jobDefinitionString = null;
  }

  public void setJobDefinitionString(final String jobDef)
  {
    jobDefinitionString = jobDef;
    jobDefinitionObject = null;
  }

  public String getJobDefinitionString()
  {
    if (jobDefinitionString != null) {
      return jobDefinitionString;
    }
    jobDefinitionString = SerializationUtil.serialize(jobDefinitionObject);
    return jobDefinitionString;
  }

  public void setJobStore(final JobStore jobStore)
  {
    this.jobStore = jobStore;
  }

  public void setTrigger(final Trigger trigger)
  {
    this.triggerDefintionObject = trigger;
    this.triggerDefintionString = null;
  }

  /**
   * An Hand des übergebenen String werden verschiedene Definitionen kreiert.
   * <p>
   * <ul>
   * <li>'+' {@link DelayTrigger}</li>
   * <li>'<' Deserialisieren des Triggers mittels {@link JdbcJobStore#deserialize(String, Class)}</li>
   * <li>Sonst wird ein {@link CronTrigger} angelegt.</li>
   * </ul>
   * </p>
   *
   * @param definition the definition
   * @return the trigger
   */
  public static Trigger parseTrigger(final String definition)
  {
    return TriggerJobUtils.createTriggerDefinition(definition);
  }

  public void setTriggerDefinition(final String definition)
  {
    triggerDefintionString = definition;
    triggerDefintionObject = null;
  }

  public Trigger getTrigger()
  {
    if (triggerDefintionObject != null) {
      return triggerDefintionObject;
    }
    if (triggerDefintionString == null) {
      return null;
    }
    triggerDefintionObject = parseTrigger(triggerDefintionString);
    return triggerDefintionObject;
  }

  public String getTriggerDefinition()
  {
    if (triggerDefintionString != null) {
      return triggerDefintionString;
    }
    if (triggerDefintionObject == null) {
      return null;
    }
    triggerDefintionString = triggerDefintionObject.getTriggerDefinition();
    return triggerDefintionString;
  }

  /**
   * Hier wird das eigentliche Runtime-Objekt erzeugt.
   * <p>
   * <code>This</code> wird als {@link TriggerJobDO} gesetzt.
   * </p>
   *
   * @see de.micromata.genome.jchronos.Job#getExecutor()
   */
  public FutureJob getExecutor()
  {
    final FutureJob job = getJobDefinition().getInstance();
    job.setTriggerJobDO(this);
    return job;
  }

  /**
   * Used to be shown in UI.
   *
   * @return
   */
  public String getJobArgumentsForDisplay()
  {
    String s = getJobArgumentString();

    try {
      String stmp = SerializationUtil.deserialize(s, String.class);
      if (stmp != null) {
        s = stmp;
      }
    } catch (Exception ex) {

    }
    return s;
  }

  public Object getOrginalJobArgument()
  {
    if (jobArgumentObject != null) {
      return jobArgumentObject;
    }
    jobArgumentObject = SerializationUtil.deserialize(jobArgumentString, null);
    return jobArgumentObject;
  }

  public Object getJobArguments()
  {
    getOrginalJobArgument();
    return jobArgumentObject;
  }

  public String getJobArgumentString()
  {
    if (jobArgumentString != null) {
      return jobArgumentString;
    }
    return SerializationUtil.serialize(jobArgumentObject);
  }

  public JobDefinition getJobDefinition()
  {
    if (jobDefinitionObject != null) {
      return jobDefinitionObject;
    }
    jobDefinitionObject = SerializationUtil.deserialize(jobDefinitionString, JobDefinition.class);
    return jobDefinitionObject;
  }

  /**
   * Gets the next fire time.
   *
   * @param now the now
   * @return the next fire time
   */
  public Date getNextFireTime(final Date now)
  {
    Trigger trigg = getTrigger();
    if (trigg == null) {
      return null;
    }
    nextFireTime = trigg.getNextFireTime(now);
    return nextFireTime == null ? null : new Date(nextFireTime.getTime());
  }

  public void setFireTime(final Date nextFireTime)
  {
    this.nextFireTime = nextFireTime == null ? null : new Date(nextFireTime.getTime());
  }

  public Date getFireTime()
  {
    return nextFireTime == null ? null : new Date(nextFireTime.getTime());
  }

  public String getFireTimeString()
  {
    return JobDebugUtils.dateToString(nextFireTime);
  }

  public Date getLastScheduledTime()
  {
    return lastScheduledTime == null ? null : new Date(lastScheduledTime.getTime());
  }

  public void setLastScheduledTime(final Date lastScheduledTime)
  {
    this.lastScheduledTime = lastScheduledTime == null ? null : new Date(lastScheduledTime.getTime());
  }

  public String getLastScheduleTimeString()
  {
    return JobDebugUtils.dateToString(lastScheduledTime);
  }

  public long getScheduler()
  {
    return scheduler;
  }

  public void setScheduler(final long scheduler)
  {
    this.scheduler = scheduler;
  }

  public State getState()
  {
    return state;
  }

  public void setState(State state)
  {
    this.state = state;
  }

  public String getStateString()
  {
    return state.name();
  }

  public void setStateString(String state)
  {
    this.state = State.valueOf(state);
  }

  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  /**
   * Checks for failure result.
   *
   * @return true, if successful
   */
  public boolean hasFailureResult()
  {
    return currentResultPk != null;
  }

  public Long getCurrentResultPk()
  {
    return currentResultPk;
  }

  public void setCurrentResultPk(Long resultPk)
  {

    this.currentResultPk = resultPk;

  }

  public JobStore getJobStore()
  {
    return jobStore;
  }

  public int getRetryCount()
  {
    return retryCount;
  }

  public void setRetryCount(int retryCount)
  {
    this.retryCount = retryCount;
  }

  public JobDefinition getJobDefinitionObject()
  {
    return jobDefinitionObject;
  }

  public void setJobDefinitionObject(JobDefinition jobDefinitionObject)
  {
    this.jobDefinitionObject = jobDefinitionObject;
  }

  public Trigger getTriggerDefintionObject()
  {
    return triggerDefintionObject;
  }

  public void setTriggerDefintionObject(Trigger triggerDefintionObject)
  {
    this.triggerDefintionObject = triggerDefintionObject;
  }

  public String getTriggerDefintionString()
  {
    return triggerDefintionString;
  }

  public void setTriggerDefintionString(String triggerDefintionString)
  {
    this.triggerDefintionString = triggerDefintionString;
  }

  @Deprecated
  public Object getJobArgumentObject()
  {
    return jobArgumentObject;
  }

  public void setJobArgumentObject(Object jobArgumentObject)
  {
    this.jobArgumentObject = jobArgumentObject;
    this.jobArgumentString = null;
  }

  public Date getNextFireTime()
  {
    return nextFireTime;
  }

  public void setNextFireTime(Date nextFireTime)
  {
    this.nextFireTime = nextFireTime;
  }

  public void setJobArgumentString(String jobArgumentString)
  {
    this.jobArgumentString = jobArgumentString;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName(String jobName)
  {
    this.jobName = jobName;
  }
}
