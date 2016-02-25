package de.micromata.genome.util.event;

import java.util.List;

public interface MgcFilterEvent<R>extends MgcEvent
{
  /**
   * Call next filter in chain.
   */
  void nextFilter();

  /**
   * Gets the result.
   *
   * @return the result
   */
  R getResult();

  /**
   * Sets the result.
   *
   * @param result the new result
   */
  void setResult(R result);

  /**
   * Used internal
   * 
   * @param eventHandlerList
   */
  void setEventHandlerChain(List<MgcEventListener> eventHandlerList);
}
