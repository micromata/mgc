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

import de.micromata.genome.chronos.State;

/**
 * Represents a Job Result.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JobResultDO extends ChronosStdRecordDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7696253063378461680L;

  // private long id;

  /**
   * The job pk.
   */
  private long jobPk;

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
   * The result object.
   */
  private Object resultObject;

  /**
   * The result string.
   */
  private String resultString;

  // public long getId()
  // {
  // return id;
  // }

  // public void setId(long id)
  // {
  // this.id = id;
  // }

  public long getJobPk()
  {
    return jobPk;
  }

  public void setJobPk(long jobId)
  {
    this.jobPk = jobId;
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
    return state == null ? "UNDEFINED" : state.name();
  }

  public void setStateString(String state)
  {
    this.state = State.valueOf(state);
  }

  public Object getResultObject()
  {
    if (resultObject == null) {
      resultObject = SerializationUtil.deserialize(resultString, null);
    }
    return resultObject;
  }

  public void setResultObject(Object resultObject)
  {
    this.resultString = null;
    this.resultObject = resultObject;
  }

  public String getResultString()
  {
    if (resultString != null) {
      return resultString;
    }
    resultString = SerializationUtil.serialize(resultObject);
    return resultString;
  }

  public String getResultStringForDB()
  {
    return StringUtils.substring(getResultString(), 0, 1290);
  }

  public void setResultString(String resultString)
  {
    this.resultString = resultString;
    this.resultObject = null;
  }

  public long getDuration()
  {
    return duration;
  }

  public void setDuration(long duration)
  {
    this.duration = duration;
  }

  public Date getExecutionStart()
  {
    return executionStart;
  }

  public void setExecutionStart(Date executionStart)
  {
    this.executionStart = executionStart;
  }

  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  public String getVm()
  {
    return vm;
  }

  public void setVm(String vm)
  {
    this.vm = vm;
  }

  public int getRetryCount()
  {
    return retryCount;
  }

  public void setRetryCount(int retryCount)
  {
    this.retryCount = retryCount;
  }

  /**
   * To string.
   *
   * @return the string
   */
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
