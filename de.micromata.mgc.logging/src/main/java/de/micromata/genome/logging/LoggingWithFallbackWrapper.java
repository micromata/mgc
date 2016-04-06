package de.micromata.genome.logging;

import de.micromata.genome.logging.spi.LoggingWrapper;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingWithFallbackWrapper extends LoggingWrapper implements LoggingWithFallback
{
  public LoggingWithFallbackWrapper(LoggingWithFallback target)
  {
    super(target);
  }

  public LoggingWithFallbackWrapper()
  {

  }

  private LoggingWithFallback getTargetWithFallback()
  {
    return (LoggingWithFallback) getTarget();

  }

  @Override
  public Logging getSecondary()
  {
    return getTargetWithFallback().getSecondary();
  }

  @Override
  public void setSecondary(Logging secondary)
  {
    getTargetWithFallback().setSecondary(secondary);

  }

}
