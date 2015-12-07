package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;

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
