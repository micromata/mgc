package de.micromata.mgc.db.jpa.api.events;

import de.micromata.mgc.db.jpa.CriteriaUpdate;
import de.micromata.mgc.db.jpa.api.IEmgr;

/**
 * Event will be invoked before an criteri update will be executed.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBeforeCriteriaUpdateEvent extends EmgrCriteriaUpdateEvent
{

  /**
   * Instantiates a new emgr before criteria update event.
   *
   * @param emgr the emgr
   * @param criteriaUpdate the criteria update
   */
  public EmgrBeforeCriteriaUpdateEvent(IEmgr<?> emgr, CriteriaUpdate<?> criteriaUpdate)
  {
    super(emgr, criteriaUpdate);
  }

}
