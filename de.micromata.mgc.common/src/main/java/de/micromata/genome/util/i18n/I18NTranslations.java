package de.micromata.genome.util.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility for some standard implementations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class I18NTranslations
{
  static I18NLocaleProvider defaultProvider = new I18NLocaleProvider()
  {

    @Override
    public Locale getCurrentLocale()
    {
      return Locale.getDefault();
    }

  };
  static NoTranslationProvider noTranslationProvider = new NoTranslationProvider();

  public static I18NLocaleProvider systemDefaultLocaleProvider()
  {
    return defaultProvider;
  }

  public static I18NTranslationProvider noTranslationProvider()
  {
    return noTranslationProvider;
  }

  public static ResourceBundle asResourceBundle(I18NTranslationProvider provider)
  {
    if (provider instanceof ResourceBundle) {
      return (ResourceBundle) provider;
    }
    return new TranslationProviderResourceBundleWrapper(provider);
  }

}
