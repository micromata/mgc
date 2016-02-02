/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   11.07.2006
// Copyright Micromata 11.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.types;

/**
 * The Class StringObjectPair.
 *
 * @author roger@micromata.de
 * @see Pair
 */
public class StringObjectPair extends Pair<String, Object>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 8490052926203941134L;

  /**
   * Instantiates a new string object pair.
   */
  public StringObjectPair()
  {
    super();
  }

  /**
   * Instantiates a new string object pair.
   *
   * @param key the key
   * @param value the value
   */
  public StringObjectPair(String key, Object value)
  {
    super(key, value);
  }
}
