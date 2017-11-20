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
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * Look if annotation has attribute.
 * 
 * @author roger@micromata.de
 * 
 */
public class AnnotationAttributeClassMatcher extends MatcherBase<Class<?>>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 7887535795705625292L;

  /**
   * The annotation class.
   */
  private Class<? extends Annotation> annotationClass;

  /**
   * The field name.
   */
  private String fieldName;

  /**
   * The getter method.
   */
  private Method getterMethod;

  /**
   * The field value.
   */
  private String fieldValue;

  /**
   * Instantiates a new annotation attribute class matcher.
   *
   * @param anotationClass the anotation class
   * @param fieldName the field name
   * @param fieldValue the field value
   */
  public AnnotationAttributeClassMatcher(Class<? extends Annotation> anotationClass, String fieldName,
      String fieldValue)
  {
    this.annotationClass = anotationClass;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    try {
      getterMethod = anotationClass.getMethod(fieldName, new Class<?>[] {});
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public String toString()
  {
    return "<Expr>.matchAttr(" + annotationClass.getCanonicalName() + ", " + fieldName + " = " + fieldValue + ")";
  }

  /**
   * Gets the value.
   *
   * @param anon the anon
   * @return the value
   */
  private Object getValue(Annotation anon)
  {
    Object value;
    try {
      value = getterMethod.invoke(anon, new Object[] {});
    } catch (Exception ex) {
      return null;
    }
    return value;
  }

  /**
   * Compare simple type.
   *
   * @param val the val
   * @return true, if successful
   */
  protected boolean compareSimpleType(Object val)
  {
    if (val instanceof Class<?>) {
      return StringUtils.equals(((Class<?>) val).getCanonicalName(), fieldValue);
    }
    final String sval = val != null ? val.toString() : null;
    return StringUtils.equals(sval, fieldValue);
  }

  /**
   * Contains value.
   *
   * @param array the array
   * @return true, if successful
   */
  protected boolean containsValue(Object array)
  {
    int len = Array.getLength(array);
    for (int i = 0; i < len; ++i) {
      Object val = Array.get(array, i);
      if (compareSimpleType(val) == true) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean match(Class<?> cls)
  {
    final Annotation anon = AnnotationClassMatcher.findAnnotation(cls, annotationClass);

    if (anon == null) {
      return false;
    }
    Object val = getValue(anon);
    if (val.getClass().isArray() == true) {
      return containsValue(val);
    }
    return compareSimpleType(val);
  }

  public Class<? extends Annotation> getAnnotationClass()
  {
    return annotationClass;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public String getFieldValue()
  {
    return fieldValue;
  }
}
