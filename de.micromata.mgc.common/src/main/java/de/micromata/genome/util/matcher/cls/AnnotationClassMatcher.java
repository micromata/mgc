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

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * Matches if annotation exists on class.
 *
 * @author roger@micromata.de
 */
public class AnnotationClassMatcher extends MatcherBase<Class<?>>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1687444024529319011L;

  /**
   * The annotation class.
   */
  private Class<? extends Annotation> annotationClass;

  /**
   * Instantiates a new annotation class matcher.
   *
   * @param anotationClass the anotation class
   */
  public AnnotationClassMatcher(Class<? extends Annotation> anotationClass)
  {
    this.annotationClass = anotationClass;
  }

  /**
   * Find annotation.
   *
   * @param cls the cls
   * @param annotationClas the annotation clas
   * @return the annotation
   */
  public static Annotation findAnnotation(Class<?> cls, Class<? extends Annotation> annotationClas)
  {
    final Annotation anon = cls.getAnnotation(annotationClas);
    if (anon != null) {
      return anon;
    }
    Class<?> supercls = cls.getSuperclass();
    if (supercls == null || supercls == Object.class) {
      return null;
    }
    return findAnnotation(supercls, annotationClas);
  }

  @Override
  public boolean match(Class<?> cls)
  {
    final Annotation anon = findAnnotation(cls, annotationClass);

    return anon != null;
  }

  public Class<? extends Annotation> getAnnotationClass()
  {
    return annotationClass;
  }

  @Override
  public String toString()
  {
    return "<EXPR>.hasAnnotation(" + annotationClass.getCanonicalName() + ")";
  }

}
