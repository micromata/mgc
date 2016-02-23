package de.micromata.genome.util.i18n;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections15.iterators.IteratorEnumeration;

/**
 * Wrapps a I18NTranslationProvider as a ResourceBundle.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TranslationProviderResourceBundleWrapper extends ResourceBundle implements I18NTranslationProvider
{
  private I18NTranslationProvider nested;

  public TranslationProviderResourceBundleWrapper(I18NTranslationProvider nested)
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

  @Override
  protected Object handleGetObject(String key)
  {
    return getTranslationForKey(key);
  }

  @Override
  public Enumeration<String> getKeys()
  {
    return new IteratorEnumeration<String>(keySet().iterator());
  }

}
