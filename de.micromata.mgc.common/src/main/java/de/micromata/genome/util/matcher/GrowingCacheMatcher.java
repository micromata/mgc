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

import java.util.HashMap;
import java.util.Map;

/**
 * always growing, never shrinking, never invalidating => must eventually be recreated.
 *
 * @author jens@micromata.de
 * @param <T> the generic type
 */
public class GrowingCacheMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 2201308849362191628L;

  /**
   * The cache.
   */
  private Map<T, MatchResult> cache = new HashMap<T, MatchResult>();

  /**
   * The backend.
   */
  private Matcher<T> backend;

  /**
   * The no match value.
   */
  private Boolean noMatchValue;

  /**
   * Instantiates a new growing cache matcher.
   *
   * @param backend the backend
   * @param noMatchValue the no match value
   */
  public GrowingCacheMatcher(Matcher<T> backend, Boolean noMatchValue)
  {
    this.backend = backend;
    this.noMatchValue = noMatchValue;
  }

  @Override
  public MatchResult apply(T object)
  {
    MatchResult res = cache.get(object);
    if (res == null) {
      res = backend.apply(object);
      cache.put(object, res);
    }
    return res;
  }

  @Override
  public boolean match(T object)
  {
    MatchResult res = apply(object);
    if (res == MatchResult.MatchPositive) {
      return true;
    } else if (res == MatchResult.MatchNegative) {
      return false;
    }
    if (noMatchValue == null) {
      throw new IllegalStateException(MatchResult.MatchNegative.toString());
    }
    return noMatchValue;
  }

  /**
   * Wrap.
   *
   * @param <T> the generic type
   * @param backend the backend
   * @param noMatchValue the no match value
   * @return the matcher
   */
  public static <T> Matcher<T> wrap(Matcher<T> backend, Boolean noMatchValue)
  {
    return new GrowingCacheMatcher<T>(backend, noMatchValue);
  }
}
