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

package de.micromata.genome.util.runtime;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.collections.OrderedProperties;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsIncludeReplacer implements OrderedProperties.KeyValueReplacer
{

  /**
   *
  */

  private final LocalSettings localSettings;
  /**
   * The load dir.
   */
  File loadDir;

  /**
   * Instantiates a new include replacer.
   * 
   * @param loadDir the load dir
   * @param localSettings TODO
   */
  public LocalSettingsIncludeReplacer(LocalSettings localSettings, File loadDir)
  {
    this.localSettings = localSettings;
    this.loadDir = loadDir;
  }

  /**
   * {@inheritDoc}
   * 
   */

  @Override
  public Pair<String, String> replace(Pair<String, String> keyValue, Map<String, String> target)
  {
    if (keyValue.getKey().equals("include") == true) {
      File tf = new File(loadDir, keyValue.getSecond());
      this.localSettings.getLocalSettingsLoader().loadSettings(this.localSettings, tf, target, false, true);
      return null;
    }
    if (keyValue.getValue().contains("${LOCALSETTINGSDIR}") == true) {
      String value = StringUtils.replace(keyValue.getValue(), "${LOCALSETTINGSDIR}", loadDir.getAbsolutePath());
      keyValue.setValue(value);
    }
    return keyValue;
  }

}