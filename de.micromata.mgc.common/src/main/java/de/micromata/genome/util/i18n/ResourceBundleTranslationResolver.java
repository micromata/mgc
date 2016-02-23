package de.micromata.genome.util.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Resolves to rsource bundle.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ResourceBundleTranslationResolver implements I18NTranslationResolver
{
  private String cpResId;

  public ResourceBundleTranslationResolver(String cpResId)
  {
    this.cpResId = cpResId;
  }

  @Override
  public I18NTranslationProvider getTranslationFor(Locale locale)
  {
    return new ResourceBundleTranslationProvider(ResourceBundle.getBundle(cpResId, locale));
  }

}
