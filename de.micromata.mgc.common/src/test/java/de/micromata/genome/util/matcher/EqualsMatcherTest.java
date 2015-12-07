package de.micromata.genome.util.matcher;

import junit.framework.TestCase;

public class EqualsMatcherTest extends TestCase
{
  public void testNulls()
  {
    assertTrue(new EqualsMatcher<String>(null).match(null));
    assertFalse(new EqualsMatcher<String>("").match(null));
    assertFalse(new EqualsMatcher<String>(null).match(""));
    assertTrue(new EqualsMatcher<String>("").match(""));
    assertFalse(new EqualsMatcher<String>("").match("z"));
  }

}
