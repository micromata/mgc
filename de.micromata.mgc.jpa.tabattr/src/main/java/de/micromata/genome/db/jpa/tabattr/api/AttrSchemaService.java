/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2014 Kai Reinhard (k.reinhard@micromata.de)
//
// ProjectForge is dual-licensed.
//
// This community edition is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as published
// by the Free Software Foundation; version 3 of the License.
//
// This community edition is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, see http://www.gnu.org/licenses/.
//
/////////////////////////////////////////////////////////////////////////////

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
   * @param attrMaster the attr master
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
