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
