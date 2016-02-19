package de.micromata.genome.util.validation;

import java.util.ResourceBundle;

/**
 * Translates a message.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ValTranslateService
{

  /**
   * Translates a validation message into a string.
   * 
   * As side effect, the translation should be stored in ValMessage.setMessage().
   *
   * @param message the message
   * @return the string
   */
  String translate(ValMessage message);

  /**
   * Does not translate, uses the i18n key es message.
   *
   * @return the val translate service
   */
  public static ValTranslateService noTranslation()
  {
    return (message) -> {
      if (message.getMessage() != null) {
        return message.getMessage();
      }
      String msg;
      if (message.getArguments() != null && message.getArguments().length > 0) {
        msg = String.format(message.getI18nkey(), message.getArguments());
      } else {
        msg = message.getI18nkey();
      }
      message.setMessage(msg);
      return msg;
    };
  }

  /**
   * Resource bundle translation.
   *
   * @param bundle the bundle
   * @return the val translate service
   */
  public static ValTranslateService resourceBundleTranslation(ResourceBundle bundle)
  {
    return (message) -> {
      if (message.getMessage() != null) {
        return message.getMessage();
      }
      String trans = bundle.getString(message.getI18nkey());
      String msg;
      if (message.getArguments() != null && message.getArguments().length > 0) {
        msg = String.format(trans, message.getArguments());
      } else {
        msg = trans;
      }
      message.setMessage(msg);
      return msg;
    };
  }
}
