package de.micromata.genome.jpa.trace;

import de.micromata.genome.util.runtime.CallableX;

/**
 * Implements a Tracer for JPA executions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface JpaTracer
{

  /**
   * Execute a tracer Statement.
   * 
   * @param <T> the generic type
   * @param sql the sql
   * @param keyValues the key values
   * @param callback the callback
   * @return the return value of the callback.
   */
  <T> T execute(String sql, Object[] keyValues, CallableX<T, RuntimeException> callback);
}
