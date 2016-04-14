//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.jpa.trace.eventhandler;

import de.micromata.genome.jpa.trace.JpaTracer;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.CallableX;

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
