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

package de.micromata.genome.chronos.manager;

import de.micromata.genome.chronos.JobLogEvent;
import de.micromata.genome.chronos.spi.JobEventImpl;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;

/**
 * Wrapps an Job into a Genome logging attribute.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogJobEventAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -908728178801341463L;

  /**
   * Instantiates a new log job event attribute.
   *
   * @param event the event
   */
  public LogJobEventAttribute(JobLogEvent event)
  {
    super(GenomeAttributeType.JobEvent, getEventString(event));
  }

  /**
   * Instantiates a new log job event attribute.
   *
   * @param job the job
   */
  public LogJobEventAttribute(TriggerJobDO job)
  {
    super(GenomeAttributeType.JobEvent, getEventString(buildEvent(job)));
  }

  /**
   * Builds the event.
   *
   * @param job the job
   * @return the job log event
   */
  private static JobLogEvent buildEvent(TriggerJobDO job)
  {
    JobLogEvent event = new JobEventImpl(job, job.getJobDefinition(), null, job.getState(), null);
    return event;
  }

  /**
   * Gets the event string.
   *
   * @param event the event
   * @return the event string
   */
  private static String getEventString(final JobLogEvent event)
  {
    if (event == null) {
      return "NullJobEvent";
    }
    final StringBuilder sb = new StringBuilder();
    if (event.getScheduler() != null) {
      sb.append(" Scheduler=").append(event.getScheduler()).append("\n");
    }
    // if (event.getRunner() != null) {
    // sb.append(" Runner=").append(event.getRunner()).append("\n");
    // }
    if (event.getJob() != null) {
      sb.append(" Job=").append(event.getJob()).append("\n");
    }
    if (event.getJobDefinition() != null) {
      sb.append(" Definition=").append(event.getJobDefinition()).append("\n");
    }
    if (event.getJobResult() != null) {
      sb.append(" Result=").append(event.getJobResult()).append("\n");
    }
    if (event.getJobStatus() != null) {
      sb.append(" Status=").append(event.getJobStatus()).append("\n");
    }
    return sb.toString();
  }
}
