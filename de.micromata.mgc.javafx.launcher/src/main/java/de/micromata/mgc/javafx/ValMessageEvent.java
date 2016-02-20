/**
 *  Project: VLS
 *  Copyright(c) 2015 by Deutsche Post AG
 *  All rights reserved.
 */
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
