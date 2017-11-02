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

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides the translation for one language.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface I18NTranslationProvider
{
  /**
   * Get all keys for a language.
   *
   * @return
   */
  Set<String> keySet();

  default String translate(String key)
  {
    Object ores = getTranslationForKey(key);
    if (ores == null) {
      return null;
    }
    return Objects.toString(ores, StringUtils.EMPTY);
  }

  /**
   * get the translation for a i18n key.
   *
   * @param key
   * @return null if not found
   */
  Object getTranslationForKey(String key);

  /**
   * The id of the underlying genome config resource.
   *
   * @return
   */
  String getId();

  /**
   * Need reload.
   *
   * @return true, if successful
   */
  boolean needReload();
}
