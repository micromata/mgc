package de.micromata.genome.util.i18n;

import java.util.Set;

/**
 * Wrapps a I18NTranslationProvider.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TranslationProviderWrapper implements I18NTranslationProvider
{
  protected I18NTranslationProvider nested;

  public TranslationProviderWrapper(I18NTranslationProvider nested)
  {
    this.nested = nested;
  }

  @Override
  public Set<String> keySet()
  {
    return nested.keySet();
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    return nested.getTranslationForKey(key);
  }

  @Override
  public String getId()
  {
    return nested.getId();
  }

  @Override
  public boolean needReload()
  {
    return nested.needReload();
  }

}
