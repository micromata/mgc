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

package de.micromata.genome.stats;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.runtime.CallableX;

/**
 * Interface fuer Stats.
 *
 * @author roger@micromata.de
 */
public interface StatsDAO
{

  /**
   * Fuegt einen Performance-Eintrag hinzu.
   *
   * @param category the category
   * @param pointName DetailName
   * @param millis the millis
   * @param wait Anteil warten bei Critical und Wartezeit bei Scheduler
   */
  public void addPerformance(LogCategory category, String pointName, long millis, long wait);

  /**
   * Wird bei jedem Aufruf des Logging aufgerufen.
   *
   * @param lwe the lwe
   */
  public void addLogging(LogEntry lwe);

  /**
   * Wird bei jedem Aufruf des Logging aufgerufen.
   *
   * @param lwe the lwe
   */
  public void addLogging(LogWriteEntry lwe);

  /**
   * Measure request processing.
   *
   * @param req the req
   * @param millis the millis
   */
  public void addRequest(HttpServletRequest req, long millis);

  /**
   * Run long running op.
   *
   * @param <V> the value type
   * @param <EX> the generic type
   * @param category the category
   * @param pointName the point name
   * @param displayObject the display object
   * @param callback the callback
   * @return the v
   * @throws EX the ex
   */
  public <V, EX extends Throwable> V runLongRunningOp(LogCategory category, String pointName, Object displayObject,
      CallableX<V, EX> callback) throws EX;

  public Collection<CurrentOperation> getCurrentOperations();
}
