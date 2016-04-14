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
