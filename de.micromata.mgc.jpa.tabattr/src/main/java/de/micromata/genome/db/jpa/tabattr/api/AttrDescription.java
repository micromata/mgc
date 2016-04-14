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
