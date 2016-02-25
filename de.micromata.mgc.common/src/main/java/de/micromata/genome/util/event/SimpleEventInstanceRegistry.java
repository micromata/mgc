package de.micromata.genome.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.micromata.genome.util.runtime.ClassUtils;

/**
 * Event Registry.
 * 
 * @param <T> the generic type of the event registry.
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SimpleEventInstanceRegistry extends AbstractMgcEventRegistry implements McEventInstanceRegistry
{
  private static final Logger LOG = Logger.getLogger(SimpleEventInstanceRegistry.class);
  private String registryName;

  /**
   * The listener map.
   */
  private Map<Class<? extends MgcEvent>, List<MgcEventListener<?>>> listenerMap = new HashMap<>();

  /**
   * Instantiates a new shpmtws config holder.
   * 
   * @param registryName the registryName
   */
  protected SimpleEventInstanceRegistry(String registryName)
  {
    super(registryName);
  }

  protected void addEvent(Class<? extends MgcEvent> event, MgcEventListener<?> listener)
  {
    Map<Class<? extends MgcEvent>, List<MgcEventListener<?>>> nmp = new HashMap<>();
    nmp.putAll(listenerMap);
    List<MgcEventListener<?>> list = nmp.get(event);
    if (list == null) {
      list = new ArrayList<>();
      nmp.put(event, list);
    }
    list.add(listener);
    listenerMap = nmp;

  }

  @Override
  public <EVENT extends MgcEvent, LISTENER extends MgcEventListener<EVENT>> void registerListener(
      LISTENER listenerInstance)
  {
    Class<? extends MgcEvent> argType = ClassUtils.getGenericTypeArgument(listenerInstance.getClass(), MgcEvent.class);
    if (argType == null) {
      LOG.error("Cannot determine Event type from Listener class: " + listenerInstance.getClass().getName());
      return;
    }
    addEvent(argType, listenerInstance);
  }

  /**
   * Gets the listener.
   * 
   * @param event the event
   * @return the listener
   */
  List<MgcEventListener<?>> getListener(MgcEvent event)
  {
    List<MgcEventListener<?>> ret = new ArrayList<>();
    for (Map.Entry<Class<? extends MgcEvent>, List<MgcEventListener<?>>> me : listenerMap
        .entrySet()) {
      if (me.getKey().isAssignableFrom(event.getClass()) == true) {
        for (MgcEventListener<?> listener : me.getValue()) {
          ret.add(listener);
        }
      }
    }
    return ret;
  }

  @Override
  public void submitEvent(MgcEvent event)
  {
    dispatchEvent(event);

  }

  /**
   * Invoke events.
   * 
   * @param event the event
   */
  @Override
  public void dispatchEvent(MgcEvent event)
  {

    List<MgcEventListener<?>> listeners = getListener(event);
    for (MgcEventListener<?> listener : listeners) {
      invokeListener(listener, event);
    }
  }

}
