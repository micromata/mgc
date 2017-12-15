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

package de.micromata.genome.chronos.spi.jdbc;

import java.io.StringReader;
import java.lang.reflect.Method;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Used to serialize Objects to XML to store trigger, etc.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SerializationUtil
{

  /**
   * The Constant xstream.
   */
  private static final XStream xstream;

  static {
    final DomDriver domDriver = new DomDriver();
    xstream = new XStream(domDriver);
    XStream.setupDefaultSecurity(xstream); // can be removed with XStream 1.5
    xstream.allowTypesByWildcard(new String[] {"de.micromata.genome.**"});
  }

  /**
   * Deserialize with x stream.
   *
   * @param <T> the generic type
   * @param definition the definition
   * @param clazz the clazz
   * @return the t
   */
  @SuppressWarnings("unchecked")
  public static <T> T deserializeWithXStream(String definition, final Class<T> clazz)
  {
    try {
      // final XStream xstream = new XStream(domDriver);
      final Object object = xstream.fromXML(new StringReader(definition));

      if (object == null) {
        return null;
      }
      if (clazz == null || clazz.isAssignableFrom(object.getClass())) {
        return (T) object;
      }

      throw new IllegalArgumentException("Can't deserialize object: " + definition + " is not of type " + clazz.getName());
    } catch (final Exception ex) {
      throw new IllegalArgumentException("Can't deserialize object: " + definition, ex);
    }
  }

  /**
   * The Constant createFromStringArgTypes.
   */
  private static final Class< ? >[] createFromStringArgTypes = new Class< ? >[] { String.class};

  /**
   * Deserialize with factory method.
   *
   * @param <T> the generic type
   * @param text the text
   * @param requiredClass the required class
   * @return the t
   */
  @SuppressWarnings("unchecked")
  public static <T> T deserializeWithFactoryMethod(String text, Class< ? > requiredClass)
  {
    int argOffset = text.indexOf(':');
    if (argOffset == -1) {
      // TODO (Rx) rrk warn
      return null;
    }
    String className = text.substring(0, argOffset);
    String argument = text.substring(argOffset + 1);
    try {
      Class< ? > cls = Class.forName(className);
      Method m = cls.getMethod("createFromString", createFromStringArgTypes);
      Object o = m.invoke(null, new Object[] { argument});
      if (requiredClass == null) {
        return (T) o;
      }
      if (requiredClass == null || requiredClass.isAssignableFrom(o.getClass())) {
        return (T) o;
      }
      throw new IllegalArgumentException("Can't deserialize object: " + text + " is not of type " + requiredClass.getName());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  /**
   * Deserialize.
   *
   * @param <T> the generic type
   * @param text the text
   * @param requiredClass the required class
   * @return the t
   */
  @SuppressWarnings("unchecked")
  public static <T> T deserialize(String text, Class<T> requiredClass)
  {
    if (text == null) {
      return null;
    }
    if (text.equals("<null/>") == true) {
      return null;
    }
    if (text.startsWith("<") == true) {
      return deserializeWithXStream(text, requiredClass);
    }
    Object ret = null;
    if (text.startsWith("\"") == true) {
      ret = text.substring(1);
    } else {
      ret = deserializeWithFactoryMethod(text, requiredClass);
    }
    return (T) ret;
  }

  /**
   * Serialize.
   *
   * @param obj the obj
   * @return the string
   */
  public static String serialize(final Object obj)
  {
    if (obj == null) {
      return "<null/>";
    }
    if (obj instanceof String) {
      return "\"" + (String) obj;
    }
    if (obj instanceof Stringifiable) {
      return ((Stringifiable) obj).asString();
    }

    try {
      // final XStream xstream = new XStream(new DomDriver());
      return xstream.toXML(obj);
    } catch (final Exception ex) {
      throw new IllegalArgumentException("Can't deserialize object: " + obj, ex);
    }
  }
}
