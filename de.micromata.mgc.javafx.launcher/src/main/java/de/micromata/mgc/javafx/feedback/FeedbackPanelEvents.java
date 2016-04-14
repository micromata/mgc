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

package de.micromata.mgc.javafx.feedback;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event for signaling that feedback messages should be deleted.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 * 
 */
public class FeedbackPanelEvents extends Event
{

  /**
   * The serialVersionUID.
   */
  private static final long serialVersionUID = 2469273350952726996L;

  /**
   * The event type.
   */
  public static final EventType<FeedbackPanelEvents> CLEAR = new EventType<>("clearMessageEvent");

  /**
   * Update the owning windows height.
   */
  public static final EventType<FeedbackPanelEvents> UPDATE_WINDOW_HEIGHT = new EventType<>("updateWindow");

  /**
   * Constructor with {@link #CLEAR} eventtype as default.
   */
  public FeedbackPanelEvents()
  {
    super(CLEAR);
  }

  /**
   * Constructor.
   * 
   * @param type wanted event type.
   */
  public FeedbackPanelEvents(EventType<FeedbackPanelEvents> type)
  {
    super(type);
  }
}