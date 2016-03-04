package de.micromata.genome.util.matcher;

import org.junit.Test;

/**
 * Tests groovy matching.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class GroovyMatcherTest extends MatcherTestBase
{
  public static class MyClassProvider
  {
    String matchAgainst;

    public MyClassProvider(String matchAgainst)
    {
      this.matchAgainst = matchAgainst;
    }

    public int myLength(String l)
    {
      return l.length();
    }

    public int thisLength()
    {
      return matchAgainst.length();
    }
  }

  @Override
  protected MatcherFactory<String> createMatcherFactory()
  {
    BooleanListRulesFactory<String> ret = new BooleanListRulesFactory<String>();
    ret.getTokenFactories().add(0, new GroovyMatcherFactory<String>(MyClassProvider.class));
    return ret;
  }

  @Test
  public void testGroovyWithFunction()
  {
    checkMatches("${myLength('a') == 1 || arg.length() == 3}", "a");
    checkMatches("${myLength('a') == 2 || arg.length() == 3}", "abc");
    checkMatches("${thisLength() == 3}", "abc");
    checkNotMatches("${thisLength() == 3}", "cabc");
  }

  @Test
  public void testGroovy()
  {
    //checkMatches("a || b", "a");
    checkMatches("a || ${arg.length() == 3}", "a");
    checkMatches("a || ${arg.length() == 3}", "abc");
    checkMatches("a || ${arg.length() == 3 or arg.length() == 2}", "bc");
    checkMatches("a || ${arg.length() == 3 or arg.length() == 2}", "cbc");
  }

}
