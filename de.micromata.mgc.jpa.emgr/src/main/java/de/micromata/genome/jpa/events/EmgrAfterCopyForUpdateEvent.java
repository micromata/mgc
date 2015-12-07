package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.IEmgr;

/**
 * Before an entity will by copied into an persistense Entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterCopyForUpdateEvent extends EmgrBaseCopyEvent
{

  /**
   * Instantiates a new emgr after copy for update event.
   *
   * @param emgr the emgr
   * @param iface the iface
   * @param target the target
   * @param source the source
   * @param forceUpdate the force update
   */
  public EmgrAfterCopyForUpdateEvent(IEmgr<?> emgr, Class<?> iface, Object target, Object source, boolean forceUpdate)
  {
    super(emgr, iface, target, source, forceUpdate);
  }

}
