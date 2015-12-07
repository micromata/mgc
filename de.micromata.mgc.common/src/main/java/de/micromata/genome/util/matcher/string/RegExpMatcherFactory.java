package de.micromata.genome.util.matcher.string;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;

/**
 * Create a RegExpMatcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public class RegExpMatcherFactory<T>implements MatcherFactory<T>
{

  @Override
  public Matcher<T> createMatcher(String pattern)
  {
    return new RegExpMatcher<T>(pattern);
  }

  @Override
  public String getRuleString(Matcher<T> matcher)
  {
    if (matcher instanceof RegExpMatcher) {
      ((RegExpMatcher<T>) matcher).getPattern().toString();
    }
    return "<unknown>";
  }

}
