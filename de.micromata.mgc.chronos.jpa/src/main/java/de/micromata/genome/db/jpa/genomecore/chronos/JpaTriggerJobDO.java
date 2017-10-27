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

package de.micromata.genome.db.jpa.genomecore.chronos;

import de.micromata.genome.chronos.State;
import de.micromata.genome.jpa.StdRecordDO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/**
 * The Class JpaTriggerJobDO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
@Entity
@Table(name = "TB_TA_CHRONOS_JOB")
@org.hibernate.annotations.Table(indexes = { //
    @Index(name = "IX_TA_JOB_NAME", columnNames = "JOB_NAME"),
    @Index(name = "IX_TA_CHRONOS_JOB_STATE", columnNames = { "STATE" }), //
    @Index(name = "IX_TA_JOB_NEXT_FIRE_TIME", columnNames = { "NEXT_FIRE_TIME" }), //
    @Index(name = "IX_TA_CHRONOS_JOB_SCHEDULER", columnNames = { "TA_CHRONOS_SCHEDULER" }),
    @Index(name = "IX_TA_CHRONOS_JOB_MODAT", columnNames = { "MODIFIEDAT" }),
}, appliesTo = "TB_TA_CHRONOS_JOB")
@SequenceGenerator(name = "SQ_TA_CHRONOS_JOB", sequenceName = "SQ_TA_CHRONOS_JOB")
public class JpaTriggerJobDO extends StdRecordDO<Long>
{

  /**
   *
  */

  private static final long serialVersionUID = 8731646722819635634L;

  /**
   * The job definition string.
   */
  private String jobDefinitionString;

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

  @Column(name = "TA_CHRONOS_JOB")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_CHRONOS_JOB")
  @Override
  public Long getPk()
  {
    return pk;
  }

  @Column(name = "CURRENT_RESULT")
  public Long getCurrentResultPk()
  {
    return currentResultPk;
  }

  public void setCurrentResultPk(Long currentResultPk)
  {
    this.currentResultPk = currentResultPk;
  }

  @Column(name = "HOST_NAME", length = 64)
  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  @Column(name = "JOB_DEFINITION", length = 1300)
  public String getJobDefinitionString()
  {
    return jobDefinitionString;
  }

  public void setJobDefinitionString(String jobDefinitionString)
  {
    this.jobDefinitionString = jobDefinitionString;
  }

  @Column(name = "JOB_ARGUMENT", length = 1300)
  public String getJobArgumentString()
  {
    return jobArgumentString;
  }

  public void setJobArgumentString(String jobArgumentString)
  {
    this.jobArgumentString = jobArgumentString;
  }

  @Column(name = "JOB_NAME", length = 128)
  public String getJobName()
  {
    return jobName;
  }

  public void setJobName(String jobName)
  {
    this.jobName = jobName;
  }

  @Column(name = "JOB_RETRY_COUNT")
  public int getRetryCount()
  {
    return retryCount;
  }

  public void setRetryCount(int retryCount)
  {
    this.retryCount = retryCount;
  }

  @Column(name = "TRIGGER_DEFINITION", length = 1300)
  public String getTriggerDefintionString()
  {
    return triggerDefintionString;
  }

  public void setTriggerDefintionString(String triggerDefintionString)
  {
    this.triggerDefintionString = triggerDefintionString;
  }

  @Column(name = "NEXT_FIRE_TIME")
  public Date getNextFireTime()
  {
    return nextFireTime;
  }

  public void setNextFireTime(Date nextFireTime)
  {
    this.nextFireTime = nextFireTime;
  }

  @Column(name = "LAST_RUN_TIME")
  public Date getLastScheduledTime()
  {
    return lastScheduledTime;
  }

  public void setLastScheduledTime(Date lastScheduledTime)
  {
    this.lastScheduledTime = lastScheduledTime;
  }

  @Column(name = "TA_CHRONOS_SCHEDULER")
  public long getScheduler()
  {
    return scheduler;
  }

  public void setScheduler(long scheduler)
  {
    this.scheduler = scheduler;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "STATE")
  public State getState()
  {
    return state;
  }

  public void setState(State state)
  {
    this.state = state;
  }

}
