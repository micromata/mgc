package de.micromata.genome.util.validation;

import org.apache.commons.lang.StringUtils;

/**
 * Base class on ValMessage related exceptions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class ValStatusException extends RuntimeException
{

  /**
   * Instantiates a new val status exception.
   *
   * @param message the message
   */
  public ValStatusException(String message)
  {
    super(message);
  }

  public ValState getValState()
  {
    return getWorstMessage().getValState();
  }

  public abstract ValMessage getWorstMessage();

  @Override
  public String getMessage()
  {
    StringBuilder sb = new StringBuilder();
    String smess = super.getMessage();
    if (StringUtils.isNotBlank(smess) == true) {
      sb.append(smess).append(": ");
    }
    ValMessage msg = getWorstMessage();
    if (msg.getMessage() != null) {
      sb.append(msg.getMessage());
    } else {
      sb.append(msg.getI18nkey());
    }
    if (msg.getReference() != null) {
      sb.append(" on ").append(msg.getReference().getClass().getSimpleName());
    }
    if (StringUtils.isNotBlank(msg.getProperty()) == true) {
      sb.append(".").append(msg.getReference());
    }
    return sb.toString();
  }
}
