package de.micromata.genome.jpa.events;

/**
 * Marker interface.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public interface EmgrEventHandler<E extends EmgrEvent>
{

  /**
   * An event is called.
   *
   * @param event the event
   */
  void onEvent(E event);
}
