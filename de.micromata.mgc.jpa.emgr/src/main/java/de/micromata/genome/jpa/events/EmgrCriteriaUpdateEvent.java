package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;

/**
 * Base event with CriteriaUpdate.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrCriteriaUpdateEvent extends EmgrEvent
{

  /**
   * The criteria update.
   */
  private CriteriaUpdate<?> criteriaUpdate;

  /**
   * Instantiates a new emgr criteria update event.
   *
   * @param emgr the emgr
   * @param criteriaUpdate the criteria update
   */
  public EmgrCriteriaUpdateEvent(IEmgr<?> emgr, CriteriaUpdate<?> criteriaUpdate)
  {
    super(emgr);
    this.criteriaUpdate = criteriaUpdate;
  }

  public CriteriaUpdate<?> getCriteriaUpdate()
  {
    return criteriaUpdate;
  }
}
