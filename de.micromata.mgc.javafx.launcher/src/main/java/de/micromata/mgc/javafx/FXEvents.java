package de.micromata.mgc.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.launcher.gui.AbstractController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FXEvents
{
  private static FXEvents INSTANCE = new FXEvents();
  private final WeakHashMap<AbstractController<?>, List<Node>> listeners = new WeakHashMap<>();

  public static FXEvents get()
  {
    return INSTANCE;
  }

  public void registerListener(AbstractController<?> controller, Node node)
  {
    listeners.putIfAbsent(controller, new ArrayList<Node>());
    listeners.get(controller).add(node);
  }

  /**
   * Adds an eventhandler to the selected node.
   * 
   * @param node node to append the eventhandler to.
   * @param handler the event handler to append
   * @param type event type
   * @param controller the controller
   * @return list of controller nodes that want to be notified in case of an event fired by fireEvent.
   */
  public <E extends Event> void addEventHandler(AbstractController<?> controller, Node node, EventType<E> type,
      EventHandler<E> handler)
  {
    node.addEventHandler(type, handler);

    List<Node> eventSinks = listeners.get(controller);
    if (eventSinks == null) {
      eventSinks = new ArrayList<>();
      listeners.put(controller, eventSinks);
    }
    Optional<Node> optexist = eventSinks.stream().filter(lnode -> lnode.equals(node)).findFirst();
    if (optexist.isPresent() == false) {
      eventSinks.add(node);
    }
  }

  public void registerValMessageReceiver(AbstractController<?> controller, Node node,
      Class<?> referenceType, String property)
  {
    registerListener(controller, node);
    node.addEventHandler(ValMessageEvent.MESSAGE_EVENT_TYPE, event -> {
      ValMessage msg = event.getMessage();
      if (referenceType != null && msg.getReference() != null) {
        if (referenceType.isAssignableFrom(msg.getReference().getClass()) == false) {
          return;
        }
        if (StringUtils.isNotBlank(property) && StringUtils.isNotBlank(msg.getProperty()) == true) {
          if (property.equals(msg.getProperty()) == false) {
            return;
          }
        }
        controller.addToFeedback(msg);
        FXGuiUtils.markErroneousField(controller, node, msg);
        event.consume();
      }

    });
  }

  public void fireEvent(Event event)
  {
    for (List<Node> nodes : listeners.values()) {
      for (Node node : nodes) {
        node.fireEvent(event);
        if (event.isConsumed() == true) {
          break;
        }
      }
    }
  }
}
