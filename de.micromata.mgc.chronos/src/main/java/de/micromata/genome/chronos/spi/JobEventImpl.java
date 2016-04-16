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

package de.micromata.genome.chronos.spi;

import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobLogEvent;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

public class JobEventImpl implements JobLogEvent
{
  private TriggerJobDO job;

  private JobDefinition jobDefinition;

  private JobResultDO jobResult;

  private State jobStatus;

  private Scheduler scheduler;

  /**
   * @param job
   * @param jobDefinition
   * @param jobResult
   * @param jobStatus
   * @param runner
   * @param scheduler
   */
  public JobEventImpl(final TriggerJobDO job, final JobDefinition jobDefinition, final JobResultDO jobResult, final State jobStatus,
      final Scheduler scheduler)
  {
    this.job = job;
    this.jobDefinition = jobDefinition;
    this.jobResult = jobResult;
    this.jobStatus = jobStatus;

    this.scheduler = scheduler;
  }

  public TriggerJobDO getJob()
  {
    return job;
  }

  public JobDefinition getJobDefinition()
  {
    return jobDefinition;
  }

  public JobResultDO getJobResult()
  {
    return jobResult;
  }

  public State getJobStatus()
  {
    return jobStatus;
  }

  public Scheduler getScheduler()
  {
    return scheduler;
  }

}
