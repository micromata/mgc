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

package de.micromata.genome.util.runtime.config;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

/**
 * Maps a localsettings to a model.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LocalSettingsConfigModel
{

  /**
   * Validate the model.
   *
   * @param ctx the ctx
   */
  public void validate(ValContext ctx);

  /**
   * When store into properties.
   * 
   * @return the comment of the section
   */
  String getSectionComment();

  /**
   * store the configuration into local settings.
   *
   * @param writer the writer
   * @return if created a new section writer, returned this.
   */
  LocalSettingsWriter toProperties(LocalSettingsWriter writer);

  /**
   * load the configuration from local settings.
   *
   * @param localSettings the local settings
   */
  void fromLocalSettings(LocalSettings localSettings);

  /**
   * Build a key from base key name.
   *
   * @param key the key
   * @return the string
   */
  String buildKey(String key);

  /**
   * get the commented giben by a @ALocalSettingsPath annotion.
   *
   * @param localProperty the local property
   * @return the string
   */
  String findCommentForProperty(String localProperty);

  /**
   * Initialize configuration.
   */
  default void initializeConfiguration()
  {

  }

}
