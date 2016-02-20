package de.micromata.genome.util.validation;

/**
 * Translates a message.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ValTranslateService
{
  /**
   * Translate an i18n key to a message.
   * 
   * @param key
   * @return
   */
  String translate(String key);

  /**
   * Translates a validation message into a string.
   * 
   * As side effect, the translation should be stored in ValMessage.setMessage().
   *
   * @param message the message
   * @return the string
   */
  String translate(ValMessage message);

}
