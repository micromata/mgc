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

import java.util.Set;

import de.micromata.genome.db.jpa.tabattr.impl.TabAttrCopier;
import de.micromata.genome.jpa.EntityCopy;

/**
 * A entity, which as attached attributes.
 * 
 * The getStringAttribute and putStringAttribute stores values as string.
 * 
 * getAttribute and putAttribute uses internally a StringConverter to convert a object from/to string.
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@EntityCopy(copier = { TabAttrCopier.class })
public interface EntityWithAttributes
{
  /**
   * get an entity.
   * 
   * @param key aka propertyName
   * @return if not set, returns null
   */
  String getStringAttribute(String key);

  /**
   * set a attribute.
   * 
   * @param key must not be null
   * @param value must not be null
   */
  void putStringAttribute(String key, String value);

  /**
   * Gets the attribute.
   * 
   * @param key the key
   * @return the attribute
   */
  Object getAttribute(String key);

  /**
   * Get an Attribute with expected class.
   *
   * @param <T> the generic type
   * @param key the key
   * @param expectedClass the expected class
   * @return the attribute
   * @throws IllegalArgumentException if value is not null and class does not match
   */
  <T> T getAttribute(String key, Class<T> expectedClass);

  /**
   * Put attribute.
   * 
   * @param key the key
   * @param value the value
   */
  void putAttribute(String key, Object value);

  /**
   * Remove the attribute.
   * 
   * @param key aka propertyName
   */
  void removeAttribute(String key);

  /**
   * The keys of the attributes.
   *
   * @return the attribute keys
   */
  Set<String> getAttributeKeys();
}
