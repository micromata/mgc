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
 * matcher.match(object) returns true if matcher.object < arg
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class LessThanMatcher<T extends Comparable<T>>extends ComparatorMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5903327361420239096L;

  /**
   * Instantiates a new less than matcher.
   */
  public LessThanMatcher()
  {
    super();
  }

  /**
   * Instantiates a new less than matcher.
   *
   * @param other the other
   */
  public LessThanMatcher(T other)
  {
    super(other);
  }

  @Override
  public boolean match(T object)
  {
    if (other == null || other == null) {
      return false;
    }
    return other.compareTo(object) > 0;
  }

  @Override
  public String toString()
  {
    return "(" + other.toString() + " < EXPR)";
  }
}
