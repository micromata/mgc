package de.micromata.genome.util.event;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Base class for Event registry.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractMgcEventRegistry implements MgcEventRegistry
{
  protected String registryName;

  public AbstractMgcEventRegistry()
  {

  }

  public AbstractMgcEventRegistry(String registryName)
  {
    this.registryName = registryName;
  }

  /**
   * Wrapp the invocation of the event handler.
   * 
   * @param listener the listener
   * @param event the event
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void invokeListener(MgcEventListener listener, final MgcEvent event)
  {
    listener.onEvent(event);
  }

  protected abstract List<MgcEventListener> collectEventFilter(MgcEvent event);

  @Override
  public <R, E extends MgcFilterEvent<R>> R filterEvent(E event, MgcEventListener<E> execute)
  {
    List<MgcEventListener> handlerList = collectEventFilter(event);
    handlerList.add(execute);
    event.setEventHandlerChain(handlerList);
    event.nextFilter();
    return event.getResult();
  }

  @Override
  public String getRegistryName()
  {
    if (StringUtils.isNotBlank(registryName) == true) {
      return registryName;
    }
    return MgcEventRegistry.super.getRegistryName();
  }

  public void setRegistryName(String registryName)
  {
    this.registryName = registryName;
  }

}
