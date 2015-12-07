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
 * matcher.match(object) returns true if matcher.object >= arg
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class MoreThanOrEqualMatcher<T extends Comparable<T>>extends ComparatorMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 470081892409452114L;

  /**
   * Instantiates a new more than or equal matcher.
   */
  public MoreThanOrEqualMatcher()
  {
  }

  /**
   * Instantiates a new more than or equal matcher.
   *
   * @param other the other
   */
  public MoreThanOrEqualMatcher(T other)
  {
    super(other);
  }

  @Override
  public boolean match(T object)
  {
    if (other == null || other == null) {
      return false;
    }
    return other.compareTo(object) <= 0;
  }

  @Override
  public String toString()
  {
    return "(" + other.toString() + " >= EXPR)";
  }
}
