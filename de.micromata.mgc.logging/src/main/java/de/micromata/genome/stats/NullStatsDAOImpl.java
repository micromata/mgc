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
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.runtime.CallableX;

/**
 * Implementation which simply does nothing.
 *
 * @author roger@micromata.de
 */
public class NullStatsDAOImpl implements StatsDAO
{

  @Override
  public void addLogging(LogEntry lwe)
  {

  }

  @Override
  public void addPerformance(LogCategory category, String pointName, long millis, long wait)
  {
  }

  @Override
  public void addRequest(HttpServletRequest req, long millis)
  {

  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<CurrentOperation> getCurrentOperations()
  {
    return Collections.EMPTY_LIST;
  }

  @Override
  public <T, EX extends Throwable> T runLongRunningOp(LogCategory category, String pointName, Object displayObject,
      CallableX<T, EX> callback) throws EX
  {
    return callback.call();
  }

  @Override
  public void addLogging(LogWriteEntry lwe)
  {

  }

}
