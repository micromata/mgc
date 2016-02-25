package de.micromata.genome.util.event;

public class TestEventOne implements MgcEvent
{
  private final String value;

  public TestEventOne(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

}
