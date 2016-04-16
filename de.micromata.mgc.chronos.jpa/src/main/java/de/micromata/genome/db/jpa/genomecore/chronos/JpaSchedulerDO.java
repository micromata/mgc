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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import de.micromata.genome.jpa.StdRecordDO;

/**
 * The Class JpaSchedulerDO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
@Entity
@Table(name = "TB_TA_CHRONOS_SCHEDULER",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" },
        name = "IX_TA_CHRONOS_SCHEDULER_NAME") })
@org.hibernate.annotations.Table(indexes = { //
    @Index(name = "IX_TA_CHRONOS_SCHEDULER_MODAT", columnNames = { "MODIFIEDAT" }),
}, appliesTo = "TB_TA_CHRONOS_SCHEDULER")
@SequenceGenerator(name = "SQ_TA_CHRONOS_SCHEDULER", sequenceName = "SQ_TA_CHRONOS_SCHEDULER")
public class JpaSchedulerDO extends StdRecordDO<Long>
{

  /**
   * The name.
   */
  private String name;

  /**
   * The thread pool size.
   */
  private int threadPoolSize = 1;

  /**
   * The service retry time.
   */
  private int serviceRetryTime = 60000;

  /**
   * The job retry time.
   */
  private int jobRetryTime = 30000;

  /**
   * The job max retry count.
   */
  private int jobMaxRetryCount = 2;

  /**
   * The node binding timeout.
   */
  private int nodeBindingTimeout = 0;

  @Column(name = "TA_CHRONOS_SCHEDULER")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_CHRONOS_SCHEDULER")
  @Override
  public Long getPk()
  {

    return pk;
  }

  @Column(name = "NAME", length = 100)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Column(name = "POOL_SIZE")
  public int getThreadPoolSize()
  {
    return threadPoolSize;
  }

  public void setThreadPoolSize(int threadPoolSize)
  {
    this.threadPoolSize = threadPoolSize;
  }

  @Column(name = "SERVICE_RETRY_INTERVAL")
  public int getServiceRetryTime()
  {
    return serviceRetryTime;
  }

  public void setServiceRetryTime(int serviceRetryTime)
  {
    this.serviceRetryTime = serviceRetryTime;
  }

  @Column(name = "JOB_RETRY_INTERVAL")
  public int getJobRetryTime()
  {
    return jobRetryTime;
  }

  public void setJobRetryTime(int jobRetryTime)
  {
    this.jobRetryTime = jobRetryTime;
  }

  @Column(name = "JOB_MAX_RETRY_COUNT")
  public int getJobMaxRetryCount()
  {
    return jobMaxRetryCount;
  }

  public void setJobMaxRetryCount(int jobMaxRetryCount)
  {
    this.jobMaxRetryCount = jobMaxRetryCount;
  }

  @Column(name = "NODE_BIND_INTERVAL")
  public int getNodeBindingTimeout()
  {
    return nodeBindingTimeout;
  }

  public void setNodeBindingTimeout(int nodeBindingTimeout)
  {
    this.nodeBindingTimeout = nodeBindingTimeout;
  }

}
