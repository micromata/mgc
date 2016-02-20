package de.micromata.genome.util.validation;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractValTranslateService implements ValTranslateService
{

  @Override
  public String translate(ValMessage message)
  {
    if (message.getMessage() != null) {
      return message.getMessage();
    }
    String msg = translate(message.getI18nkey());
    if (message.getArguments() != null && message.getArguments().length > 0) {
      msg = String.format(msg, message.getArguments());
    }
    message.setMessage(msg);
    return null;
  }

}
