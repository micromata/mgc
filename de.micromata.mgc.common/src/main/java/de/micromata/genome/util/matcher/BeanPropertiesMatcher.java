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

package de.micromata.genome.util.matcher;

import org.apache.commons.beanutils.BeanUtils;

import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;

/**
 * Matches against a property expression.
 *
 * @author roger, lado
 */
public class BeanPropertiesMatcher extends MatcherBase<Object>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -4422921168954539863L;

  /**
   * property in the bean.
   */
  private String property;

  /**
   * must have value.
   */
  private String value;

  /**
   * The matcher.
   */
  private Matcher<String> matcher = null;

  /**
   * Instantiates a new bean properties matcher.
   */
  public BeanPropertiesMatcher()
  {

  }

  /**
   * Instantiates a new bean properties matcher.
   *
   * @param property the property
   * @param value the value
   */
  public BeanPropertiesMatcher(String property, String value)
  {
    this.property = property;
    this.value = value;

  }

  @Override
  public boolean match(Object object)
  {
    try {
      String value = null;
      try {
        value = BeanUtils.getNestedProperty(object, property);
      } catch (NoSuchMethodException nsme) {
        return false;
      }
      initMatcher();
      return matcher.match(value);
    } catch (Exception e) {

      /**
       * @logging
       * @reason Kann das Match-Regel nicht ausweten
       * @action Konfiguration überprüfen
       */
      throw new RuntimeException("Can not execute match rule: " + object + "." + property, e);
    }
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getProperty()
  {
    return property;
  }

  public void setProperty(String property)
  {
    this.property = property;
  }

  /**
   * Inits the matcher.
   */
  private void initMatcher()
  {
    if (matcher != null) {
      return;
    }
    SimpleWildcardMatcherFactory<String> simpleMatcherFactory = new SimpleWildcardMatcherFactory<String>();
    matcher = simpleMatcherFactory.createMatcher(value);
  }
}
