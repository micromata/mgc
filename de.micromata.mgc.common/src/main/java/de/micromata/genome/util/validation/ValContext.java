package de.micromata.genome.util.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import de.micromata.genome.util.i18n.I18NTranslationProvider;

/**
 * A Validation Context.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValContext
{

  /**
   * The messages.
   */
  private List<ValMessage> messages = new ArrayList<>();

  /**
   * Instantiates a new val context.
   */
  public ValContext()
  {

  }

  /**
   * A validation sub context, where in the ValMessage reference and property will be set.
   *
   * @param reference the reference
   * @param property the property
   * @return the val context
   */
  public ValContext createSubContext(Object reference, String property)
  {
    return new ValContextDelegate(this)
    {
      private List<ValMessage> localMessages = new ArrayList<>();

      @Override
      public ValMessage createValMessage()
      {
        ValMessage vm = parent.createValMessage();
        vm.setReference(reference);
        vm.setProperty(property);
        return vm;
      }

      @Override
      public void addMessage(ValMessage vm)
      {
        super.addMessage(vm);
        localMessages.add(vm);
      }

      @Override
      public boolean hasLocalError()
      {
        return hasAtLeastLevel(localMessages, ValState.Error);
      }

    };
  }

  /**
   * Adds the error.
   *
   * @param i18nkey the i18nkey
   */
  public void error(String i18nkey)
  {
    add(ValState.Error, null, i18nkey);
  }

  public void error(String property, String i18nkey)
  {
    add(ValState.Error, property, i18nkey);
  }

  public void error(String property, String i18nkey, Exception ex)
  {
    add(ValState.Error, property, i18nkey, ex);
  }

  public void warn(String i18nkey)
  {
    add(ValState.Warning, null, i18nkey);
  }

  public void warn(String property, String i18nkey)
  {
    add(ValState.Warning, property, i18nkey);
  }

  public void info(String i18nkey)
  {
    add(ValState.Info, null, i18nkey);
  }

  public void info(String property, String i18nkey)
  {
    add(ValState.Info, property, i18nkey);
  }

  public void directError(String property, String message)
  {
    addDirect(ValState.Error, property, message, null);
  }

  public void directError(String property, String message, Exception ex)
  {
    addDirect(ValState.Error, property, message, ex);
  }

  public void directWarn(String property, String message)
  {
    addDirect(ValState.Warning, property, message, null);
  }

  public void directInfo(String property, String message)
  {
    addDirect(ValState.Info, property, message, null);
  }

  public void translateMessages(I18NTranslationProvider transService)
  {
    for (ValMessage message : messages) {
      message.getTranslatedMessage(transService);
    }
  }

  /**
   * Creates the val message.
   *
   * @return the val message
   */
  protected ValMessage createValMessage()
  {
    return new ValMessage();
  }

  /**
   * Adds the.
   *
   * @param valState the val state
   * @param i18nkey the i18nkey
   */
  public void add(ValState valState, String property, String i18nkey)
  {
    add(valState, property, i18nkey, null);
  }

  public void add(ValState valState, String property, String i18nkey, Exception ex)
  {
    ValMessage vm = createValMessage();
    vm.addProperty(property);
    vm.setValState(valState);
    vm.setI18nkey(i18nkey);
    vm.setException(ex);
    addMessage(vm);
  }

  public void addDirect(ValState valState, String property, String message, Exception ex)
  {
    ValMessage vm = createValMessage();
    vm.addProperty(property);
    vm.setValState(valState);
    vm.setMessage(message);
    vm.setException(ex);
    addMessage(vm);
  }

  /**
   * Adds the message.
   *
   * @param vm the vm
   */
  public void addMessage(ValMessage vm)
  {
    messages.add(vm);

  }

  public List<ValMessage> getMessages()
  {
    return messages;
  }

  /**
   * Returns <code>true</code> if this context has any validation messages.
   * 
   * @return true or false
   */
  public boolean hasMessages()
  {
    return getMessages().isEmpty() == false;
  }

  public boolean hasErrors()
  {
    return hasAtLeastLevel(messages, ValState.Error);
  }

  protected boolean hasAtLeastLevel(List<ValMessage> messages, ValState state)
  {
    return messages.stream().anyMatch(msg -> compareValidationLevels(msg.getValState(), state) >= 0);
  }

  public boolean hasLocalError()
  {
    return hasErrors();
  }

  /**
   * Checks if the given message already exists in this context. Messages are considered as 'equal' if their property is
   * equal and the existing message has an equal or worse validation state.
   * 
   * @param message the message to check
   * @return <code>true</code> if message already exists, <code>false</code> otherwise
   */
  public boolean messageExitsForElement(final ValMessage message)
  {
    boolean ret = messages.stream().anyMatch(msg -> {
      return msg.getReference() == message.getReference() &&
          ObjectUtils.equals(msg.getProperty(), message.getProperty()) &&
          compareValidationLevels(msg.getValState(), message.getValState()) >= 0;
    });
    return ret;
  }

  /**
   * Compares two validation states by their level.
   * 
   * @param state1 the first state
   * @param state2 the second state
   * @return 1 if state1 > state2, 0 if equal, -1 if state1 < state2
   */
  private int compareValidationLevels(ValState state1, ValState state2)
  {
    int level1 = state1 == null ? ValState.Unvalidated.getLevel() : state1.getLevel();
    int level2 = state2 == null ? ValState.Unvalidated.getLevel() : state2.getLevel();
    return Integer.compare(level1, level2);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (ValMessage msg : messages) {
      sb.append("\n").append(msg);
    }
    return sb.toString();
  }
}
