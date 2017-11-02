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

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections4.iterators.IteratorEnumeration;

/**
 * Wrapps a I18NTranslationProvider as a ResourceBundle.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TranslationProviderResourceBundleWrapper extends ResourceBundle implements I18NTranslationProvider
{
  private I18NTranslationProvider nested;

  public TranslationProviderResourceBundleWrapper(I18NTranslationProvider nested)
  {
    this.nested = nested;
  }

  @Override
  public Set<String> keySet()
  {
    return nested.keySet();
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    return nested.getTranslationForKey(key);
  }

  @Override
  public String getId()
  {
    return nested.getId();
  }

  @Override
  public boolean needReload()
  {
    return nested.needReload();
  }

  @Override
  protected Object handleGetObject(String key)
  {
    return getTranslationForKey(key);
  }

  @Override
  public Enumeration<String> getKeys()
  {
    return new IteratorEnumeration<String>(keySet().iterator());
  }

}
