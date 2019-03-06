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

package de.micromata.genome.util.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import de.micromata.genome.util.matcher.BooleanListRulesFactory;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherBase;
import de.micromata.genome.util.matcher.MatcherFactory;

/**
 * Matches against Fields.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FieldMatchers
{

  /**
   * The name matcher factory.
   */
  public static MatcherFactory<String> nameMatcherFactory = new BooleanListRulesFactory<String>();

  /**
   * Matches against a field name.
   * 
   * @param pattern which will be parsed by BooleanListRulesFactory.
   * @return the matcher for the field
   */
  public static Matcher<Field> fieldName(String pattern)
  {
    final Matcher<String> matcher = nameMatcherFactory.createMatcher(pattern);
    return new MatcherBase<Field>()
    {

      @Override
      public boolean match(Field object)
      {
        return matcher.match(object.getName());
      }

    };
  }

  /**
   * Only matches, if field does not have Modifier
   * 
   * @param modifier the modifier count
   * @return the matcher for the field
   */
  public static Matcher<Field> hasNotModifier(final int modifier)
  {
    return new MatcherBase<Field>()
    {

      @Override
      public boolean match(Field object)
      {
        return (object.getModifiers() & modifier) == 0;
      }

    };
  }

  /**
   * Only matches, if field does have Modifier
   * 
   * @param modifier the modifier count
   * @return the matcher of the field
   */
  public static Matcher<Field> hasModifier(final int modifier)
  {
    return new MatcherBase<Field>()
    {

      @Override
      public boolean match(Field object)
      {
        return (object.getModifiers() & modifier) == modifier;
      }
    };
  }

  /**
   * Matches only, if field has excact given type.
   * 
   * @param type the class type
   * @return the matcher for the field
   */
  public static Matcher<Field> hasType(final Class<?> type)
  {
    return new MatcherBase<Field>()
    {

      @Override
      public boolean match(Field object)
      {
        return object.getType() == type;
      }
    };
  }

  /**
   * Matches only, if field has type, which is assignable to given type.
   * 
   * @param type the class type
   * @return the matcher for the field
   */
  public static Matcher<Field> assignableTo(final Class<?> type)
  {
    return new MatcherBase<Field>()
    {

      @Override
      public boolean match(Field object)
      {
        return type.isAssignableFrom(object.getType());
      }
    };
  }

  /**
   * Checks for annotation.
   *
   * @param anotClass the anot class
   * @return the matcher
   */
  public static Matcher<Field> hasAnnotation(Class<? extends Annotation> anotClass)
  {
    return new MatcherBase<Field>()
    {

      @Override
      public boolean match(Field object)
      {
        return object.isAnnotationPresent(anotClass);
      }
    };
  }
}
