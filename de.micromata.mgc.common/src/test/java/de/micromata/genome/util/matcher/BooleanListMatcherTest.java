package de.micromata.genome.util.matcher;

import org.junit.Assert;
import org.junit.Test;

public class BooleanListMatcherTest
{
  @Test
  public void testReqExprWithPlus()
  {
    String pattern = "a, ~[Xy].\\+";
    BooleanListRulesFactory<String> blf = new BooleanListRulesFactory<String>();
    Matcher<String> m = blf.createMatcher(pattern);
    m.match("a");
  }

  private void testParse(String pattern, String[] matching, String[] notmatching)
  {
    BooleanListRulesFactory<String> blf = new BooleanListRulesFactory<String>();
    Matcher<String> m = blf.createMatcher(pattern);
    String dc = blf.getRuleString(m);
    System.out.println("p: " + pattern + "; decp: " + dc);
    for (String t : matching) {
      boolean match = m.match(t);
      Assert.assertTrue("Should match. pattern:" + pattern + "; test: " + t, match);
    }
    for (String t : notmatching) {
      boolean match = m.match(t);
      Assert.assertFalse("Should NOT match. pattern:" + pattern + "; test: " + t, match);
    }
  }

  private static String[] sa(String... strings)
  {
    return strings;
  }

  @Test
  public void testParse()
  {
    testParse("(*a && b*)", sa("ba"), sa("c", "ab"));
    testParse("a,b", sa("a", "b"), sa("c", "ab"));
    testParse("a || b", sa("a", "b"), sa("c", "ab"));

    testParse("((a) || (b))", sa("a", "b"), sa("c", "ab"));

    testParse("*a && !b*", sa("xa", "asdfa"), sa("ba", "xx", "bx"));

    testParse("(*a && b*) || (*c && d*)", sa("ba", "dxc"), sa("xx", "xx", "bx"));
  }

  protected void testFailure(String pattern)
  {
    try {
      testParse(pattern, sa(), sa());
      Assert.fail("Expect exception");
    } catch (InvalidMatcherGrammar ex) {
      Assert.assertTrue("Expected ex: " + ex.getMessage(), true);
    }
  }

  @Test
  public void testFailures()
  {
    testFailure("a(");
    testFailure("a &&");
    testFailure(")a");
  }

  @Test
  public void testEscaping()
  {
    testParse("\\,ab", sa(",ab"), sa("ab"));
  }
}
