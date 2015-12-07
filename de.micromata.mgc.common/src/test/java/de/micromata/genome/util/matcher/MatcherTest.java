package de.micromata.genome.util.matcher;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;

public class MatcherTest
{
  // @Test
  public void testGroovy()
  {
    SimpleWildcardMatcherFactory<String> mf = new SimpleWildcardMatcherFactory<String>();
    Matcher<String> m = mf.createMatcher("${return arg.equalsIgnoreCase('TEST')}");
    Assert.assertTrue(m.match("test"));
    m = mf.createMatcher("${return arg.equalsIgnoreCase('aTEST')}");
    Assert.assertFalse(m.match("test"));
  }

  @Test
  public void testEscaping()
  {
    String pattern = "\\*a";
    SimpleWildcardMatcherFactory<String> mf = new SimpleWildcardMatcherFactory<String>();
    Matcher<String> m = mf.createMatcher(pattern);
    Assert.assertTrue(m.match("*a"));
    String rp = mf.getRuleString(m);
    m = mf.createMatcher("*a");
    Assert.assertTrue(m.match("*a"));
  }
}
