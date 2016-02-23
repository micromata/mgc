package de.micromata.genome.util.i18n;

import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

/**
 * Provides the translation for one language.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface I18NTranslationProvider
{
  /**
   * Get all keys for a language.
   *
   * @return
   */
  Set<String> keySet();

  default String translate(String key)
  {
    Object ores = getTranslationForKey(key);
    if (ores == null) {
      return null;
    }
    return ObjectUtils.toString(ores);
  }

  /**
   * get the translation for a i18n key.
   *
   * @param key
   * @return null if not found
   */
  Object getTranslationForKey(String key);

  /**
   * The id of the underlying genome config resource.
   *
   * @return
   */
  String getId();

  /**
   * Need reload.
   *
   * @return true, if successful
   */
  boolean needReload();
}
