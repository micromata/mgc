package de.micromata.genome.util.matcher;

import org.junit.Assert;

/**
 * Test base for matcher tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class MatcherTestBase
{

  protected MatcherFactory<String> createMatcherFactory()
  {
    return new BooleanListRulesFactory<String>();
  }

  protected Matcher<String> createMatcher(String pattern)
  {
    return createMatcherFactory().createMatcher(pattern);
  }

  protected void checkMatches(String pattern, String input)
  {
    Matcher<String> matcher = createMatcher(pattern);
    boolean match = matcher.match(input);
    Assert.assertTrue("Matcher with pattern: " + pattern + " does not match to: " + input, match);
  }

  protected void checkNotMatches(String pattern, String input)
  {
    Matcher<String> matcher = createMatcher(pattern);
    Assert.assertTrue("Matcher with pattern: " + pattern + " does match to: " + input, matcher.match(input) == false);
  }
}
