package de.micromata.mgc.db.jpa.api.events;

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
