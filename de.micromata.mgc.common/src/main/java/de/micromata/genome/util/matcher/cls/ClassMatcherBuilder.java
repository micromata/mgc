/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   04.07.2009
// Copyright Micromata 04.07.2009
//
/////////////////////////////////////////////////////////////////////////////
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
