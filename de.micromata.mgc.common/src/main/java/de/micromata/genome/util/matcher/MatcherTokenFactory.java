package de.micromata.genome.util.matcher;

import de.micromata.genome.util.matcher.BooleanListRulesFactory.TokenResultList;

/**
 * Creates a Matcher consuming tokens.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface MatcherTokenFactory<T>
{
  /**
   * Reading tokens and crate a matcher.
   * 
   * The curToken should point after consuming behind last consumed token.
   * 
   * @param tokens
   * @return null, if not be consumed.
   */
  public Matcher<T> createMatcher(TokenResultList tokens);
}
