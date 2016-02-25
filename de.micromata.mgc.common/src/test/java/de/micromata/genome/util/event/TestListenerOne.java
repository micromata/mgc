package de.micromata.genome.util.event;

public class TestListenerOne implements MgcEventListener<TestEventOne>
{
  public static int callCount = 0;

  @Override
  public void onEvent(TestEventOne event)
  {
    System.out.println("Called TestListenerOne with " + event.getValue());
    ++callCount;
  }

}
