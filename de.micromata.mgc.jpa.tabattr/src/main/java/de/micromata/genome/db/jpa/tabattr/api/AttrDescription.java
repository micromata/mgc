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

import java.io.Serializable;

/**
 *
 * Describes one Attribute.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AttrDescription implements Serializable
{
  /**
   * technical name.
   */
  private String propertyName;

  /**
   * Type of the attribute.
   */
  private Class<?> type = String.class;
  /**
   * Maximum length in case of Strings.
   */
  private int maxLength = -1;

  /**
   * Used in Validation. Value must not be empty.
   */
  private boolean required = false;

  /**
   * I18N key used to communicate field to user.
   */
  private String i18nkey;
  /**
   * In case of value integer, min value.
   */
  private int minIntValue;

  /**
   * In case of Integer, max value.
   */
  private int maxIntValue;
  /**
   * Default Value in string rerpesentation.
   */
  private String defaultStringValue;
  /**
   * Internal parsed default value
   */
  private Object defaultValue;

  private String wicketComponentFactoryClass;

  /**
   * Bootstrap span for this column.
   */
  private   int span = 2;



  public String getWicketComponentFactoryClass()
  {
    return wicketComponentFactoryClass;
  }

  public void setWicketComponentFactoryClass(final String wicketComponentFactoryClass)
  {
    this.wicketComponentFactoryClass = wicketComponentFactoryClass;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(final String propertyName)
  {
    this.propertyName = propertyName;

  }

  public Class<?> getType()
  {
    return type;
  }

  public void setType(final Class<?> type)
  {
    this.type = type;
  }

  public int getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength(final int maxLength)
  {
    this.maxLength = maxLength;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired(final boolean required)
  {
    this.required = required;
  }

  public String getI18nkey()
  {
    return i18nkey;
  }

  public void setI18nkey(final String i18nkey)
  {
    this.i18nkey = i18nkey;
  }

  public int getMinIntValue()
  {
    return minIntValue;
  }

  public void setMinIntValue(final int minIntValue)
  {
    this.minIntValue = minIntValue;
  }

  public int getMaxIntValue()
  {
    return maxIntValue;
  }

  public void setMaxIntValue(final int maxIntValue)
  {
    this.maxIntValue = maxIntValue;
  }

  public Object getDefaultValue()
  {
    return defaultValue;
  }

  public void setDefaultValue(final Object defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public String getDefaultStringValue()
  {
    return defaultStringValue;
  }

  public void setDefaultStringValue(final String defaultStringValue)
  {
    this.defaultStringValue = defaultStringValue;
  }

  public int getSpan()
  {
    return span;
  }
  public void setSpan(final int span)
  {
    this.span = span;
  }
}
