package de.micromata.genome.util.matcher;

import java.util.Collection;

/**
 * Base class of Matcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public abstract class MatcherBase<T>implements Matcher<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 7157263544470217750L;

  @Override
  public MatchResult apply(T token)
  {
    return match(token) ? MatchResult.MatchPositive : MatchResult.NoMatch;
  }

  @Override
  public boolean matchAll(Collection<T> sl, boolean defaultValue)
  {
    boolean matches = defaultValue;
    for (T token : sl) {
      MatchResult mr = apply(token);
      if (mr == MatchResult.NoMatch) {
        return false;
      }
      if (mr == MatchResult.MatchPositive) {
        matches = true;
      }
    }
    return matches;
  }

  @Override
  public boolean matchAny(Collection<T> sl, boolean defaultValue)
  {
    boolean matches = defaultValue;
    for (T token : sl) {
      MatchResult mr = apply(token);
      if (mr == MatchResult.MatchPositive) {
        return true;
      }
    }
    return matches;
  }
}
