package de.micromata.genome.util.validation;

/**
 * Contains a validation message.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ValMessageException extends ValStatusException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8132019164228653852L;

  /**
   * The val message.
   */
  private ValMessage valMessage;

  /**
   * Instantiates a new val message exception.
   *
   * @param message the message
   * @param valMessage the val message
   */
  public ValMessageException(String message, ValMessage valMessage)
  {
    super(message);
    this.valMessage = valMessage;

  }

  @Override
  public ValMessage getWorstMessage()
  {
    return valMessage;
  }

}
