package de.micromata.genome.jpa.events.impl;

import java.util.List;

import javax.persistence.Query;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.WrappedQuery;
import de.micromata.genome.jpa.events.EmgrQueryGetResultListFilterEvent;

/**
 * Event on the call Query.getResultList().
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrEventQuery extends WrappedQuery
{
  /**
   * The emgr.
   */
  private IEmgr<?> emgr;

  public EmgrEventQuery(IEmgr<?> emgr, Query nested)
  {
    super(nested);
    this.emgr = emgr;
  }

  @Override
  public List getResultList()
  {
    return emgr.getEmgrFactory().getEventFactory().invokeEvents(
        new EmgrQueryGetResultListFilterEvent(emgr, nested),
        (event) -> {
          event.setResult(nested.getResultList());
        });
  }

}
