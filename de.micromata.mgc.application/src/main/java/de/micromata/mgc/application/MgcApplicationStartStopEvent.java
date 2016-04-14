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

package de.micromata.mgc.application;

import de.micromata.genome.util.event.MgcEvent;
import de.micromata.genome.util.validation.ValMessage;

/**
 * Event will be dispatched over de.micromata.genome.util.event.MgcEventRegistries.getEventInstanceRegistry().
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcApplicationStartStopEvent implements MgcEvent
{

  /**
   * The status.
   */
  private MgcApplicationStartStopStatus status;

  /**
   * The application.
   */
  private MgcApplication<?> application;

  /**
   * The val message.
   */
  private ValMessage valMessage;

  /**
   * Instantiates a new mgc application start stop event.
   *
   * @param status the status
   */
  public MgcApplicationStartStopEvent(MgcApplication<?> application, MgcApplicationStartStopStatus status,
      ValMessage valMessage)
  {
    this.status = status;
    this.application = application;
    this.valMessage = valMessage;
  }

  public MgcApplicationStartStopStatus getStatus()
  {
    return status;
  }

  public MgcApplication<?> getApplication()
  {
    return application;
  }

  public ValMessage getValMessage()
  {
    return valMessage;
  }

}
