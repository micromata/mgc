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

package de.micromata.genome.util.matcher.cls;

import java.lang.annotation.Annotation;

import de.micromata.genome.util.matcher.Matcher;

/**
 * Builder factory for class matcher.
 * 
 * @author roger@micromata.de
 * 
 */
public class ClassMatcherBuilder
{

  /**
   * Class matches.
   *
   * @param matcher the matcher
   * @return the matcher
   */
  public static Matcher<Class<?>> classMatches(Matcher<String> matcher)
  {
    return new ClassNameMatcher(matcher);
  }

  /**
   * Class extends.
   *
   * @param cls the cls
   * @return the matcher
   */
  public static Matcher<Class<?>> classExtends(Class<?> cls)
  {
    return new ExtendsClassMatcher(cls);
  }

  /**
   * Class has annotation.
   *
   * @param cls the cls
   * @return the matcher
   */
  public static Matcher<Class<?>> classHasAnnotation(Class<? extends Annotation> cls)
  {
    return new AnnotationClassMatcher(cls);
  }

  /**
   * Class annotation has value.
   *
   * @param cls the cls
   * @param name the name
   * @param value the value
   * @return the matcher
   */
  public static Matcher<Class<?>> classAnnotationHasValue(Class<? extends Annotation> cls, String name, String value)
  {
    return new AnnotationAttributeClassMatcher(cls, name, value);
  }

}
