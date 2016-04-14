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

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Used a ResourceBundle for translation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ResourceBundleTranslationProvider implements I18NTranslationProvider
{
  private ResourceBundle resourceBundle;

  public ResourceBundleTranslationProvider(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
  }

  @Override
  public Object getTranslationForKey(String key)
  {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException ex) {
      return null;
    }
  }

  @Override
  public Set<String> keySet()
  {
    return resourceBundle.keySet();
  }

  @Override
  public String getId()
  {
    return resourceBundle.getBaseBundleName();
  }

  @Override
  public boolean needReload()
  {
    return false;
  }

}
