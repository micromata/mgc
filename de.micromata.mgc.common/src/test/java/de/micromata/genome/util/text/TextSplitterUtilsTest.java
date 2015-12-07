package de.micromata.genome.util.text;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.Assert;

/**
 * 
 * @author roger
 * 
 */
public class TextSplitterUtilsTest
{
  String join(List<String> l, String joiner)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < l.size(); ++i) {
      if (i > 0) {
        sb.append(joiner);
      }
      sb.append(l.get(i));
    }
    return sb.toString();
  }

  private void testSlitting(String text, String[] tokens, char escape, String espected)
  {
    testSlitting(text, tokens, escape, espected, true);
  }

  private void testSlitting(String text, String[] tokens, char escape, String espected, boolean returnToken)
  {
    testSlitting(text, tokens, escape, espected, returnToken, false);
  }

  private void testSlitting(String text, String[] tokens, char escape, String espected, boolean returnToken,
      boolean returnUnescaped)
  {
    List<String> ret = TextSplitterUtils.parseStringTokens(text, tokens, escape, returnToken, returnUnescaped);
    String erg = join(ret, "|");
    System.out.println(erg);
    Assert.assertEquals(espected, erg);
  }

  @Test
  public void testSplit1()
  {
    List<String> ret = TextSplitterUtils.parseStringTokens("a^:b:c", new char[] { ':' }, '^', true);
    String erg = join(ret, "|");
    System.out.println(erg);
    Assert.assertEquals("a:b|:|c", erg);
  }

  @Test
  public void testRegExp()
  {
    String p = "x";
    p = Pattern.quote(p);
    p = "^(" + p + ")(.*)";
    Matcher m = Pattern.compile(p).matcher("xsdf");
    if (m.matches() == true) {
      MatchResult mr = m.toMatchResult();
      System.out.println("matches: " + mr.group());
      for (int i = 0; i < mr.groupCount(); ++i) {
        System.out.println("g: " + mr.group(i));
      }
    } else {
      System.out.println("NOT matches");
    }
  }

  @Test
  public void testSplit2()
  {

    testSlitting("a\\:b:c", new String[] { ":" }, '\\', "a:b|:|c");

    testSlitting("a => b=c", new String[] { "=>", "=" }, '\\', "a |=>| b|=|c");
  }

  @Test
  public void testSplit3()
  {
    try {
      testSlitting("a\\:b:c\\", new String[] { ":" }, '\\', "a:b|c\\", false);
      Assert.fail("expect ex");
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework

    }
  }

  @Test
  public void testSplit4()
  {
    testSlitting("a\\:b:c", new String[] { ":" }, '\\', "a\\:b|c", false, true);
  }
}
