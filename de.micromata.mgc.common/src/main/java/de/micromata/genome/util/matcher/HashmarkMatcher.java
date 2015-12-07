/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    jens@micromata.de
// Created   31.12.2008
// Copyright Micromata 31.12.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher;

/**
 * matches nothing, but is used to identify inactive derivation of e.g. rights/roles that can be selected for
 * sub-matchers in a hierarchy. In that case the following mappong is used: MatchPositive -> match in sub-matcher if not
 * -'ed there MatchNegative or NoMatch -> match in sub-matcher only if +'ed there
 *
 * @author jens@micromata.de
 * @param <T> the generic type
 */
public class HashmarkMatcher<T>extends NoneMatcher<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5434145883323585312L;

  /**
   * The enclosed matcher.
   */
  protected Matcher<T> enclosedMatcher;

  /**
   * Instantiates a new hashmark matcher.
   *
   * @param enclosedMatcher the enclosed matcher
   */
  public HashmarkMatcher(Matcher<T> enclosedMatcher)
  {
    this.enclosedMatcher = enclosedMatcher;
  }

  public Matcher<T> getEnclosedMatcher()
  {
    return enclosedMatcher;
  }

  @Override
  public boolean match(T object)
  {
    return false;
  }
}
