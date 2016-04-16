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

package de.micromata.genome.chronos;

import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * For logging a Job.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JobLogEvent
{

  /**
   * Gets the scheduler.
   *
   * @return the scheduler
   */
  Scheduler getScheduler();

  /**
   * Gets the job.
   *
   * @return the job
   */
  TriggerJobDO getJob();

  /**
   * Gets the job definition.
   *
   * @return the job definition
   */
  JobDefinition getJobDefinition();

  /**
   * Gets the job result.
   *
   * @return the job result
   */
  JobResultDO getJobResult();

  /**
   * Gets the job status.
   *
   * @return the job status
   */
  State getJobStatus();
}
