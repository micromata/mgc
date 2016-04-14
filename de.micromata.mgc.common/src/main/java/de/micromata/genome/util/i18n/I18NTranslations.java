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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility for some standard implementations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class I18NTranslations
{
  static I18NLocaleProvider defaultProvider = new I18NLocaleProvider()
  {

    @Override
    public Locale getCurrentLocale()
    {
      return Locale.getDefault();
    }

  };
  static NoTranslationProvider noTranslationProvider = new NoTranslationProvider();

  public static I18NLocaleProvider systemDefaultLocaleProvider()
  {
    return defaultProvider;
  }

  public static I18NTranslationProvider noTranslationProvider()
  {
    return noTranslationProvider;
  }

  public static ResourceBundle asResourceBundle(I18NTranslationProvider provider)
  {
    if (provider instanceof ResourceBundle) {
      return (ResourceBundle) provider;
    }
    return new TranslationProviderResourceBundleWrapper(provider);
  }

}
