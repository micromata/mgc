package de.micromata.genome.util.event;

public class TestEventTwo implements MgcEvent
{
  private final String value;

  public TestEventTwo(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

}
