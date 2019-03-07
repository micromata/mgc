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

import java.util.List;

/**
 * Cast one model to another.
 * 
 * This will used for composed configurations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface CastableLocalSettingsConfigModel extends LocalSettingsConfigModel
{
  /**
   * Get a sub node.
   * 
   * @param other the other class
   * @return the {@link LocalSettingsConfigModel} which was found
   */
  <T extends LocalSettingsConfigModel> T castTo(Class<T> other);

  /**
   * Cast to collection
   * @param other the other
   * @param <T> the type of List to retrun
   * @return the casted List
   */
  <T extends LocalSettingsConfigModel> List<T> castToCollect(Class<T> other);

  /**
   * Should a configuration dialog tag created
   * 
   * @param other the other
   * @return the result
   */
  default <T extends LocalSettingsConfigModel> T castToForConfigDialog(Class<T> other)
  {
    return castTo(other);
  }

  /**
   * Should a configuration dialog tag created
   * 
   * @param other the other
   * @return the list for the dialog
   */
  default <T extends LocalSettingsConfigModel> List<T> castToForConfigDialogCollect(Class<T> other)
  {
    return castToCollect(other);
  }
}
