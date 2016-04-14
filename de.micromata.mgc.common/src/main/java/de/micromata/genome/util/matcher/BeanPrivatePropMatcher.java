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

import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * A matcher, which checks if on a given bean the Field is matching.
 * 
 * It used PrivateBeanUtils to read the field.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <BEAN> the generic type
 * @param <FIELDTYPE> the generic type
 */
public class BeanPrivatePropMatcher<BEAN, FIELDTYPE>extends MatcherBase<BEAN>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5031870129869115761L;

  /**
   * The field matcher.
   */
  private final Matcher<FIELDTYPE> fieldMatcher;

  /**
   * The field name.
   */
  private final String fieldName;

  /**
   * Instantiates a new bean private prop matcher.
   *
   * @param fieldName the field name
   * @param fieldMatcher the field matcher
   */
  public BeanPrivatePropMatcher(String fieldName, Matcher<FIELDTYPE> fieldMatcher)
  {
    this.fieldMatcher = fieldMatcher;
    this.fieldName = fieldName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean match(BEAN object)
  {
    FIELDTYPE value = (FIELDTYPE) PrivateBeanUtils.readField(object, fieldName);
    return fieldMatcher.match(value);
  }

  @Override
  public String toString()
  {
    return "matchProp(" + fieldName + ": " + fieldMatcher.toString() + ")";
  }
}
