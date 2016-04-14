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

package de.micromata.mgc.javafx.launcher;

import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.application.MgcApplicationStartStopStatus;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * trigger launcher events.
 * 
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcLauncherEvent extends Event
{
  public static final EventType<MgcLauncherEvent> APP_STARTED = new EventType<>("mgcLaucherAppStarted");
  public static final EventType<MgcLauncherEvent> APP_STOPPED = new EventType<>("mgcLaucherAppStopped");

  private MgcApplicationStartStopStatus status;
  private ValMessage message;

  /**
   * Constructor.
   * 
   * @param message the message.
   */
  public MgcLauncherEvent(EventType<MgcLauncherEvent> eventType, MgcApplicationStartStopStatus status,
      ValMessage message)
  {
    super(eventType);
    this.status = status;
    this.message = message;
  }

  public MgcApplicationStartStopStatus getStatus()
  {
    return status;
  }

  public ValMessage getMessage()
  {
    return message;
  }

}
