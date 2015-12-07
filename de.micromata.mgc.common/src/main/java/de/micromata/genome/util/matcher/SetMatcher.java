/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   31.12.2008
// Copyright Micromata 31.12.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher;

import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Matches agains a set via contains.
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class SetMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -870906355711283761L;

  /**
   * The set.
   */
  private Set<T> set;

  /**
   * Instantiates a new sets the matcher.
   *
   * @param set the set
   */
  public SetMatcher(Set<T> set)
  {
    Validate.notNull(set);
    this.set = set;
  }

  @Override
  public boolean match(T object)
  {
    return set.contains(object);
  }

  @Override
  public String toString()
  {
    return "inSet(" + set + ")";
  }

}
