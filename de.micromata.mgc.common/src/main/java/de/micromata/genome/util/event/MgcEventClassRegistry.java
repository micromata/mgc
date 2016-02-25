package de.micromata.genome.util.event;

/**
 * Registry for event classes.
 * 
 * This are sensible for dynamic modules, like Genome Modules.
 * 
 * If an Event or EventListener classloader disappear (GCed), the event will not be longer dispatched.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface MgcEventClassRegistry extends MgcEventRegistry
{
  /**
   * Register an event listener.
   * 
   * @param listenerClass
   */
  <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      Class<LISTENER> listenerClass);

  /**
   * Removes a listener class.
   * 
   * @param listenerClass
   */
  public <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void removeListener(
      Class<LISTENER> listenerClass);

}
