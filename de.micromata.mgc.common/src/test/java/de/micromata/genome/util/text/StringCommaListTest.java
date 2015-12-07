package de.micromata.genome.util.text;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the StringCommaList.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StringCommaListTest
{

  @Ignore
  private void checkEquals(String[] array)
  {
    String s = StringCommaList.encodeStringArray(array);
    String[] ret = StringCommaList.decodeStringArray(s);
    Assert.assertArrayEquals(array, ret);
  }

  @Ignore
  private void checkEquals(Long[] array)
  {
    String s = StringCommaList.encodeLongArray(array);
    Long[] ret = StringCommaList.decodeLongArray(s);
    Assert.assertArrayEquals(array, ret);
  }

  @Test
  public void testStringCommaList()
  {
    checkEquals(new String[] { "a,b", "", "", "sadf"});
    checkEquals(new String[] { "a\\b", "", "", "sadf"});
    checkEquals(new String[] { "a\\,b", "", "", "sadf"});
    checkEquals(new String[] {});
    checkEquals(new String[] { "asdf"});
    checkEquals(new String[] { "asdf", "", "", "sadf"});
  }

  @Test
  public void testLongCommaList()
  {
    checkEquals(new Long[] {});
    checkEquals(new Long[] { 14L});
    checkEquals(new Long[] { 14L, 0L});
  }
}
