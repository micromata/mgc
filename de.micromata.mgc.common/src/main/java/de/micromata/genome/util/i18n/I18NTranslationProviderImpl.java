package de.micromata.genome.util.i18n;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections15.iterators.IteratorEnumeration;

/**
 * Translates dynamically by current locale
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class I18NTranslationProviderImpl extends ResourceBundle implements I18NTranslationProvider
{
  private Map<Locale, I18NTranslationProvider> localeToTrans = new ConcurrentHashMap<>();
  private I18NLocaleProvider localeProvider;
  private I18NTranslationResolver resolver;

  public I18NTranslationProviderImpl(I18NLocaleProvider localeProvider, I18NTranslationResolver resolver)
  {
    this.localeProvider = localeProvider;
    this.resolver = resolver;
  }

  protected I18NTranslationProvider getCurrentTranslationProvider()
  {
    Locale locale = getLocale();
    I18NTranslationProvider transpro = localeToTrans.get(locale);
    if (transpro == null) {
      transpro = resolver.getTranslationFor(locale);
      localeToTrans.put(locale, transpro);
    }
    return transpro;
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    I18NTranslationProvider prov = getCurrentTranslationProvider();
    return prov.getTranslationForKey(key);
  }

  @Override
  public String getId()
  {
    I18NTranslationProvider prov = getCurrentTranslationProvider();
    return prov.getId();
  }

  @Override
  public boolean needReload()
  {
    return false;
  }

  @Override
  public Locale getLocale()
  {
    return localeProvider.getCurrentLocale();
  }

  @Override
  protected Object handleGetObject(String key)
  {
    return getTranslationForKey(key);
  }

  @Override
  public boolean containsKey(String key)
  {
    I18NTranslationProvider prov = getCurrentTranslationProvider();
    Set<String> set = prov.keySet();
    return set.contains(key);
  }

  @Override
  public Set<String> keySet()
  {
    I18NTranslationProvider prov = getCurrentTranslationProvider();
    return prov.keySet();
  }

  @Override
  public Enumeration<String> getKeys()
  {
    return new IteratorEnumeration<String>(keySet().iterator());
  }

}
