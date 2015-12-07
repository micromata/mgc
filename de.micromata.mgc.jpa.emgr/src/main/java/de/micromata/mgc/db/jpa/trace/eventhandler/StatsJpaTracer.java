package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.CallableX;
import de.micromata.mgc.db.jpa.trace.JpaTracer;

/**
 * A Jpa Tracer reporting to genome stats.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StatsJpaTracer implements JpaTracer
{

  /**
   * The instance.
   */
  private static StatsJpaTracer INSTANCE = new StatsJpaTracer();

  /**
   * Gets the.
   *
   * @return the stats jpa tracer
   */
  public static StatsJpaTracer get()
  {
    return INSTANCE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.jpa.JpaTracer#execute(java.lang.String, java.lang.Object[],
   * de.micromata.genome.util.runtime.CallableX)
   */
  @Override
  public <T> T execute(String sql, Object[] keyValues, CallableX<T, RuntimeException> callback)
  {
    long startTime = System.currentTimeMillis();
    try {
      return callback.call();
    } finally {
      long endTime = System.currentTimeMillis();
      LoggingServiceManager.get().getStatsDAO().addPerformance(GenomeLogCategory.Jpa, sql, endTime - startTime, 0);
    }
  }

}
