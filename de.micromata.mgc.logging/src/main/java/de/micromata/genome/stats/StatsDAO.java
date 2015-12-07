/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   21.03.2008
// Copyright Micromata 21.03.2008
//
/////////////////////////////////////////////////////////////////////////////
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
