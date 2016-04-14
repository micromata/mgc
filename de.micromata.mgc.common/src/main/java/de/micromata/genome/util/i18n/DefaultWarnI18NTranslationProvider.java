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

import org.apache.log4j.Logger;

/**
 * if nested provider has no translation, logs in Log4J a warn and return ???key???.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DefaultWarnI18NTranslationProvider extends TranslationProviderWrapper
{
  private static final Logger LOG = Logger.getLogger(DefaultWarnI18NTranslationProvider.class);

  public DefaultWarnI18NTranslationProvider(I18NTranslationProvider nested)
  {
    super(nested);
  }

  @Override
  public Object getTranslationForKey(String key)
  {

    Object ret = super.getTranslationForKey(key);
    if (ret != null) {
      return ret;
    }
    LOG.warn("Translation for key not found: " + key);
    return "???" + key + "???";
  }

}
