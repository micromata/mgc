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

package de.micromata.genome.db.jpa.tabattr.api;

/**
 * Interface to handle with Attrs.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface AttrSchemaService
{
  /**
   * Initialize an set of Attribute. Rules how to initialize attrs will be loaded by configurat.
   *
   * @param attrMaster
   */
  void initializeAttrSet(EntityWithAttributes attrMaster);

  /**
   * Loads the meta description for the Entity.
   *
   * @return the attr descriptions
   */
  AttrSchema getAttrSchema(String name);

  /**
   * Gets the default value.
   *
   * @param <R> the generic type
   * @param schemaName the schema name
   * @param propertyName the property name
   * @param expectedClass the expected class
   * @return the default value
   */
  <R> R getDefaultValue(String schemaName, String propertyName, Class<R> expectedClass);

}
