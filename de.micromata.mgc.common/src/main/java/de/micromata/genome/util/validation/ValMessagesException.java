package de.micromata.genome.util.validation;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Mesasge with a list of message.
 * 
 * NOTE: the List must never by empty.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValMessagesException extends ValStatusException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 8970392691691721620L;

  /**
   * The val messages.
   */
  private List<ValMessage> valMessages;

  /**
   * Instantiates a new val messages exception.
   *
   * @param message the message
   * @param valMessages the val messages
   */
  public ValMessagesException(String message, List<ValMessage> valMessages)
  {
    super(message);
    this.valMessages = valMessages;
    Validate.isTrue(valMessages.isEmpty() == false);
  }

  public ValMessagesException(List<ValMessage> valMessages)
  {
    this("", valMessages);
  }

  @Override
  public ValMessage getWorstMessage()
  {
    ValMessage ret = null;
    for (ValMessage msg : valMessages) {
      if (ret == null) {
        ret = msg;
        continue;
      }
      if (msg.getValState().isWorse(ret.getValState()) == true) {
        ret = msg;
      }
    }
    return ret;
  }

}
