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

import java.util.Date;

/**
 * The Interface Trigger.
 *
 * @author roger, wolle
 */
public interface Trigger
{

  /**
   * calcuate the next trigger definition.
   *
   * @param now the now
   * @return may return null, if no future trigger results
   */
  public Date getNextFireTime(Date now);

  /**
   * 
   * @return the internal next firetime, may be null
   */
  public Date getInternalNextFireTime();

  /**
   * sets the internal next fire time
   * 
   * @param nextFireTime
   */
  public void setNextFireTime(Date nextFireTime);

  /**
   * Vorbereitung auf den nächsten Auslösezeitpunkt.
   *
   * @param scheduler the scheduler
   * @param cause the cause
   * @return nextFireTime null wenn nicht mehr ausgefuehrt werden soll
   */

  public Date updateAfterRun(Scheduler scheduler, JobCompletion cause);

  /**
   * 
   * @return the trigger definition
   */
  public String getTriggerDefinition();

}
