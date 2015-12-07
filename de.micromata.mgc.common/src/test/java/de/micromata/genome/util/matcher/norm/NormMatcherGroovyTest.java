package de.micromata.genome.util.matcher.norm;

import org.junit.Test;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherTestBase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class NormMatcherGroovyTest extends MatcherTestBase
{
  protected Matcher<String> createMatcher(String pattern)
  {
    return new NormBooleanMatcherFactory<String>().createMatcher(pattern);
  }

  @Test
  public void testQuotesinGroovy()
  {
    checkMatches("a or ${arg.contains('{')}", "b{");
  }

  @Test
  public void testGroovyNestedBrackets()
  {
    checkMatches("a or ${arg.grep { c -> c == 'b' }.size() == 2}", "abb");
  }

  @Test
  public void testGroovy()
  {
    checkMatches("a or ${arg.length() == 3}", "a");
    checkMatches("a or ${arg.length() == 3}", "abc");
    checkMatches("a or ${arg.length() == 3 or arg.length() == 2}", "bc");
    checkMatches("a or ${arg.length() == 3 or arg.length() == 2}", "cbc");
  }
}
