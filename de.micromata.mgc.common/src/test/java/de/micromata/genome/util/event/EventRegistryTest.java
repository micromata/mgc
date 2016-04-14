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

package de.micromata.genome.util.event;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EventRegistryTest
{
  @Test
  public void testClassRegistry()
  {
    TestListenerOne.callCount = 0;
    TestListenerOneDerived.callCount = 0;
    TestListenerTwo.callCount = 0;
    MgcEventClassRegistry registry = new SimpleEventClassRegistry("test");
    registry.registerListener(TestListenerOne.class);
    registry.registerListener(TestListenerTwo.class);
    registry.registerListener(TestListenerOneDerived.class);
    TestEventTwo tw1 = new TestEventTwo("Test");
    registry.dispatchEvent(tw1);
    Assert.assertEquals(0, TestListenerOne.callCount);
    Assert.assertEquals(0, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);

    TestEventOneDerived nd = new TestEventOneDerived("Test2");
    registry.dispatchEvent(nd);
    Assert.assertEquals(1, TestListenerOne.callCount);
    Assert.assertEquals(1, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);

    TestEventOne one = new TestEventOne("Test2");
    registry.dispatchEvent(one);
    Assert.assertEquals(2, TestListenerOne.callCount);
    Assert.assertEquals(1, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);

    registry.removeListener(TestListenerOneDerived.class);

    nd = new TestEventOneDerived("Test2");
    registry.dispatchEvent(nd);
    Assert.assertEquals(3, TestListenerOne.callCount);
    Assert.assertEquals(1, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);

  }

  @Test
  public void testInstanceRegistry()
  {
    TestListenerOne.callCount = 0;
    TestListenerOneDerived.callCount = 0;
    TestListenerTwo.callCount = 0;
    SimpleEventInstanceRegistry registry = new SimpleEventInstanceRegistry("testinstance");
    registry.registerListener(new TestListenerOne());
    registry.registerListener(new TestListenerTwo());
    registry.registerListener(new TestListenerOneDerived());
    TestEventTwo tw1 = new TestEventTwo("Test");
    registry.dispatchEvent(tw1);
    Assert.assertEquals(0, TestListenerOne.callCount);
    Assert.assertEquals(0, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);

    TestEventOneDerived nd = new TestEventOneDerived("Test2");
    registry.dispatchEvent(nd);
    Assert.assertEquals(1, TestListenerOne.callCount);
    Assert.assertEquals(1, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);

    TestEventOne one = new TestEventOne("Test2");
    registry.dispatchEvent(one);

    Assert.assertEquals(2, TestListenerOne.callCount);
    Assert.assertEquals(1, TestListenerOneDerived.callCount);
    Assert.assertEquals(1, TestListenerTwo.callCount);
  }
}
