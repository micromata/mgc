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

package de.micromata.genome.util.strings;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class ReducedReflectionToStringBuilder.
 *
 * @author roger@micromata.de
 */
public class ReducedReflectionToStringBuilder extends ReflectionToStringBuilder
{

  /**
   * Instantiates a new reduced reflection to string builder.
   *
   * @param object the object
   */
  public ReducedReflectionToStringBuilder(Object object)
  {
    super(object);
  }

  /**
   * Instantiates a new reduced reflection to string builder.
   *
   * @param object the object
   * @param style the style
   * @param buffer the buffer
   * @param reflectUpToClass the reflect up to class
   * @param outputTransients the output transients
   * @param outputStatics the output statics
   */
  public ReducedReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer,
      Class<?> reflectUpToClass,
      boolean outputTransients, boolean outputStatics)
  {
    super(object, style, buffer, reflectUpToClass, outputTransients, outputStatics);
  }

  /**
   * Instantiates a new reduced reflection to string builder.
   *
   * @param object the object
   * @param style the style
   * @param buffer the buffer
   */
  public ReducedReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer)
  {
    super(object, style, buffer);
  }

  /**
   * Instantiates a new reduced reflection to string builder.
   *
   * @param object the object
   * @param style the style
   */
  public ReducedReflectionToStringBuilder(Object object, ToStringStyle style)
  {
    super(object, style);
  }

  /**
   * Reflection to string.
   *
   * @param object the object
   * @param style the style
   * @param outputTransients the output transients
   * @param reflectUpToClass the reflect up to class
   * @return the string
   */
  @SuppressWarnings("rawtypes")
  public static String reflectionToString(Object object, ToStringStyle style, boolean outputTransients,
      Class reflectUpToClass)
  {
    return ReducedReflectionToStringBuilder.toString(object, style, outputTransients, false, reflectUpToClass);
  }

  /**
   * To string.
   *
   * @param object the object
   * @param style the style
   * @param outputTransients the output transients
   * @param outputStatics the output statics
   * @param reflectUpToClass the reflect up to class
   * @return the string
   */
  @SuppressWarnings("rawtypes")
  public static String toString(Object object, ToStringStyle style, boolean outputTransients, boolean outputStatics,
      Class reflectUpToClass)
  {
    return new ReducedReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics)
        .toString();
  }

  /**
   * The Class Registry.
   */
  public static class Registry
  {

    /**
     * The level.
     */
    public int level = 0;

    /**
     * The visited.
     */
    public Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
  }

  /**
   * The registry.
   */
  private static ThreadLocal<Registry> registry = new ThreadLocal<Registry>()
  {
    @Override
    protected synchronized Registry initialValue()
    {
      return new Registry();
    }
  };

  /**
   * Push registry.
   */
  public static void pushRegistry()
  {
    Registry r = registry.get();
    ++r.level;
  }

  /**
   * Pop registry.
   */
  public static void popRegistry()
  {
    Registry r = registry.get();
    if (--r.level == 0) {
      r.visited.clear();
    }
  }

  static public Map<Object, Object> getRegistry()
  {
    return registry.get().visited;
  }

  static public Registry getRegistryObject()
  {
    return registry.get();
  }

  /**
   * Checks if is object registered.
   *
   * @param value the value
   * @return true, if is object registered
   */
  static public boolean isObjectRegistered(Object value)
  {
    return getRegistry().containsKey(value);
  }

  /**
   * Register object.
   *
   * @param value the value
   */
  static public void registerObject(Object value)
  {
    getRegistry().put(value, null);
  }

  /**
   * Super to string.
   *
   * @return the string
   */
  public String superToString()
  {
    if (this.getObject() == null) {
      this.getStringBuffer().append("<null>");
    } else {
      getStyle().appendEnd(this.getStringBuffer(), this.getObject());
    }
    return this.getStringBuffer().toString();
  }

  @Override
  public String toString()
  {
    if (this.getObject() == null) {
      return super.toString();
    }

    if (isObjectRegistered(this.getObject()) == true) {
      // The object has already been appended, therefore we have an
      // object cycle.
      // Append a simple Object.toString style string. The field name is
      // already appended at this point.
      this.appendAsObjectToString(this.getObject());
      // return superToString();
      return getStringBuffer().toString();
      //
    }
    String ret = "";
    pushRegistry();
    try {
      Class<?> clazz = this.getObject().getClass();
      if (this.appendFieldsInInternal(clazz) == true) {
        while (clazz.getSuperclass() != null && clazz != this.getUpToClass()) {
          clazz = clazz.getSuperclass();
          this.appendFieldsInInternal(clazz);
        }
      }
      // getStyle().appendEnd(this.getStringBuffer(), this.getObject());
      ret = superToString();
      // ret = getStringBuffer().toString();
    } finally {
      popRegistry();
    }
    return ret;
  }

  @Override
  public ToStringBuilder appendAsObjectToString(Object object)
  {
    this.getStringBuffer().append(object.getClass().getSimpleName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(object)));
    if (object instanceof ShortDisplayable) {
      this.getStringBuffer().append("[").append(((ShortDisplayable) object).toShortString()).append("]");
    }
    return this;
  }

  @Override
  protected boolean accept(Field field)
  {
    // if (field.getName().indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR) != -1) {
    // // Reject field from inner class.
    // return false;
    // }
    if (field.getAnnotation(NoStringifyAnnotation.class) != null) {
      return false;
    }
    if (Modifier.isTransient(field.getModifiers()) && !this.isAppendTransients()) {
      // transients.
      return false;
    }
    if (Modifier.isStatic(field.getModifiers()) && !this.isAppendStatics()) {
      // transients.
      return false;
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void appendFieldsIn(Class clazz)
  {
    appendFieldsInInternal(clazz);
  }

  /**
   * Append fields in internal.
   *
   * @param clazz the clazz
   * @return true, if successful
   */
  protected boolean appendFieldsInInternal(Class<?> clazz)
  {

    registerObject(getObject());
    if (clazz.isArray()) {
      this.reflectionAppendArray(this.getObject());
      return false;
    }
    Field[] fields = clazz.getDeclaredFields();
    AccessibleObject.setAccessible(fields, true);
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      String fieldName = field.getName();
      if (this.accept(field) == false) {
        continue;
      }

      try {
        // Warning: Field.get(Object) creates wrappers objects
        // for primitive types.
        Object fieldValue = this.getValue(field);
        if (isObjectRegistered(fieldValue) == true && field.getType().isPrimitive() == false) {
          this.getStringBuffer().append(fieldName).append("=");
          this.appendAsObjectToString(fieldValue);
          this.getStringBuffer().append(",");

        } else {
          this.append(fieldName, fieldValue);
        }
      } catch (IllegalAccessException ex) {
        // this can't happen. Would get a Security exception
        // instead
        // throw a runtime exception in case the impossible
        // happens.
        throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
      }
    }
    return true;
  }

}
