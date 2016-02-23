package de.micromata.genome.util.i18n;

import java.util.Locale;

/**
 * Resolves translation for one language.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface I18NTranslationResolver
{
  /**
   * For the locale get the translation proder.
   * 
   * @param locale
   * @return
   */
  I18NTranslationProvider getTranslationFor(Locale locale);
}
