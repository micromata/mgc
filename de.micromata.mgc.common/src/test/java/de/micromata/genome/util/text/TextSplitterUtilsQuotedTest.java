package de.micromata.genome.util.text;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author roger
 * @since 1.2.1
 */
public class TextSplitterUtilsQuotedTest
{
  @Test
  public void test10()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("'\\-a'", new String[] { ","}, false, '\'');
    Assert.assertEquals(1, ret.size());
    Assert.assertEquals("\\-a", ret.get(0));
  }

  @Test
  public void test9()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,b;'b,c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a", ret.get(0));
    Assert.assertEquals("b;b,c", ret.get(1));
  }

  @Test
  public void test8()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,b;b\\,c", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a", ret.get(0));
    Assert.assertEquals("b;b,c", ret.get(1));
  }

  @Test
  public void test7()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\\'n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a'n", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test6()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\\\\n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a\\n", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test5()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\\n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("an", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test4()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a\n", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test3()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test2()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,'b,c'", new String[] { ","}, true, '\'');
    Assert.assertEquals(3, ret.size());
    Assert.assertEquals("b,c", ret.get(2));
  }

  @Test
  public void testFirst()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,'b,c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("b,c", ret.get(1));
  }
}
