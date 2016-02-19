package de.micromata.genome.util.validation;

import java.util.ResourceBundle;

/**
 * Provides standard translation services.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValTranslateServices
{
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
