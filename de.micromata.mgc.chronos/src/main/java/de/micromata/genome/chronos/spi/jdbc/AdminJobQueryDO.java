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

/**
 * Wrapps a query for jobs.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AdminJobQueryDO
{

  /**
   * The host name.
   */
  private String hostName = "";

  /**
   * The state.
   */
  private String state = "";

  /**
   * The job name.
   */
  private String jobName = "";

  /**
   * The scheduler name.
   */
  private String schedulerName = "";

  /**
   * The result count.
   */
  private int resultCount = 10;

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName(String jobName)
  {
    this.jobName = jobName;
  }

  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  public String getSchedulerName()
  {
    return schedulerName;
  }

  public void setSchedulerName(String schedulerName)
  {
    this.schedulerName = schedulerName;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public int getResultCount()
  {
    return resultCount;
  }

  public void setResultCount(int resultCount)
  {
    this.resultCount = resultCount;
  }
}
