package de.micromata.genome.util.strings.converter;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of the StandardStringConverter.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StandardStringConverterTest
{
  private void internalTestConvert(String s, Object obj)
  {
    StringConverter sc = StandardStringConverter.get();
    Assert.assertEquals(sc.cast(s, obj.getClass()), obj);
  }

  private void internalTestConvertArray(String s, Object[] obj)
  {
    StringConverter sc = StandardStringConverter.get();
    Assert.assertArrayEquals((Object[]) sc.cast(s, obj.getClass()), obj);
  }

  @Test
  public void testIt()
  {
    internalTestConvertArray("1,2", new Long[] { 1L, 2L});
    internalTestConvert("asdf", "asdf");
    internalTestConvert("42", new Integer(42));
    internalTestConvert("42.5", new Double(42.5));
    internalTestConvert("42.5", new BigDecimal("42.5"));
    internalTestConvertArray("a,b", new String[] { "a", "b"});

  }
}
