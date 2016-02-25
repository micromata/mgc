package de.micromata.genome.logging.events;

import de.micromata.genome.util.event.MgcEventListener;
import de.micromata.genome.util.event.MgcFilterEvent;
import de.micromata.genome.util.event.SimpleEventClassRegistry;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingEventListenerRegistryServiceImpl implements LoggingEventListenerRegistryService
{
  SimpleEventClassRegistry logEventRegistry = new SimpleEventClassRegistry("loggingEvents");

  @Override
  public <EVENT extends LoggingEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      Class<LISTENER> listenerClass)
  {
    logEventRegistry.registerListener(listenerClass);
  }

  @Override
  public void submitEvent(LoggingEvent event)
  {
    logEventRegistry.submitEvent(event);
  }

  @Override
  public <R, E extends MgcFilterEvent<R>> R filterEvent(E event, MgcEventListener<E> execute)
  {
    return logEventRegistry.filterEvent(event, execute);
  }
}
