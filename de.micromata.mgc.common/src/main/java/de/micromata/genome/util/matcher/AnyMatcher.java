/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    jensi@micromata.de
// Created   30.12.2008
// Copyright Micromata 30.12.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher;

/**
 * matches if at least one given matcher matches.
 *
 * @param <T> the generic type
 */
public class AnyMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 4055207023527099498L;

  /**
   * The matchers.
   */
  private Iterable<Matcher<? super T>> matchers;

  /**
   * Instantiates a new any matcher.
   *
   * @param matchers the matchers
   */
  public AnyMatcher(Iterable<Matcher<? super T>> matchers)
  {
    this.matchers = matchers;
  }

  @Override
  public boolean match(T object)
  {
    for (Matcher<? super T> m : matchers) {
      if (m.match(object) == true) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("anyof(");
    boolean isFirst = true;
    for (Matcher<? super T> m : matchers) {
      if (isFirst == false) {
        sb.append(", ");
      }
      isFirst = false;
      sb.append(m);
    }
    sb.append(")");
    return sb.toString();
  }

}
