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
public class EventRegistryFilterTest
{
  public static class TestFilter1Event extends MgcFilterEventImpl<String>
  {

  }

  static boolean passFilter1Event = true;

  public static class TestFilter1EventListener implements MgcEventListener<TestFilter1Event>
  {

    @Override
    public void onEvent(TestFilter1Event event)
    {
      event.setResult("InEvent");
      if (passFilter1Event == true) {
        event.nextFilter();
      }
    }

  }

  @Test
  public void testClassFilter()
  {
    SimpleEventClassRegistry clsRegistry = new SimpleEventClassRegistry("testclass");
    clsRegistry.registerListener(TestFilter1EventListener.class);
    TestFilter1Event evnt = new TestFilter1Event();
    passFilter1Event = true;
    String res = clsRegistry.filterEvent(evnt, event -> event.setResult("fallback"));
    Assert.assertEquals("fallback", res);

    evnt = new TestFilter1Event();
    passFilter1Event = false;
    res = clsRegistry.filterEvent(evnt, event -> event.setResult("fallback"));
    Assert.assertEquals("InEvent", res);

  }

  public static class SpecialRegistry extends SimpleEventInstanceRegistry
  {
    public void addOnEvent(MgcEventListener<TestFilter1Event> handler)
    {
      addEventListener(TestFilter1Event.class, handler);
    }
  }

  @Test
  public void testInstanceFilter()
  {
    SpecialRegistry specRegistrz = new SpecialRegistry();

    specRegistrz.addOnEvent(event -> {
      event.setResult("Ich bin es");
      event.nextFilter();
    });
    TestFilter1Event evnt = new TestFilter1Event();
    String res = specRegistrz.filterEvent(evnt, event -> event.setResult("" + event.getResult() + " Roger"));
    Assert.assertEquals("Ich bin es Roger", res);

  }
}
