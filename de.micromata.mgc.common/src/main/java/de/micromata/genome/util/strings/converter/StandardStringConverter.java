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

/*
 * 
 */
package de.micromata.genome.util.strings.converter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.text.StringCommaList;
import de.micromata.genome.util.types.Converter;
import de.micromata.genome.util.types.Pair;

/**
 * The Class StandardStringConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class StandardStringConverter implements StringConverter, Serializable
{

  /**
   * The instance.
   */
  private static StandardStringConverter INSTANCE = new StandardStringConverter();

  /**
   * The char to class map.
   */
  private static Map<Character, Class<?>> charToClassMap = new HashMap<Character, Class<?>>();

  /**
   * The class to char map.
   */
  private static Map<Class<?>, Character> classToCharMap = new HashMap<Class<?>, Character>();

  static {
    for (ConvertedStringTypes vs : ConvertedStringTypes.values()) {
      charToClassMap.put(vs.getShortType(), vs.getEncodeClass());
      classToCharMap.put(vs.getEncodeClass(), vs.getShortType());
    }
  }

  /**
   * Gets the.
   *
   * @return the standard string converter
   */
  public static StandardStringConverter get()
  {
    return INSTANCE;
  }

  @Override
  public char getTypeChar(Object value)
  {
    if (value == null) {
      return ConvertedStringTypes.NULL.getShortType();
    }
    Class<?> cls = value.getClass();

    if (value instanceof String) {
      return ConvertedStringTypes.STRING.getShortType();
    }

    Class<?> vclass = value.getClass();

    if (value instanceof Boolean || vclass == Boolean.TYPE) {
      return ConvertedStringTypes.BOOLEAN.getShortType();
    }
    if (value instanceof Byte || vclass == Byte.TYPE) {
      return ConvertedStringTypes.BYTE.getShortType();
    }
    if (value instanceof Short || vclass == Short.TYPE) {
      return ConvertedStringTypes.SHORT.getShortType();
    }
    if (value instanceof Integer || vclass == Integer.TYPE) {
      return ConvertedStringTypes.INTEGER.getShortType();
    }
    if (value instanceof Long || vclass == Long.TYPE) {
      return ConvertedStringTypes.LONG.getShortType();
    }
    if (value instanceof Float || vclass == Float.TYPE) {
      return ConvertedStringTypes.FLOAT.getShortType();
    }
    if (value instanceof Double || vclass == Double.TYPE) {
      return ConvertedStringTypes.DOUBLE.getShortType();
    }
    if (value instanceof Character || vclass == Character.TYPE) {
      return ConvertedStringTypes.CHAR.getShortType();
    }
    if (value instanceof Date) {
      return ConvertedStringTypes.DATE.getShortType();
    }
    if (value instanceof BigDecimal) {
      return ConvertedStringTypes.BIGDECIMAL.getShortType();
    }
    if (value instanceof Character) {
      return ConvertedStringTypes.CHAR.getShortType();
    }
    if (value instanceof byte[]) {
      return ConvertedStringTypes.BYTEARRAY.getShortType();
    }
    if (value instanceof String[]) {
      return ConvertedStringTypes.STRINGARRAY.getShortType();
    }
    if (value instanceof Long[]) {
      return ConvertedStringTypes.LONGARRAY.getShortType();
    }

    // if (value instanceof Serializable)
    // return ConvertedStringTypes.XMLOBJECT;
    return ConvertedStringTypes.CUSTOM.getShortType();
  }

  /**
   * Return true, if this type is not a bean, but a simple type.
   * 
   * @param o the object. may be null
   * @return true, if is simple type
   */
  @Override
  public boolean isSimpleType(Object o)
  {
    return getTypeChar(o) != ConvertedStringTypes.CUSTOM.getShortType();
  }

  @Override
  public Pair<Character, String> objectToString(Object value)
  {
    if (value == null) {
      return Pair.make(ConvertedStringTypes.NULL.getShortType(), null);
    }
    if (value instanceof String) {
      return Pair.make(ConvertedStringTypes.STRING.getShortType(), (String) value);
    }
    if (value instanceof Boolean) {
      return Pair.make(ConvertedStringTypes.BOOLEAN.getShortType(), Boolean.toString((Boolean) value));
    }
    if (value instanceof Byte) {
      return Pair.make(ConvertedStringTypes.BYTE.getShortType(), Byte.toString((Byte) value));
    }
    if (value instanceof Short) {
      return Pair.make(ConvertedStringTypes.SHORT.getShortType(), Short.toString((Short) value));
    }
    if (value instanceof Integer) {
      return Pair.make(ConvertedStringTypes.INTEGER.getShortType(), Integer.toString((Integer) value));
    }
    if (value instanceof Long) {
      return Pair.make(ConvertedStringTypes.LONG.getShortType(), Long.toString((Long) value));
    }
    if (value instanceof Float) {
      return Pair.make(ConvertedStringTypes.FLOAT.getShortType(), Float.toString((Float) value));
    }
    if (value instanceof Double) {
      return Pair.make(ConvertedStringTypes.DOUBLE.getShortType(), Double.toString((Double) value));
    }
    if (value instanceof Date) {
      return Pair.make(ConvertedStringTypes.DATE.getShortType(), formatDate((Date) value));
    }
    if (value instanceof BigDecimal) {
      return Pair.make(ConvertedStringTypes.BIGDECIMAL.getShortType(), formatBigDecimal((BigDecimal) value));
    }
    if (value instanceof Character) {
      return Pair.make(ConvertedStringTypes.CHAR.getShortType(), Character.toString((Character) value));
    }
    if (value instanceof byte[]) {
      return Pair.make(ConvertedStringTypes.BYTEARRAY.getShortType(), formatByteArray((byte[]) value));
    }
    if (value instanceof String[]) {
      return Pair.make(ConvertedStringTypes.STRINGARRAY.getShortType(), formatStringArray((String[]) value));
    }
    if (value instanceof Long[]) {
      return Pair.make(ConvertedStringTypes.LONGARRAY.getShortType(), formatLongArray((Long[]) value));
    }

    throw new IllegalArgumentException(
        "StringConverter; Cannot encode object to string: " + value.getClass().getName());
  }

  @Override
  public Object stringToObject(Character type, String s)
  {
    ConvertedStringTypes atype = ConvertedStringTypes.getValueTypeByShortName(type);
    switch (atype) {
      case STRING:
        return s;
      case NULL:
        return null;

      case BOOLEAN:
        return parseBoolean(s);
      case BIGDECIMAL:
        return parseBigDecimal(s);
      case BYTE:
        return parseByte(s);
      case CHAR:
        return parseCharacter(s);
      case SHORT:
        return parseShort(s);
      case INTEGER:
        return parseInteger(s);
      case LONG:
        return parseLong(s);
      case FLOAT:
        return parseFloat(s);
      case DOUBLE:
        return parseDouble(s);
      case DATE:
        return parseTimestamp(s);
      case BYTEARRAY:
        return parseByteArray(s);
      case STRINGARRAY:
        return parseStringArray(s);
      case LONGARRAY:
        return parseLongArray(s);
      // case XMLOBJECT:
      // return parseXmlObject(s);
      default:
        throw new IllegalArgumentException("StringConverter; Unsupported short type: " + type);
    }

  }

  @Override
  public <T> T cast(String s, Class<T> requested)
  {
    Character fc = classToCharMap.get(requested);
    if (fc == null) {
      throw new IllegalArgumentException("StringConverter; Unsupported class type: " + requested);
    }
    return (T) stringToObject(fc, s);
  }

  /**
   * Format long array.
   *
   * @param data the data
   * @return the string
   */
  protected String formatLongArray(Long[] data)
  {
    return StringCommaList.encodeLongArray(data);
  }

  /**
   * Parses the long array.
   *
   * @param data the data
   * @return the long[]
   */
  protected Long[] parseLongArray(String data)
  {
    return StringCommaList.decodeLongArray(data);
  }

  /**
   * Format string array.
   *
   * @param data the data
   * @return the string
   */
  protected String formatStringArray(String[] data)
  {
    return StringCommaList.encodeStringArray(data);
  }

  /**
   * Parses the string array.
   *
   * @param data the data
   * @return the string[]
   */
  protected String[] parseStringArray(String data)
  {
    return StringCommaList.decodeStringArray(data);
  }

  /**
   * Format byte array.
   *
   * @param data the data
   * @return the string
   */
  protected String formatByteArray(byte[] data)
  {
    return Base64.encodeBase64String(data);
  }

  /**
   * Parses the byte array.
   *
   * @param data the data
   * @return the byte[]
   */
  protected byte[] parseByteArray(String data)
  {
    return Base64.decodeBase64(data);
  }

  /**
   * Format big decimal.
   *
   * @param value the value
   * @return the string
   */
  protected String formatBigDecimal(BigDecimal value)
  {
    return value.toString();
  }

  /**
   * Format date.
   *
   * @param date the date
   * @return the string
   */
  protected String formatDate(Date date)
  {
    return Converter.formatByIsoTimestampFormat(date);
  }

  /**
   * Parses the big decimal.
   *
   * @param s the s
   * @return the big decimal
   */
  protected BigDecimal parseBigDecimal(String s)
  {
    if (StringUtils.isBlank(s) == true) {
      return null;
    }
    try {
      return new BigDecimal(s);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for LONG: " + s, ex);
    }
  }

  /**
   * Parses the boolean.
   *
   * @param s the s
   * @return the boolean
   */
  protected Boolean parseBoolean(String s)
  {
    return Boolean.parseBoolean(s);
  }

  /**
   * Parses the byte.
   *
   * @param s the s
   * @return the byte
   */
  protected Byte parseByte(String s)
  {
    if (StringUtils.isBlank(s) == true) {
      return null;
    }
    try {
      return Byte.parseByte(s);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for BYTE: " + s, ex);
    }
  }

  /**
   * Parses the character.
   *
   * @param s the s
   * @return the character
   */
  protected Character parseCharacter(String s)
  {
    if (StringUtils.isBlank(s) == true) {
      return null;
    }
    if (s.length() != 1) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for CHARACTER: " + s);
    }
    return s.charAt(0);
  }

  /**
   * Parses the timestamp.
   *
   * @param v the v
   * @return the date
   */
  protected Date parseTimestamp(String v)
  {
    if (StringUtils.isBlank(v) == true) {
      return null;
    }
    try {
      return Converter.parseByIsoTimestampFormat(v);
    } catch (ParseException ex) {
      try {
        return Converter.parseByIsoDateFormat(v);
      } catch (ParseException ex2) {
        throw new IllegalArgumentException("StringConverter; Unexpected value format for DATE: " + v, ex2);
      }
    }

  }

  /**
   * Parses the double.
   *
   * @param v the v
   * @return the double
   */
  protected Double parseDouble(String v)
  {
    if (StringUtils.isBlank(v) == true) {
      return null;
    }
    try {
      return Double.parseDouble(v);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for DOUBLE: " + v, ex);
    }
  }

  /**
   * Parses the float.
   *
   * @param v the v
   * @return the float
   */
  protected Float parseFloat(String v)
  {
    if (StringUtils.isBlank(v) == true) {
      return null;
    }
    try {
      return Float.parseFloat(v);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for FLOAT: " + v, ex);
    }
  }

  /**
   * Parses the integer.
   *
   * @param v the v
   * @return the integer
   */
  protected Integer parseInteger(String v)
  {
    if (StringUtils.isBlank(v) == true) {
      return null;
    }
    try {
      Integer ret = Integer.parseInt(v);
      return ret;
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for INTEGER: " + v, ex);
    }
  }

  /**
   * Parses the long.
   *
   * @param v the v
   * @return the long
   */
  protected Long parseLong(String v)
  {
    if (StringUtils.isBlank(v) == true) {
      return null;
    }
    try {
      return Long.parseLong(v);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for LONG: " + v, ex);
    }
  }

  /**
   * Parses the short.
   *
   * @param v the v
   * @return the short
   */
  protected Short parseShort(String v)
  {
    if (StringUtils.isBlank(v) == true) {
      return null;
    }
    try {
      return Short.parseShort(v);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("StringConverter; Unexpected value format for SHORT: " + v, ex);
    }
  }

  @Override
  public String asString(Object s)
  {
    return objectToString(s).getSecond();
  }

}
