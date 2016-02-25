package de.micromata.genome.util.event;

public class TestListenerTwo implements MgcEventListener<TestEventTwo>
{
  public static int callCount = 0;

  @Override
  public void onEvent(TestEventTwo event)
  {
    System.out.println("Called TestListenerTwo with " + event.getValue());
    ++callCount;
  }

}
