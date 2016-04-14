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

package de.micromata.genome.db.jdbc.trace;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * The Class SqlCommands.
 *
 * @author roger
 */
public class SqlCommands
{

  /**
   * The uncommited commands.
   */
  private List<Pair<Savepoint, List<TraceSqlArguments>>> uncommitedCommands = new ArrayList<Pair<Savepoint, List<TraceSqlArguments>>>();

  /**
   * Adds the trace.
   *
   * @param sqlArgs the sql args
   */
  public void addTrace(TraceSqlArguments sqlArgs)
  {
    if (uncommitedCommands.isEmpty() == true) {
      pushSavePoint(null);
    }
    uncommitedCommands.get(uncommitedCommands.size() - 1).getSecond().add(sqlArgs);
  }

  /**
   * Clear savepoint.
   *
   * @param sv the sv
   */
  public void clearSavepoint(Savepoint sv)
  {
    int i = uncommitedCommands.size() - 1;
    for (; i >= 0; --i) {
      Savepoint sp = uncommitedCommands.get(i).getFirst();
      uncommitedCommands.remove(i);
      if (sp == sv) {
        break;
      }
    }

  }

  /**
   * Find savepoint index.
   *
   * @param sv the sv
   * @return the int
   */
  public int findSavepointIndex(Savepoint sv)
  {
    int i = uncommitedCommands.size() - 1;
    for (; i >= 0; --i) {
      if (uncommitedCommands.get(i).getFirst() == sv) {
        return i;
      }
    }
    return i;
  }

  /**
   * Clear.
   */
  public void clear()
  {
    uncommitedCommands.clear();
  }

  /**
   * Push save point.
   *
   * @param sp the sp
   */
  public void pushSavePoint(Savepoint sp)
  {
    uncommitedCommands.add(new Pair<Savepoint, List<TraceSqlArguments>>(sp, new ArrayList<TraceSqlArguments>()));
  }

  public List<Pair<Savepoint, List<TraceSqlArguments>>> getUncommitedCommands()
  {
    return uncommitedCommands;
  }

  public void setUncommitedCommands(List<Pair<Savepoint, List<TraceSqlArguments>>> uncommitedCommands)
  {
    this.uncommitedCommands = uncommitedCommands;
  }

}
