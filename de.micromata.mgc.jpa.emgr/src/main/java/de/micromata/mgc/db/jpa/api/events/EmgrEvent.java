package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Base classes to all Events invoked by Emgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrEvent
{

  /**
   * The emgr.
   */
  private final IEmgr<?> emgr;

  /**
   * Instantiates a new emgr event.
   *
   * @param emgr the emgr
   */
  public EmgrEvent(IEmgr<?> emgr)
  {
    this.emgr = emgr;
  }

  public IEmgr<?> getEmgr()
  {
    return emgr;
  }

  @Override
  public String toString()
  {
    return super.toString();// + ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
  }
}
