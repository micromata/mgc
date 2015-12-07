/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   09.07.2006
// Copyright Micromata 09.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.types;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

/**
 * Similar to c++ type pair.
 *
 * @author roger@micromata.de
 * @param <K> the key type
 * @param <V> the value type
 */
public class Pair<K, V> implements Map.Entry<K, V>, Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1427196812388547552L;

  /**
   * The key.
   */
  private K key;

  /**
   * The value.
   */
  private V value;

  /**
   * Make.
   *
   * @param <MK> the generic type
   * @param <MV> the generic type
   * @param key the key
   * @param value the value
   * @return the pair
   */
  public static <MK, MV> Pair<MK, MV> make(MK key, MV value)
  {
    return new Pair<MK, MV>(key, value);
  }

  /**
   * Builds null-save clone.
   *
   * @param <MK> the generic type
   * @param <MV> the generic type
   * @param source the source
   * @return the pair
   */
  public static <MK, MV> Pair<MK, MV> make(Pair<MK, MV> source)
  {
    if (source == null) {
      return null;
    }
    return new Pair<MK, MV>(source.getKey(), source.getValue());
  }

  /**
   * Instantiates a new pair.
   */
  public Pair()
  {
  }

  /**
   * Instantiates a new pair.
   *
   * @param key the key
   * @param value the value
   */
  public Pair(K key, V value)
  {
    this.key = key;
    this.value = value;
  }

  @Override
  public K getKey()
  {
    return key;
  }

  public void setKey(K key)
  {
    this.key = key;
  }

  @Override
  public V getValue()
  {
    return value;
  }

  @Override
  public V setValue(V value)
  {
    V t = this.value;
    this.value = value;
    return t;
  }

  public K getFirst()
  {
    return key;
  }

  public V getSecond()
  {
    return value;
  }

  public void setFirst(K k)
  {
    key = k;
  }

  public void setSecond(V v)
  {
    value = v;
  }

  @Override
  public String toString()
  {
    return key + ": " + value;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Pair) {
      Pair other = (Pair) obj;
      return ObjectUtils.equals(key, other.key) && ObjectUtils.equals(value, other.value);

    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return ObjectUtils.hashCode(key) * 31 + ObjectUtils.hashCode(value);
  }
}
