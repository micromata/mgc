//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
