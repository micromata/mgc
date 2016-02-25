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
