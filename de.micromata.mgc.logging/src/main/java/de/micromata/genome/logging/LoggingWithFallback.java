package de.micromata.genome.logging;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LoggingWithFallback extends Logging
{
  /**
   * get the fallback logging
   * 
   * @return
   */
  Logging getSecondary();

  /**
   * sets the fallback.
   * 
   * @param secondary
   */
  void setSecondary(Logging secondary);
}
