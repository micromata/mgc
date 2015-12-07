package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.IEmgr;

/**
 * Base for Copy events.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBaseCopyEvent extends EmgrEvent
{

  /**
   * The iface.
   */
  private Class<?> iface;

  /**
   * The target.
   */
  private Object target;

  /**
   * The source.
   */
  private Object source;

  /**
   * The force update.
   */
  private boolean forceUpdate;

  /**
   * Instantiates a new emgr base copy event.
   *
   * @param emgr the emgr
   * @param iface the iface
   * @param target the target
   * @param source the source
   * @param forceUpdate the force update
   */
  public EmgrBaseCopyEvent(IEmgr<?> emgr, Class<?> iface, Object target, Object source, boolean forceUpdate)
  {
    super(emgr);
    this.iface = iface;
    this.target = target;
    this.source = source;
    this.forceUpdate = forceUpdate;
  }

  public Class<?> getIface()
  {
    return iface;
  }

  public void setIface(Class<?> iface)
  {
    this.iface = iface;
  }

  public Object getTarget()
  {
    return target;
  }

  public void setTarget(Object target)
  {
    this.target = target;
  }

  public Object getSource()
  {
    return source;
  }

  public void setSource(Object source)
  {
    this.source = source;
  }

  public boolean isForceUpdate()
  {
    return forceUpdate;
  }

  public void setForceUpdate(boolean forceUpdate)
  {
    this.forceUpdate = forceUpdate;
  }
}
