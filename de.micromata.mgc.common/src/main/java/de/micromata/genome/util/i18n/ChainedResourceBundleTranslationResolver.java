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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

/**
 * Uses ResourceBundles loaded from class path.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ChainedResourceBundleTranslationResolver implements I18NTranslationResolver
{
  private String[] resIds;

  /**
   * 
   * @param resIds path to class path, from higher to lower priority
   */
  public ChainedResourceBundleTranslationResolver(String... resIds)
  {
    this.resIds = resIds;
  }

  @Override
  public I18NTranslationProvider getTranslationFor(Locale locale)
  {
    Map<String, Object> entries = new HashMap<>();
    for (String resId : resIds) {
      ResourceBundle resbundle = ResourceBundle.getBundle(resId, locale);
      for (String key : resbundle.keySet()) {
        entries.putIfAbsent(key, resbundle.getObject(key));
      }
    }
    return new MapTranslationProvider(StringUtils.join(resIds, "_"), entries);
  }

}
