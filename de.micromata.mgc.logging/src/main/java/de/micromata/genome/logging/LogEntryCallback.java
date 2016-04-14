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

package de.micromata.genome.logging;

/**
 * Callback which will be called on every fetched LogEntry.
 *
 * @author roger
 */
public interface LogEntryCallback
{

  /**
   * Called on each row.
   *
   * @param le the le
   * @throws EndOfSearch if seach should be terminated
   */
  public void onRow(LogEntry le) throws EndOfSearch;

  /**
   * create a log entry on base a log entry.
   * 
   * This can also simply return the same LogEntry
   *
   * @param le the le
   * @return the log entry
   */
  public default LogEntry createLogEntry(LogEntry le)
  {
    return le;
  }
}
