/*
 *
 */
package de.micromata.genome.util.event;

/**
 * An Event listener.
 *
 * An Eventlister should be provide a default constructor and an stateless and multithread enabled implementation.
 *
 * @param <T>
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface MgcEventListener<T extends MgcEvent>
{

  /**
   * Callback for an event.
   *
   * @param event the event
   */
  void onEvent(T event);
}
