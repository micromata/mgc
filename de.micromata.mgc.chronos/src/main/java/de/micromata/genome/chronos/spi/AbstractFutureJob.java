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

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;

/**
 * Hält zusätzlich den {@link TriggerJobDO}.
 * 
 * DO NOT THIS DIRECTLY in Genome, but AbstractGenomeJob or better AbstractCommandLineJob
 * 
 * @author H.Spiewok@micromata.de
 * 
 */
public abstract class AbstractFutureJob implements FutureJob
{

  /**
   * The trigger job do.
   */
  private TriggerJobDO triggerJobDO;

  public long getWaitTime()
  {
    if (triggerJobDO != null && triggerJobDO.getFireTime() != null) {
      return System.currentTimeMillis() - triggerJobDO.getFireTime().getTime();
    }
    return 0;
  }

  /**
   * @see de.micromata.genome.chronos.FutureJob#call(java.lang.Object)
   */
  @Override
  public abstract Object call(Object argument) throws Exception;

  @Override
  public TriggerJobDO getTriggerJobDO()
  {
    return triggerJobDO;
  }

  @Override
  public void setTriggerJobDO(final TriggerJobDO triggerJobDO)
  {
    this.triggerJobDO = triggerJobDO;
  }

}
