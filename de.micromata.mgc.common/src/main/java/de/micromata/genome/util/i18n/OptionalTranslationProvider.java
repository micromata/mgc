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

import org.apache.commons.lang3.StringUtils;

/**
 * translates only, if key starts with %.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class OptionalTranslationProvider extends TranslationProviderWrapper
{

  public OptionalTranslationProvider(I18NTranslationProvider nested)
  {
    super(nested);

  }

  @Override
  public Object getTranslationForKey(String key)
  {
    if (StringUtils.startsWith(key, "%") == true) {
      String nkey = key.substring(1);
      return super.getTranslationForKey(nkey);
    }
    return key;
  }

}
