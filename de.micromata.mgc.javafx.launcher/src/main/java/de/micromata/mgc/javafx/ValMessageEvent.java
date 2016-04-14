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

package de.micromata.mgc.javafx;

import de.micromata.genome.util.validation.ValMessage;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event that has a message as payload.
 * 
 * Controls can listen to this event to show the message to the user.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class ValMessageEvent extends Event
{
  /**
   * The serialVersionUID.
   */
  private static final long serialVersionUID = -2232994580334677191L;

  /**
   * The event type.
   */
  public static final EventType<ValMessageEvent> MESSAGE_EVENT_TYPE = new EventType<>("messageEvent");

  private ValMessage message;

  /**
   * Constructor.
   * 
   * @param message the message.
   */
  public ValMessageEvent(ValMessage message)
  {
    super(MESSAGE_EVENT_TYPE);
    this.message = message;
  }

  /**
   * Getter.
   * 
   * @return the message.
   */
  public ValMessage getMessage()
  {
    return message;
  }

}
