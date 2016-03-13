package de.micromata.mgc.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.launcher.gui.AbstractModelController;
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
  private static final Logger LOG = Logger.getLogger(FXEvents.class);
  private static FXEvents INSTANCE = new FXEvents();
  private final WeakHashMap<AbstractModelController<?>, List<Node>> listeners = new WeakHashMap<>();

  public static FXEvents get()
  {
    return INSTANCE;
  }

  public void registerListener(AbstractModelController<?> controller, Node node)
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
  public <E extends Event> void addEventHandler(AbstractModelController<?> controller, Node node, EventType<E> type,
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

  public void registerValMessageReceiver(AbstractModelController<?> controller, Node node,
      Class<?> referenceType, String property)
  {
    registerListener(controller, node);
    node.addEventHandler(ValMessageEvent.MESSAGE_EVENT_TYPE, event -> {
      ValMessage msg = event.getMessage();
      if (msg.isConsumed() == true) {
        return;
      }
      if (referenceType != null && msg.getReference() != null) {
        if (referenceType.isAssignableFrom(msg.getReference().getClass()) == false) {
          return;
        }
        if (StringUtils.isNotBlank(property) && StringUtils.isNotBlank(msg.getProperty()) == true) {
          if (property.equals(msg.getProperty()) == true) {
            controller.addToFeedback(msg);
            FXGuiUtils.markErroneousField(controller, node, msg);

            event.consume();

          }
        }
        controller.addToFeedback(msg);
      }

    });
  }

  public void registerValMessageReceiver(AbstractModelController<?> controller, Node node,
      Object model, String property)
  {
    if (node == null) {
      LOG.error("Node is null on property: " + model.getClass().getSimpleName() + "." + property);
    }
    registerListener(controller, node);
    node.addEventHandler(ValMessageEvent.MESSAGE_EVENT_TYPE, event -> {
      ValMessage msg = event.getMessage();
      if (msg.isConsumed() == true) {
        return;
      }
      Object themod = model;
      String theprop = property;

      if (StringUtils.isNotBlank(property) && StringUtils.isNotBlank(msg.getProperty()) == true
          && property.equals(msg.getProperty()) == true) {
        if (themod == msg.getReference()) {
          controller.addToFeedback(msg);
          FXGuiUtils.markErroneousField(controller, node, msg);
          event.consume();
        }

      } else if (msg.isConsumed() == false && themod == msg.getReference()) {
        controller.addToFeedback(msg);
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
