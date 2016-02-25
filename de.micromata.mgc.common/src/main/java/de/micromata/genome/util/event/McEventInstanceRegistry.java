package de.micromata.genome.util.event;

/**
 * Holds MgcEventListener instances.
 * 
 * Note: Don't use, if the listener may come from dynamic modules and the registry will be held by non-dynamic module.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface McEventInstanceRegistry extends MgcEventRegistry
{
  /**
   * Register an event listener.
   * 
   * @param listenerClass
   */
  <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      LISTENER listenerInstance);

  /**
   * Use this only, if you are sure, that listener accepts the type event.
   * 
   * May be usefull, in cases where lamda expression and you know the type of event.
   * 
   * public void addOnEvent(MgcEventListener<TestFilter1Event> handler) { addEventListener(TestFilter1Event.class,
   * handler); }
   * 
   * @param event
   * @param listener
   */
  void addEventListener(Class<? extends MgcEvent> event, MgcEventListener<?> listener);
}
