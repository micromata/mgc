/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   28.11.2009
// Copyright Micromata 28.11.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher.string;

import org.apache.commons.lang.StringUtils;

/**
 * The Class ContainsIgnoreCaseMatcher.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class ContainsIgnoreCaseMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * Instantiates a new contains ignore case matcher.
   */
  public ContainsIgnoreCaseMatcher()
  {

  }

  /**
   * Instantiates a new contains ignore case matcher.
   *
   * @param pattern the pattern
   */
  public ContainsIgnoreCaseMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return StringUtils.containsIgnoreCase(token, pattern);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.containsIgnoreCase(" + pattern + ")";
  }
}
