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
   * @param attrMaster the master of the attributes to analyze
   */
  void initializeAttrSet(EntityWithAttributes attrMaster);

  /**
   * Loads the meta description for the Entity.
   *
   * @param name the name of the schema
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

  /**
   * Gets the AttrGroup for given entity and groupName.
   * 
   * @param entity the entity
   * @param groupName the name of the group to find
   * @return null, if not found
   */
  AttrGroup getAttrGroup(EntityWithConfigurableAttr entity, String groupName);

  /**
   * Gets the AttrDescription for given entity, groupName and descriptionName.
   *
   * @param entity the entity
   * @param groupName the name of the group to find
   * @param descriptionName the description to find
   * @return null, if not found
   */
  AttrDescription getAttrDescription(EntityWithConfigurableAttr entity, String groupName, String descriptionName);

  /**
   * Gets the AttrDescription for given AttrGroup and descriptionName.
   *
   * @param attrGroup the attribute group
   * @param descriptionName the name of the description
   * @return null, if not found
   */
  AttrDescription getAttrDescription(AttrGroup attrGroup, String descriptionName);
}
