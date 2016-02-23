package de.micromata.genome.util.i18n;

import org.apache.commons.lang.StringUtils;

/**
 * translates only, if key starts with %.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class OptionalTranslationProvider extends TranslationProviderWrapper
{

  public OptionalTranslationProvider(I18NTranslationProvider nested)
  {
    super(nested);

  }

  @Override
  public Object getTranslationForKey(String key)
  {
    if (StringUtils.startsWith(key, "%") == true) {
      String nkey = key.substring(1);
      return super.getTranslationForKey(nkey);
    }
    return key;
  }

}
