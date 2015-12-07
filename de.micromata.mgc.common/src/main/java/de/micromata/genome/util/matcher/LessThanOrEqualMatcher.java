/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   14.11.2009
// Copyright Micromata 14.11.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher;

/**
 * matcher.match(object) returns true if matcher.object <= arg
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class LessThanOrEqualMatcher<T extends Comparable<T>>extends ComparatorMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -995461786747316882L;

  /**
   * Instantiates a new less than or equal matcher.
   */
  public LessThanOrEqualMatcher()
  {

  }

  /**
   * Instantiates a new less than or equal matcher.
   *
   * @param other the other
   */
  public LessThanOrEqualMatcher(T other)
  {
    super(other);
  }

  @Override
  public boolean match(T object)
  {
    if (other == null || other == null) {
      return false;
    }
    return other.compareTo(object) >= 0;
  }

  @Override
  public String toString()
  {
    return "(" + other.toString() + " <= EXPR)";
  }
}
