package de.micromata.genome.util.i18n;

import java.util.Map;
import java.util.Set;

/**
 * Stores a translation in a unsyncrhonized map.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MapTranslationProvider implements I18NTranslationProvider
{
  private String resId;
  private Map<String, Object> entryMap;

  public MapTranslationProvider(String resId, Map<String, Object> entryMap)
  {
    this.resId = resId;
    this.entryMap = entryMap;
  }

  @Override
  public Set<String> keySet()
  {
    return entryMap.keySet();
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    return entryMap.get(key);
  }

  @Override
  public String getId()
  {
    return resId;
  }

  @Override
  public boolean needReload()
  {
    return false;
  }
}
