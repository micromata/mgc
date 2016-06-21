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

package de.micromata.genome.db.jpa.tabattr.impl;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.db.jpa.tabattr.api.AttrDescription;
import de.micromata.genome.db.jpa.tabattr.api.AttrSchema;
import de.micromata.genome.db.jpa.tabattr.api.AttrSchemaService;
import de.micromata.genome.db.jpa.tabattr.api.EntityWithAttributes;
import de.micromata.genome.util.strings.converter.StandardStringConverter;
import de.micromata.genome.util.strings.converter.StringConverter;

/**
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AttrSchemaServiceBaseImpl implements AttrSchemaService
{
  private final StringConverter stringConverter = StandardStringConverter.get();

  /**
   * @see org.projectforge.framework.persistence.attr.impl.GuiAttrSchemaService#initializeAttrSet(org.projectforge.framework.persistence.attr.api.EntityWithAttributes)
   */
  @Override
  public void initializeAttrSet(final EntityWithAttributes attrMaster)
  {

    throw new UnsupportedOperationException();
  }

  protected <R> R getDefaultValue(final AttrDescription desc, final Class<R> expectedClass)
  {

    if (desc.getDefaultStringValue() == null) {
      return null;
    }
    if (desc.getDefaultValue() != null) {
      return (R) desc.getDefaultValue();
    }
    desc.setDefaultValue(stringConverter.cast(desc.getDefaultStringValue(), expectedClass));
    return (R) desc.getDefaultValue();
  }

  @Override
  public <R> R getDefaultValue(final String schemaName, final String propertyName, final Class<R> expectedClass)
  {
    final AttrSchema schema = getAttrSchema(schemaName);
    if (schema == null) {
      // TODO RK warn
    }
    for (final AttrDescription desc : schema.getColumns()) {
      if (StringUtils.equals(desc.getPropertyName(), propertyName) == true) {
        return getDefaultValue(desc, expectedClass);
      }
    }
    // TODO RK warn
    return null;
  }
}
