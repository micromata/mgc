package de.micromata.genome.chronos;

public class MaxRunChecker
{
  long maxRun;

  long start;

  String message;

  public MaxRunChecker(long maxRun, String message)
  {
    this.maxRun = maxRun;
    this.start = System.currentTimeMillis();
    this.message = message;
  }

  public void check()
  {
    if (start + maxRun < System.currentTimeMillis()) {
      throw new RuntimeException("Test takes to long: " + message);
    }
  }
}