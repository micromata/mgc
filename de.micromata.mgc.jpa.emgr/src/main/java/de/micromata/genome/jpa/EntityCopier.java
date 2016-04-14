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

package de.micromata.genome.jpa;

/**
 * Copies an entity from orig to dest using given interface class as type template.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface EntityCopier
{

  /**
   * Copy to orig to dest using getter/setter.
   *
   * @param <T> the generic type
   * @param iface the iface
   * @param dest the dest
   * @param orig the orig
   */
  <T> EntityCopyStatus copyTo(IEmgr<?> emgr, Class<? extends T> iface, T dest, T orig, String... ignoreCopyFields);
}
