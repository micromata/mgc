package de.micromata.genome.util.matcher.collections;

import java.util.Set;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;

/**
 * Creates a matching against a set.
 * 
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class InSetMatcherFactory<T> implements MatcherFactory<Set<T>>
{

  /**
   * Instantiates a new in set matcher factory.
   */
  public InSetMatcherFactory()
  {

  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.MatcherFactory#createMatcher(java.lang.String)
   */
  public Matcher<Set<T>> createMatcher(String pattern)
  {
    return new InSetMatcher<T>(pattern);
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.MatcherFactory#getRuleString(de.micromata.genome.util.matcher.Matcher)
   */
  public String getRuleString(Matcher<Set<T>> matcher)
  {
    return matcher.toString();
  }

}
