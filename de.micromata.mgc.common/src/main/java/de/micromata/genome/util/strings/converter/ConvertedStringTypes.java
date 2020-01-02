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

package de.micromata.genome.util.strings.converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Java Type mapping.
 *
 * @author roger@micromata.de
 */
public enum ConvertedStringTypes
{

  /**
   * The null.
   */
  NULL('N', null),

  /**
   * The boolean.
   */
  BOOLEAN('Z', Boolean.class),

  /**
   * The byte.
   */
  BYTE('B', Byte.class),

  /**
   * The short.
   */
  SHORT('S', Short.class),

  /**
   * The integer.
   */
  INTEGER('I', Integer.class),

  /**
   * The long.
   */
  LONG('J', Long.class),

  /**
   * The float.
   */
  FLOAT('F', Float.class),

  /**
   * The double.
   */
  DOUBLE('D', Double.class),

  /**
   * The char.
   */
  CHAR('C', Character.class),

  /**
   * The string.
   */
  STRING('V', String.class),

  /**
   * The date.
   */
  DATE('P', Date.class),

  /**
   * The date.
   */
  LOCALDATE('R', LocalDate.class),


  /**
   * The bigdecimal.
   */
  BIGDECIMAL('K', BigDecimal.class),

  /**
   * The bytearray.
   */
  BYTEARRAY('b', new byte[] {}.getClass()),

  /**
   * The stringarray.
   */
  STRINGARRAY('s', new String[] {}.getClass()),

  /**
   * The longarray.
   */
  LONGARRAY('l', new Long[] {}.getClass()),

  // XMLOBJECT('X'),
  /**
   * Unmapped or custom, f.e. Bean
   */
  CUSTOM('Y', new Object[] {}.getClass());

  /**
   * The short type name.
   */
  private char shortTypeName;

  /**
   * The type name.
   */
  private String typeName;

  /**
   * The encode class.
   */
  private Class<?> encodeClass;

  /**
   * Instantiates a new converted string types.
   *
   * @param shortTypeName the short type name
   * @param encodeClass the encode class
   */
  private ConvertedStringTypes(char shortTypeName, Class<?> encodeClass)
  {
    this.shortTypeName = shortTypeName;
    this.encodeClass = encodeClass;
  }

  public String getTypeName()
  {
    if (typeName == null) {
      typeName = new String(new char[] { shortTypeName });
    }
    return typeName;
  }

  public char getShortType()
  {
    return shortTypeName;
  }

  /**
   * Gets the value type by short name.
   *
   * @param c the c
   * @return the value type by short name
   */
  public static ConvertedStringTypes getValueTypeByShortName(char c)
  {
    switch (c) {
      case 'N':
        return NULL;
      case 'Z':
        return BOOLEAN;
      case 'B':
        return BYTE;
      case 'S':
        return SHORT;
      case 'I':
        return INTEGER;
      case 'J':
        return LONG;
      case 'F':
        return FLOAT;
      case 'D':
        return DOUBLE;
      case 'C':
        return CHAR;
      case 'V':
        return STRING;
      case 'P':
      case 'Q':
        return DATE;
      case 'R':
        return LOCALDATE;
      case 'K':
        return BIGDECIMAL;
      case 'b':
        return BYTEARRAY;
      case 's':
        return STRINGARRAY;
      case 'l':
        return LONGARRAY;
      // case 'X':
      // return XMLOBJECT;
      case 'Y':
      default:
        return CUSTOM;
    }
  }

  public Class<?> getEncodeClass()
  {
    return encodeClass;
  }
}
