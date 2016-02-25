package de.micromata.genome.util.event;

/**
 * A registry for events.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface MgcEventRegistry
{
  default String getRegistryName()
  {
    return getClass().getName();
  }

  /**
   * Submit an event.
   * 
   * @param event
   */
  void submitEvent(MgcEvent event);

  /**
   * Submit and immediatelly dispatch event.
   * 
   * @param event
   */
  void dispatchEvent(MgcEvent event);
}
