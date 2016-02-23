package de.micromata.genome.util.i18n;

import org.apache.commons.lang.ObjectUtils;

import de.micromata.genome.util.text.PlaceHolderReplacer;
import de.micromata.genome.util.text.StringResolver;

/**
 * Evaluate I{} sub expression and translates them.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PlaceholderTranslationProvider extends TranslationProviderWrapper
{
  public PlaceholderTranslationProvider(I18NTranslationProvider nested)
  {
    super(nested);

  }

  @Override
  public Object getTranslationForKey(String key)
  {
    Object value = super.getTranslationForKey(key);
    if ((value instanceof String) == false) {
      return value;
    }
    String svalue = (String) value;
    String resolved = PlaceHolderReplacer.resolveReplace(svalue, "I{", "}", new StringResolver()
    {

      @Override
      public String resolve(String placeholder)
      {
        return ObjectUtils.toString(getTranslationForKey(placeholder));
      }
    });
    return resolved;
  }
}
