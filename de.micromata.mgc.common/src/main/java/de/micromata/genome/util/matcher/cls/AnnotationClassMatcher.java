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
