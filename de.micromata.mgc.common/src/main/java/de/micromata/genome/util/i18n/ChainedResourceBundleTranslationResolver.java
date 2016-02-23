package de.micromata.genome.util.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * Uses ResourceBundles loaded from class path.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ChainedResourceBundleTranslationResolver implements I18NTranslationResolver
{
  private String[] resIds;

  /**
   * 
   * @param resIds path to class path, from higher to lower priority
   */
  public ChainedResourceBundleTranslationResolver(String... resIds)
  {
    this.resIds = resIds;
  }

  @Override
  public I18NTranslationProvider getTranslationFor(Locale locale)
  {
    Map<String, Object> entries = new HashMap<>();
    for (String resId : resIds) {
      ResourceBundle resbundle = ResourceBundle.getBundle(resId, locale);
      for (String key : resbundle.keySet()) {
        entries.putIfAbsent(key, resbundle.getObject(key));
      }
    }
    return new MapTranslationProvider(StringUtils.join(resIds, "_"), entries);
  }

}
