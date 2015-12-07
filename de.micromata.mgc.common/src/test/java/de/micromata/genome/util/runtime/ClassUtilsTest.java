package de.micromata.genome.util.runtime;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class ClassUtilsTest.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ClassUtilsTest
{

  /**
   * Test get concret generic type.
   */
  @Test
  public void testGetConcretGenericType()
  {
    Class<? extends Number> result = ClassUtils.getGenericTypeArgument(TestConcreteClass.class, Number.class);
    Assert.assertEquals(Integer.class, result);
  }

  /**
   * Test get concret derived generic type.
   */
  @Test
  public void testGetConcretDerivedGenericType()
  {
    Class<? extends Number> result = ClassUtils.getGenericTypeArgument(TestDerivedConcreteClass.class, Number.class);
    Assert.assertEquals(Integer.class, result);
  }

  @Test
  public void testAnots()
  {
    TestAnot[] ar = TestDerivedConcreteClass.class.getAnnotationsByType(TestAnot.class);
    List<TestAnot> rf = ClassUtils.findClassAnnotations(TestDerivedConcreteClass.class, TestAnot.class);
    Assert.assertEquals(4, rf.size());

  }
}
