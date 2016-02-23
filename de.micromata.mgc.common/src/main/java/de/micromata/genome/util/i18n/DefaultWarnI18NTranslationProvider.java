package de.micromata.genome.util.i18n;

import org.apache.log4j.Logger;

/**
 * if nested provider has no translation, logs in Log4J a warn and return ???key???.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DefaultWarnI18NTranslationProvider extends TranslationProviderWrapper
{
  private static final Logger LOG = Logger.getLogger(DefaultWarnI18NTranslationProvider.class);

  public DefaultWarnI18NTranslationProvider(I18NTranslationProvider nested)
  {
    super(nested);
  }

  @Override
  public Object getTranslationForKey(String key)
  {

    Object ret = super.getTranslationForKey(key);
    if (ret != null) {
      return ret;
    }
    LOG.warn("Translation for key not found: " + key);
    return "???" + key + "???";
  }

}
