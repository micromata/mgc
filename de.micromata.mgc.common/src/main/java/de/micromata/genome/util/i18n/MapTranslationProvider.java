//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
