package de.micromata.genome.util.event;

public class TestListenerOneDerived implements MgcEventListener<TestEventOneDerived>
{
  public static int callCount = 0;

  @Override
  public void onEvent(TestEventOneDerived event)
  {
    System.out.println("Called TestListenerOneDerived with " + event.getValue());
    ++callCount;
  }

}
