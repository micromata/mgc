package de.micromata.genome.util.i18n;

import java.util.Locale;

/**
 * Provides a Locale for current user/call
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface I18NLocaleProvider
{
  /**
   * 
   * @return NEVER NULL
   */
  Locale getCurrentLocale();
}
