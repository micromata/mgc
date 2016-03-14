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
