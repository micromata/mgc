package de.micromata.genome.util.matcher;

import org.junit.Test;

import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class FunctionMatcherTest extends MatcherTestBase
{
  protected Matcher<String> createMatcher(String pattern)
  {
    return new BooleanListRulesFactory<String>(new SimpleWildcardMatcherFactory<String>(), new FunctionMatcherFactory<String>(this))
        .createMatcher(pattern);
  }

  public boolean mod2lenth(String arg)
  {
    return (arg.length() % 2) == 0;
  }

  public boolean length(String arg, String length)
  {
    int il = Integer.parseInt(length);
    return arg.length() == il;
  }

  @Test
  public void testFunctionMatcher()
  {
    checkMatches("a || b", "a");
    checkMatches("mod2lenth()", "ab");
    checkNotMatches("mod2lenth()", "abc");
    checkNotMatches("length(4)", "abc");
    checkMatches("length(4)", "abcd");

  }
}
