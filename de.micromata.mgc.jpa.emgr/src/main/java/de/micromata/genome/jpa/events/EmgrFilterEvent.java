package de.micromata.genome.jpa.events;

import java.util.List;

import org.apache.log4j.Logger;

import de.micromata.genome.jpa.IEmgr;

/**
 * Event used by filter syntax.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic return type.
 */
public abstract class EmgrFilterEvent<R>extends EmgrEvent
{

  /**
   * The log.
   */
  private static Logger LOG = Logger.getLogger(EmgrFilterEvent.class);
  /**
   * the filter chain.
   */
  @SuppressWarnings("rawtypes")
  private List<EmgrEventHandler> eventHandlerList;
  /**
   * position of the filter chain.
   */
  private int curIndx = 0;

  /**
   * The result.
   */
  private R result;

  /**
   * Instantiates a new emgr filter event.
   *
   * @param emgr the emgr
   */
  public EmgrFilterEvent(IEmgr<?> emgr)
  {
    super(emgr);
  }

  /**
   * Call next filter in chain.
   */
  @SuppressWarnings("unchecked")
  public void nextFilter()
  {
    if (curIndx >= eventHandlerList.size()) {
      return;
    }
    ++curIndx;
    EmgrEventHandler ehandler = eventHandlerList.get(curIndx - 1);
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("EmgrFilter dispatch " + ehandler + "(" + this + ")");
    }
    ehandler.onEvent(this);
  }

  /**
   * Gets the result.
   *
   * @return the result
   */
  public R getResult()
  {
    return result;
  }

  /**
   * Sets the result.
   *
   * @param result the new result
   */
  public void setResult(R result)
  {
    this.result = result;
  }

  /**
   * Internal method to set chain.
   *
   * @param eventHandlerList the new event handler chain
   */
  @SuppressWarnings("rawtypes")
  public void setEventHandlerChain(List<EmgrEventHandler> eventHandlerList)
  {
    curIndx = 0;
    this.eventHandlerList = eventHandlerList;
  }
}
