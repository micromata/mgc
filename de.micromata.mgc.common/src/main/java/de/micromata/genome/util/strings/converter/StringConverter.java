/*
 * 
 */
package de.micromata.genome.util.strings.converter;

import de.micromata.genome.util.types.Pair;

/**
 * Interface which converts from/to String.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface StringConverter
{
  /**
   * Return true, if this type is not a bean, but a simple type.
   * 
   * @param o the object. may be null
   * @return true, if is simple type
   */
  public boolean isSimpleType(Object o);

  /**
   * Gets the type char.
   * 
   * 'Y' is custom, and may cannot be convertetd.
   * 
   * @param value the value
   * @return the type char
   */
  public char getTypeChar(Object value);

  /**
   * Convert to string.
   * 
   * @param o the bean, can be null
   * @return the pair first is stringified version, second is type identifier.
   * @throws IllegalArgumentException if object cannot be converted.
   */
  public Pair<Character, String> objectToString(Object o);

  /**
   * Convert object to string.
   * 
   * Calls internall objectToString.
   *
   * @param s the s
   * @return the string
   */
  public String asString(Object s);

  /**
   * Cast string to given type.
   *
   * @param <T> the generic type
   * @param s the s
   * @param requested the requested
   * @return casted type.
   * @throws IllegalArgumentException if Class is not supported
   */
  public <T> T cast(String s, Class<T> requested);

  /**
   * Return true, if this type is not a bean, but a simple type.
   *
   * @param type the type
   * @param sval the sval
   * @return true, if is simple type
   * @throws IllegalArgumentException if object cannot be converted.
   */
  public Object stringToObject(Character type, String sval);
}
