package de.micromata.genome.util.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Used a ResourceBundle for translation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ResourceBundleTranslationProvider implements I18NTranslationProvider
{
  private ResourceBundle resourceBundle;

  public ResourceBundleTranslationProvider(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException ex) {
      return null;
    }
  }

  @Override
  public Set<String> keySet()
  {
    return resourceBundle.keySet();
  }

  @Override
  public String getId()
  {
    return resourceBundle.getBaseBundleName();
  }

  @Override
  public boolean needReload()
  {
    return false;
  }

}
