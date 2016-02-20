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
  private static ValTranslateService NOTRANSLATE = new AbstractValTranslateService()
  {

    @Override
    public String translate(String key)
    {
      return key;
    }

  };

  /**
   * Does not translate, uses the i18n key es message.
   *
   * @return the val translate service
   */
  public static ValTranslateService noTranslation()
  {
    return NOTRANSLATE;
  }

  /**
   * Resource bundle translation.
   *
   * @param bundle the bundle
   * @return the val translate service
   */
  public static ValTranslateService resourceBundleTranslation(ResourceBundle bundle)
  {
    return new AbstractValTranslateService()
    {

      @Override
      public String translate(String key)
      {
        return bundle.getString(key);
      }
    };
  }
}
