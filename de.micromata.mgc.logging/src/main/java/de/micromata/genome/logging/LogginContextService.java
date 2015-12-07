package de.micromata.genome.logging;

/**
 * Provides some context inforation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LogginContextService
{
  /**
   * Current users id for logging.
   * 
   * @return
   */
  String getCurrentUserName();

  /**
   * The current runcontext like session id.
   * 
   * @return
   */
  String getRunContextId();
}
