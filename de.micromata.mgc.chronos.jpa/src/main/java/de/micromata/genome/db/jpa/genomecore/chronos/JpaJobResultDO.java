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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class JpaJobResultDO.
 */
@Entity
@Table(name = "TB_TA_CHRONOS_RESULT", indexes = {
    @Index(name = "IX_TA_CHRONOS__JOB", columnList = "TA_CHRONOS_JOB"),
    @Index(name = "IX_TA_CHRONOS_RES_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_TA_CHRONOS_RESULT_CRTAT", columnList = "CREATEDAT")
})
@SequenceGenerator(name = "SQ_TA_CHRONOS_RESULT", sequenceName = "SQ_TA_CHRONOS_RESULT")
public class JpaJobResultDO extends StdRecordDO<Long>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7696253063378461680L;

  // private long id;

  /**
   * The job pk.
   */
  private Long jobPk;

  /**
   * The state.
   */
  private State state;

  /**
   * The duration.
   */
  private long duration;

  /**
   * The execution start.
   */
  private Date executionStart;

  /**
   * The retry count.
   */
  private int retryCount;

  // private boolean active = true;

  /**
   * The host name.
   */
  private String hostName;

  /**
   * The vm.
   */
  private String vm;

  /**
   * The result string.
   */
  private String resultString;

  @Column(name = "TA_CHRONOS_RESULT")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_CHRONOS_RESULT")
  @Override
  public Long getPk()
  {
    return pk;
  }

  @Column(name = "TA_CHRONOS_JOB", nullable = false)
  public Long getJobPk()
  {
    return jobPk;
  }

  public void setJobPk(Long jobId)
  {
    this.jobPk = jobId;
  }

  @Column(name = "STATE")
  @Enumerated(EnumType.STRING)
  public State getState()
  {
    return state;
  }

  public void setState(State state)
  {
    this.state = state;
  }

  @Column(name = "RESULT_DATA", length = 1300)
  public String getResultString()
  {
    return resultString;
  }

  public void setResultString(String resultString)
  {
    this.resultString = resultString;
  }

  @Column(name = "JOB_DURATION")
  public long getDuration()
  {
    return duration;
  }

  public void setDuration(long duration)
  {
    this.duration = duration;
  }

  @Column(name = "JOB_START_TIME")
  public Date getExecutionStart()
  {
    return executionStart;
  }

  public void setExecutionStart(Date executionStart)
  {
    this.executionStart = executionStart;
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

  @Column(name = "VM_ID", length = 64)
  public String getVm()
  {
    return vm;
  }

  public void setVm(String vm)
  {
    this.vm = vm;
  }

  /**
   * TODO RK hmm, nicht im Schema gefunden.
   * 
   * @return
   */
  @Transient
  public int getRetryCount()
  {
    return retryCount;
  }

  public void setRetryCount(int retryCount)
  {
    this.retryCount = retryCount;
  }

  @Override
  public String toString()
  {
    final ToStringBuilder sb = new ToStringBuilder(this);

    sb.append("id", getPk()).append("jobId", jobPk).append("state", state).append("duration", duration)
        .append("retryCount", retryCount)
        .append("hostName", hostName).append("resultString", getResultString())
        .append("createdAt", getCreatedAtString()).append(
            "modifiedAt", getModifiedAtString());
    return sb.toString();
  }
}
