package de.micromata.genome.logging.events;

import de.micromata.genome.util.event.MgcEventListener;
import de.micromata.genome.util.event.MgcFilterEvent;

/**
 * A service for listen on Logging services.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LoggingEventListenerRegistryService
{
  /**
   * Register a class, which should be called in Logging.
   * 
   * @param listenerClass
   */
  <EVENT extends LoggingEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      Class<LISTENER> listenerClass);

  void submitEvent(LoggingEvent event);

  <R, E extends MgcFilterEvent<R>> R filterEvent(E event, MgcEventListener<E> execute);

}
