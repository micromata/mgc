package de.micromata.genome.util.i18n;

import java.util.Collections;
import java.util.Set;

/**
 * Does nothing, but return key as translated value.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class NoTranslationProvider implements I18NTranslationProvider
{

  @Override
  public Set<String> keySet()
  {
    return Collections.emptySet();
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    return key;
  }

  @Override
  public String getId()
  {
    return "notrans";
  }

  @Override
  public boolean needReload()
  {
    return false;
  }

}
